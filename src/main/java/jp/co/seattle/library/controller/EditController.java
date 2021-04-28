package jp.co.seattle.library.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class EditController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/editBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
        //本の詳細を写す

        booksService.getBookInfo(bookId);
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        return "editBook";
    }

    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateBook(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("publish_date") String publish_date,
            @RequestParam("isbn") String isbn,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome editBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setDescription(description);
        bookInfo.setPublish_date(publish_date);
        bookInfo.setIsbn(isbn);
        bookInfo.setBookId(bookId);

        boolean isIsbnValid = isbn.matches("(^\\d{10,13}$)?");
        boolean checkId = false;

        if (!isIsbnValid) {
            model.addAttribute("error", "ISBNは10字または13字の半角英数字を入力してください");
            checkId = true;
        }

        try {
            DateFormat df = new SimpleDateFormat("yyyymmdd");
            df.setLenient(false);
            df.parse(publish_date);
        } catch (ParseException p) {
            model.addAttribute("error1", "出版日は半角数字のYYYYMMDD形式で入力してください");
            checkId = true;
        }
        if (checkId) {
            return "editBook";
        }

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "editBook";
            }
        }

        // 書籍情報を新規登録する
        booksService.updateBook(bookInfo);
        //booksService.getBookInfo(bookId);
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));



        return "details";
    }

}
