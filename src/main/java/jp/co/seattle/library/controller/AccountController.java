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

import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.UsersService;

/**
 * アカウント作成コントローラー
 */
@Controller //APIの入り口
public class AccountController {
    final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private UsersService usersService;


    /**
     * アカウント作成画面
     * 
     * @param model モデル
     * @return
     */
    @RequestMapping(value = "/newAccount", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    public String createAccount(Model model) {
        return "createAccount";
    }

    /**
     * 新規アカウント作成
     *
     * @param email メールアドレス
     * @param password パスワード
     * @param passwordForCheck 確認用パスワード
     * @param model
     * @return　ホーム画面に遷移
     */
    @Transactional
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public String createAccount(Locale locale,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("passwordForCheck") String passwordForCheck,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome createAccount! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);


        // TODO バリデーションチェック、パスワード一致チェック実装

        boolean isEmailValid = email
                .matches("^([a-zA-Z0-9])+([a-zA-Z0-9\\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\\._-]+)+$");
        boolean isValidPW = password.matches("^[A-Za-z0-9]+$");
        boolean isValidPWForCheck = passwordForCheck.matches("^[A-Za-z0-9]+$");
        boolean accountcheck = false;
        
        if (usersService.sameAccount(email) != 0) {
            model.addAttribute("mailPWCheck", "既にアカウントが存在しています");

            return "createAccount";
        }

        //メールがメールアドレス形式または半角英数字ではなかった場合
        if (!isEmailValid) {
            // 遷移して欲しい画面の名前
            model.addAttribute("emailError", "メールアドレスにはメール形式、半角英数字を使用してください");
            accountcheck = true;
        }
        //パスワードと確認用パウワードが半角英数字ではなかった場合
        if (!isValidPW || !isValidPWForCheck) {
        // 遷移して欲しい画面の名前
            model.addAttribute("passwordError", "パスワードには半角英数字を使用してください");
            accountcheck = true;
        }

        /// パスワードと確認用パスワードが一致しなかった場合
        if (!password.equals(passwordForCheck)) {
            model.addAttribute("checkPasswordError", "パスワードと確認用パスワードが一致しません");
            accountcheck = true;
        }

        //バリデーションチェックエラーの場合
        if (accountcheck) {
            return "createAccount";
        }
        
        


        userInfo.setPassword(password);
        usersService.registUser(userInfo);

        model.addAttribute("bookList", booksService.getBookList());
        return "home";
    }

}
