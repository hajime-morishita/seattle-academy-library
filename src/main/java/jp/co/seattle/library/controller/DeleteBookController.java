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

/**
 * 削除コントローラー
 */
@Controller //APIの入り口
public class DeleteBookController {
    final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

    @Autowired //絶対忘れない
    private BooksService bookdService;
    @Autowired //絶対忘れない
    private LendingService lendingService;

    /**
     * 対象書籍を削除する
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    //@RequestMappingでdeleteBookを受け取る
    @RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
    public String deleteBook(
            Locale locale,
            //@RequestParamでdeletebookのbookIdを受け取る
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        //貸出しステータスの表示（bookIdの要素数が0以外の時）
        if (lendingService.lendingCheck(bookId) != 0) {

            //書籍詳細情報を返す
            model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));
            //deleteErrorに"貸出し中のため削除できません"を返す
            model.addAttribute("deleteError", "貸出し中のため削除できません");
        }

        //deletingSystemに43行目で受け取ったIDを渡す
        bookdService.deletingSystem(bookId);

        //bookListにbookdService.getBookList()でもらった情報を返す
        model.addAttribute("bookList", bookdService.getBookList());

        return "home";

    }

}
