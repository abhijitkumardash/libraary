package org.pickwicksoft.libraary.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.pickwicksoft.bookgrabber.BookGrabber;
import org.pickwicksoft.libraary.domain.Book;
import org.pickwicksoft.libraary.repository.LanguageRepository;
import org.pickwicksoft.libraary.service.mapper.BookMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookGrabber bookGrabber = new BookGrabber();
    private final LanguageRepository languageRepository;

    public BookService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Async
    public CompletableFuture<Optional<Book>> getBookByISBN(String isbn) {
        Optional<org.pickwicksoft.bookgrabber.model.Book> grabbedBook = bookGrabber.getBookByISBN(isbn);
        return CompletableFuture.completedFuture(grabbedBook.map(this::mapToDomainBook));
    }

    private Book mapToDomainBook(org.pickwicksoft.bookgrabber.model.Book grabbedBook) {
        Book book = BookMapper.INSTANCE.bookToDomainBook(grabbedBook);
        book = mapLanguage(book, grabbedBook.getLanguage());
        book = mapCover(book, grabbedBook.getCover().getLarge());
        return book;
    }

    private Book mapCover(Book book, String url) {
        book.setCover(bookGrabber.getCoverFromURL(url).orElse(null));
        return book;
    }

    private Book mapLanguage(Book book, String language) {
        return book;
    }
}
