package org.pickwicksoft.libraary.web.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Optional;
import org.pickwicksoft.libraary.domain.Author;
import org.pickwicksoft.libraary.domain.Book;
import org.pickwicksoft.libraary.repository.AuthorRepository;
import org.pickwicksoft.libraary.repository.BookRepository;
import org.pickwicksoft.libraary.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/api")
public class AuthorResource {

    private static final String ENTITY_NAME = "Author";
    private final Logger log = LoggerFactory.getLogger(org.pickwicksoft.libraary.web.rest.AuthorResource.class);
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AuthorResource(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * {@code GET  /author} : get all the authors.
     *
     * @return the {@link List} of {@link Author}s.
     */
    @GetMapping("/author")
    public List<Author> getAuthors() {
        log.debug("REST request to get all authors");
        return authorRepository.findAll();
    }

    /**
     * {@code GET  /author/:id} : get one author by id.
     *
     * @param id the id of the author to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the author, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/author/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        log.debug("REST request to get Author : {}", id);
        Optional<Author> author = authorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(author);
    }

    @GetMapping("/book/{bookId}/authors")
    public List<Author> getAuthorsByBookId(@PathVariable Long bookId) {
        log.debug("REST request to get all authors by book id");
        if (bookRepository.findById(bookId).isPresent()) {
            return authorRepository.findAuthorsByBooksId(bookId);
        } else {
            throw new BadRequestAlertException("Book not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code DELETE  /author/:id} : delete the "id" author.
     *
     * @param id the id of the author to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/author/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);
        authorRepository
            .findById(id)
            .ifPresent(author ->
                author
                    .getBooks()
                    .forEach(book -> {
                        book.removeAuthor(id);
                        bookRepository.save(book);
                    })
            );
        authorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/book/{bookId}/authors")
    public ResponseEntity<Author> addAuthorToBook(@PathVariable Long bookId, @RequestBody Author authorRequest) {
        Author author = bookRepository
            .findById(bookId)
            .map(book -> {
                if (authorRequest.getId() != null) {
                    Author _author = authorRepository
                        .findById(authorRequest.getId())
                        .orElseThrow(() -> new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid"));
                    book.addAuthor(_author);
                    bookRepository.save(book);
                    return _author;
                }

                var _author = authorRepository.save(authorRequest);
                book.addAuthor(_author);
                bookRepository.save(book);
                return _author;
            })
            .orElseThrow(() -> new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid"));
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @DeleteMapping("/book/{bookId}/authors/{authorId}")
    public ResponseEntity<HttpStatus> deleteAuthorFromBook(
        @PathVariable(value = "bookId") Long bookId,
        @PathVariable(value = "authorId") Long authorId
    ) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid"));

        book.removeAuthor(authorId);
        bookRepository.save(book);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/author/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorRequest) {
        authorRepository.findById(id).orElseThrow(() -> new BadRequestAlertException("Invalid id", ENTITY_NAME, "idinvalid"));

        return new ResponseEntity<>(authorRepository.save(authorRequest), HttpStatus.OK);
    }
}
