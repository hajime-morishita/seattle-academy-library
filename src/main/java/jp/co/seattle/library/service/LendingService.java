package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LendingService {
    final static Logger logger = LoggerFactory.getLogger(LendingService.class);

    @Autowired //絶対忘れない
    private JdbcTemplate jdbcTemplate;

    /**
     *  貸出しシステム
     * 
     * @param bookId
     */
    //0を受け取った時動く      ↓ここに入る
    public void rentSystem(int bookId) {

        //SQLの条件式（lendingテーブルのbookIDカラムに(int bookId)で受け取った値が入る）
        //DBに追加
        String sql = "insert into lending (bookID) values (" + bookId + ");";

        //jdbcTemplateはSQLの時に使う
        //updateは何もreturnしない
        jdbcTemplate.update(sql);

    }

    /**
     * 返却システム
     * 
     * @param bookId
     */
    //1を受け取った時に動く       ↓ここに入る
    public void returnSystem(int bookId) {

        //SQLの条件式（lendingテーブルのbookIDカラムに(int bookId)で受け取った値が入る）
        //DBにから削除
        String sql = "delete from lending where bookID=" + bookId + ";";

        //jdbcTemplateはSQLの時に使う
        //updateは何もreturnしない
        jdbcTemplate.update(sql);

    }

    /**
     *  貸出しステータス
     * 
     * @param bookId
     * @return　リターン
     */
    public int lendingCheck(int bookId) {
        String sql = "select count(*) from lending where bookID=" + bookId + ";";
        int lendCheck = jdbcTemplate.queryForObject(sql, Integer.class);

        return lendCheck;

    }

}
