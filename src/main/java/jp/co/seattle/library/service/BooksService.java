package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "SELECT books.ID,TITLE,AUTHOR,AUTHOR,PUBLISHER,PUBLISH_DATE,AUTHOR,THUMBNAIL_URL,DESCRIPTION,ISBN,LENDING_STATUS FROM books LEFT OUTER JOIN lending ON books.ID = lending.bookID order by TITLE asc",
                new BookInfoRowMapper());


        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id =" + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {
        //
        String sql = "INSERT INTO books (title, author,publisher,publish_date,thumbnail_name,thumbnail_url,isbn,description,upd_date,reg_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getPublish_date() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getIsbn() + "','"
                + bookInfo.getDescription() + "',"
                + "sysdate(),"
                + "sysdate());";

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍の貸出し
     * 
     * @param bookId
     */
    public void deletingSystem(int bookId) {
        String sql = "delete from books where id=" + bookId + ";";
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍を更新する
     * 
     * @return 遷移先画面
     */
    public int getReturnId() {
        String sql = "select MAX(id) from books";
        int returnId = jdbcTemplate.queryForObject(sql, Integer.class);
        return returnId;
    }

    public void updateBook(BookDetailsInfo bookInfo) {
        String sql = "update books set title='" + bookInfo.getTitle() + "', author='" + bookInfo.getAuthor() +
                "',publisher='" + bookInfo.getPublisher() + "',publish_date='" + bookInfo.getPublish_date() +
                "',thumbnail_name='" + bookInfo.getThumbnailName() + "',thumbnail_url='" + bookInfo.getThumbnailUrl() +
                "',isbn='" + bookInfo.getIsbn() + "',description='" + bookInfo.getDescription() +
                "',upd_date=sysdate()" +
                "where Id=" + bookInfo.getBookId() + ";";

        jdbcTemplate.update(sql);

    }
}
