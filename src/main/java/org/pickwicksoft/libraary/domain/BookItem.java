package org.pickwicksoft.libraary.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_item")
public class BookItem extends Book {

    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "isReferenceOnly", nullable = false)
    private Boolean isReferenceOnly;

    @Column(name = "borrowed", nullable = false)
    private Date borrowed;

    @Column(name = "dueDate", nullable = false)
    private Date dueDate;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "format", nullable = false)
    private BookFormat format;

    @Column(name = "status", nullable = false)
    private BookStatus status;

    @Column(name = "dateOfPurchase", nullable = false)
    private Date dateOfPurchase;

    @Column(name = "publicationDate", nullable = false)
    private Date publicationDate;

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
}
