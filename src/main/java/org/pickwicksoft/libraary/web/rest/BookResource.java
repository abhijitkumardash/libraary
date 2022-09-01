package org.pickwicksoft.libraary.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.pickwicksoft.libraary.domain.BookItem;
import org.pickwicksoft.libraary.repository.BookItemRepository;
import org.pickwicksoft.libraary.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "book";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookItemRepository bookRepository;

    public BookResource(BookItemRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * {@code GET  /books} : get all the books.
     * @return the {@link List} of {@link BookItem}s.
     */
    @GetMapping("/books")
    public List<BookItem> getBooks() {
        log.debug("REST request to get all books");
        return bookRepository.findAll();
    }

    /**
     * {@code GET  /books/:id} : get one book by id.
     *
     * @param id the id of the book to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the book, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BookItem> getBook(@PathVariable Long id) {
        log.debug("REST request to get book : {}", id);
        return ResponseUtil.wrapOrNotFound(bookRepository.findById(id));
    }

    @PostMapping("/books")
    public BookItem createBook(@RequestBody BookItem book) {
        log.debug("REST request to save book : {}", book);
        return bookRepository.save(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookItem> updateBook(@RequestBody BookItem book, @PathVariable Long id) throws URISyntaxException {
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

        BookItem result = bookRepository.save(book);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, book.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete book : {}", id);
        bookRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
