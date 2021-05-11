package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkRegistController {
    final static Logger logger = LoggerFactory.getLogger(BulkRegistController.class);

    @Autowired
    private BooksService booksService;


    /**
     * 一括登録画面に遷移
     * 
     * @param model モデル
     * @return
     */
    @RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "bulkRegistration";
    }

    /**
     * 一括登録機能
     * 
     * @param locale ロケール情報
     * @param uploadFile アップデートファイル
     * @param model モデル
     * @return
     */
    @Transactional
    @RequestMapping(value = "/bulkRegistSystem", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String bulkRegistSystem(Locale locale,
            @RequestParam("csvFile") MultipartFile uploadFile, Model model) {
        logger.info("Welcome bulkRegist.java! The client locale is {}.", locale);

        try (InputStream stream = uploadFile.getInputStream();
                Reader reader = new InputStreamReader(stream);
                BufferedReader buf = new BufferedReader(reader);) {

            ArrayList<BookDetailsInfo> bookList = new ArrayList<BookDetailsInfo>();
            ArrayList<String> errorList = new ArrayList<String>();
            int count = 1;

            String line;

            while ((line = buf.readLine()) != null) {
                String[] data = new String[6];
                data = line.split(",", -1);

                BookDetailsInfo bookInfo = new BookDetailsInfo();

                bookInfo.setDescription(data[5]);

                boolean checkId = false;

                if (StringUtils.isEmpty(data[0]) ||
                        StringUtils.isEmpty(data[1]) ||
                        StringUtils.isEmpty(data[2]) ||
                        StringUtils.isEmpty(data[3])) {
                    checkId = true;
                }
                bookInfo.setTitle(data[0]);
                bookInfo.setAuthor(data[1]);
                bookInfo.setPublisher(data[2]);

                boolean isIsbnValid = data[4].matches("(^\\d{10}|\\d{13}$)?");

                if (!isIsbnValid) {

                    checkId = true;
                }
                bookInfo.setIsbn(data[4]);

                try {
                    DateFormat df = new SimpleDateFormat("yyyymmdd");
                    df.setLenient(false);
                    df.parse(data[3]);

                    bookInfo.setPublish_date(data[3]);

                } catch (ParseException p) {

                    checkId = true;
                }
                if (checkId) {
                    errorList.add(count + "行目の書籍情報登録でバリデーションエラー");

                }

                bookList.add(bookInfo);

                count++;



            }

            if (!CollectionUtils.isEmpty(errorList)) {
                model.addAttribute("errorMessage", errorList);
                return "bulkRegistration";
            }
            for (BookDetailsInfo list : bookList) {
                booksService.registBook(list);
            }

            model.addAttribute("resultMessage", "登録完了");
            return "bulkRegistration";

        } catch (FileNotFoundException e) {
            model.addAttribute("ファイルが存在しません");
            return "bulkRegistration";
        } catch (IOException ie) {
            model.addAttribute("ファイル読み込みに失敗しました");
            return "bulkRegistration";
        } catch (Exception e) {
            model.addAttribute("その他の例外が発生しました");
            return "bulkRegistration";
        }
    }

}