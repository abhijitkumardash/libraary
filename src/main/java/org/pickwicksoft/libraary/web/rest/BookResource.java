package org.pickwicksoft.libraary.web.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.validation.Valid;
import org.pickwicksoft.libraary.domain.Book;
import org.pickwicksoft.libraary.repository.BookRepository;
import org.pickwicksoft.libraary.service.BookService;
import org.pickwicksoft.libraary.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/api")
public class BookResource {

    private static final String ENTITY_NAME = "Book";
    private final Logger log = LoggerFactory.getLogger(org.pickwicksoft.libraary.web.rest.BookResource.class);
    private final BookRepository bookRepository;
    private final BookService bookService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public BookResource(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    /**
     * {@code GET  /book/items} : get all the book/items.
     *
     * @return the {@link List} of {@link Book}s.
     */
    @GetMapping("/book")
    public List<Book> getBooks() {
        log.debug("REST request to get all books");
        return bookRepository.findAll();
    }

    /**
     * {@code GET  /book/items/:id} : get one book by id.
     *
     * @param id the id of the book to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the book, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get book : {}", id);
        return ResponseUtil.wrapOrNotFound(bookRepository.findById(id));
    }

    @GetMapping("/book/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) throws ExecutionException, InterruptedException {
        log.debug("REST request to get book by isbn from bookgrabber : {}", isbn);
        CompletableFuture<Optional<Book>> book = bookService.getBookByISBN(isbn);
        CompletableFuture.allOf(book).join();
        return ResponseUtil.wrapOrNotFound(book.get());
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save book : {}", book);
        if (book.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        System.out.println(book.getLanguages());
        Book result = bookRepository.save(book);
        return ResponseEntity
            .created(new URI("/api/book/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable Long id) {
        log.debug("REST request to update book : {}", book);
        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, book.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Book result = bookRepository.save(book);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, book.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete book : {}", id);
        if (!bookRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        bookRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
