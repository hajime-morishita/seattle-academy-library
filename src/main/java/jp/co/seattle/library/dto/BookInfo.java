package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍基本情報格納DTO
 */
@Configuration
@Data
public class BookInfo {

    private int bookId;

    private String title;

    private String author;

    private String publisher;

    private String publishDate;

    private String thumbnail;

    private String description;

    private String isbn;

    private String lendingService;

    public BookInfo() {

    }

    // コンストラクタ
    public BookInfo(int bookId, String title, String author, String publisher, String publishDate, String thumbnail,
            String description, String isbn, String lendingService) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.thumbnail = thumbnail;
        this.description = description;
        this.isbn = isbn;
        this.lendingService = lendingService;
    }

}