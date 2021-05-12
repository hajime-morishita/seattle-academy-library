package jp.co.seattle.library.controller;

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

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.LendingService;

@Controller //APIの入り口
public class LendingController {
    final static Logger logger = LoggerFactory.getLogger(LendingController.class);

    @Autowired //絶対忘れない
    private LendingService lendingService; //LendingServiceクラスのインスタンス化
    @Autowired //絶対忘れない
    private BooksService bookdService;//BooksServiceクラスのインスタンス化


    /**
     * 対象書籍を削除機能（借りる）
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    //@RequestMappingでrentbookを受け取る
    @RequestMapping(value = "/rentBook", method = RequestMethod.POST)
    public String rentBook(
            Locale locale,
            //@RequestParamでrentbookのbookIdを受け取る
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome LendingController! The client locale is {}.", locale);

        //貸出しの条件式（もし要素数が0なら[貸出しされていなかったら]）
        if (lendingService.lendingCheck(bookId) == 0) {
            //rentSystemに0を返す
            lendingService.rentSystem(bookId);
        }

        //書籍詳細情報を返す
        model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));
        //lendingに"貸出し中"を返す
        model.addAttribute("lending", "貸出し中");

        return "details";


    }

    /**
     * 対象書籍を削除機能（返す）
     * 
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル
     * @return
     */
    @Transactional
    //@RequestMappingでreturnBookを受け取る
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
    public String returnBook(
            Locale locale,
            //@RequestParamでreturnBookのbookIdを受け取る
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome LendingController! The client locale is {}.", locale);

        //返す条件式（もし要素数が0以外なら）
        if (lendingService.lendingCheck(bookId) != 0) {
            //returnSystemに1を返す
            lendingService.returnSystem(bookId);
        }

        //書籍詳細情報を返す
        model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));
        //lendingに"貸出し可"を返す
        model.addAttribute("lending", "貸出し可");

        return "details";


    }


}
