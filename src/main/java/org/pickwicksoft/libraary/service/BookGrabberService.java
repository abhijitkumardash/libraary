package org.pickwicksoft.libraary.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.pickwicksoft.bookgrabber.BookGrabber;
import org.pickwicksoft.libraary.domain.Book;
import org.pickwicksoft.libraary.repository.AuthorRepository;
import org.pickwicksoft.libraary.repository.LanguageRepository;
import org.pickwicksoft.libraary.service.mapper.BookMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BookGrabberService {

    private final BookGrabber bookGrabber;
    private final LanguageRepository languageRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;

    public BookGrabberService(
        LanguageRepository languageRepository,
        BookGrabber bookGrabber,
        BookMapper bookMapper,
        AuthorRepository authorRepository
    ) {
        this.languageRepository = languageRepository;
        this.bookGrabber = bookGrabber;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
    }

    @Async
    public CompletableFuture<Optional<Book>> getBookByISBN(String isbn) {
        Optional<org.pickwicksoft.bookgrabber.model.Book> grabbedBook = bookGrabber.getBookByISBN(isbn);
        return CompletableFuture.completedFuture(grabbedBook.map(book -> mapToDomainBook(book, isbn)));
    }

    private Book mapToDomainBook(org.pickwicksoft.bookgrabber.model.Book grabbedBook, String isbn) {
        Book book = bookMapper.bookToDomainBook(grabbedBook);
        if (book.getIsbn() == null) {
            book.setIsbn(Long.decode(isbn));
        }
        book = mapLanguage(book, grabbedBook.getLanguage());
        book = mapCover(book, grabbedBook.getCover().getLarge());
        book = mapAuthors(book);
        return book;
    }

    private Book mapAuthors(Book book) {
        var authors = book.getAuthors();
        authors.forEach(book::removeAuthor);
        authors
            .stream()
            .map(author -> {
                var existingAuthor = authorRepository.findAuthorByName(author.getName());
                return existingAuthor.orElse(author);
            })
            .forEach(book::addAuthor);
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
