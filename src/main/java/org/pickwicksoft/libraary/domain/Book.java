package org.pickwicksoft.libraary.domain;

import javax.persistence.*;

@MappedSuperclass
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "publicationYear", nullable = false)
    private Integer publicationYear;

    @Column(name = "pages", nullable = false)
    private Integer pages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getYear() {
        return publicationYear;
    }

    public void setYear(Integer year) {
        this.publicationYear = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
