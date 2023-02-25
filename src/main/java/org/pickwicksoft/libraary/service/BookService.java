package org.pickwicksoft.libraary.service;

import org.pickwicksoft.libraary.domain.BookItem;
import org.pickwicksoft.libraary.repository.BookItemRepository;
import org.pickwicksoft.libraary.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, BookItemRepository bookItemRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.bookItemRepository = bookItemRepository;
        this.authorService = authorService;
    }

    public BookItem updateBook(BookItem bookItem) {
        if (bookItem.getId() == null && bookItem.getBook().getId() != null) {
            var book = bookRepository.save(bookItem.getBook());
            bookItem.setBook(book);
        }
        return bookItemRepository.save(bookItem);
    }
}
