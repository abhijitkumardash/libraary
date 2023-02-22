package org.pickwicksoft.libraary.domain;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "book_item")
public class BookItem {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @NotNull
    private Book book;

    @Column(name = "barcode", nullable = false)
    private String barcode = "0000000000000";

    @Column(name = "isReferenceOnly", nullable = false)
    private Boolean isReferenceOnly = false;

    @Column(name = "borrowed")
    private Date borrowed;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "format", nullable = false)
    private BookFormat format;

    @Column(name = "status", nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @Column(name = "dateOfPurchase")
    private Date dateOfPurchase = new Date();

    @Column(name = "publicationDate", nullable = false)
    private Date publicationDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Boolean getReferenceOnly() {
        return isReferenceOnly;
    }

    public void setReferenceOnly(Boolean referenceOnly) {
        isReferenceOnly = referenceOnly;
    }

    public Date getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Date borrowed) {
        this.borrowed = borrowed;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public BookFormat getFormat() {
        return format;
    }

    public void setFormat(BookFormat format) {
        this.format = format;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
