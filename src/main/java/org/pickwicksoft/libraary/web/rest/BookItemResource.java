package org.pickwicksoft.libraary.web.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.pickwicksoft.libraary.domain.BookItem;
import org.pickwicksoft.libraary.repository.BookItemRepository;
import org.pickwicksoft.libraary.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/api")
public class BookItemResource {

    private final Logger log = LoggerFactory.getLogger(BookItemResource.class);

    private static final String ENTITY_NAME = "BookItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookItemRepository bookItemRepository;

    public BookItemResource(BookItemRepository bookItemRepository) {
        this.bookItemRepository = bookItemRepository;
    }

    /**
     * {@code GET  /book/items} : get all the book item.
     *
     * @return the {@link List} of {@link BookItem}s.
     */
    @GetMapping("/book/items")
    public ResponseEntity<List<BookItem>> getAllBookItems(
        @And(
            {
                @Spec(path = "book.title", params = "title", spec = LikeIgnoreCase.class),
                @Spec(path = "book.authors.name", params = "name", spec = LikeIgnoreCase.class),
                @Spec(path = "book.isbn", params = "isbn", spec = Equal.class),
                @Spec(path = "format", params = "format", spec = In.class),
            }
        ) Specification<BookItem> spec,
        @RequestParam(required = false, defaultValue = "") String author,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all books");
        Page<BookItem> page;
        if (author.isEmpty()) {
            page = bookItemRepository.findAll(spec, pageable);
        } else {
            List<BookItem> books = bookItemRepository.findAll(spec);
            // Filter all books where one or more authors match the authorName
            books.removeIf(bookItem -> bookItem.getBook().getAuthors().stream().noneMatch(a -> a.getName().contains(author)));
            final int start = (int) pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), books.size());
            page = new PageImpl<>(books.subList(start, end), pageable, books.size());
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book/items/:id} : get one book item by id.
     *
     * @param id the id of thebook itemto retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the book, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book/items/{id}")
    public ResponseEntity<BookItem> getBookItem(@PathVariable UUID id) {
        log.debug("REST request to get book item: {}", id);
        return ResponseUtil.wrapOrNotFound(bookItemRepository.findById(id));
    }

    @PostMapping("/book/items")
    public ResponseEntity<BookItem> createBookItem(@Valid @RequestBody BookItem book) throws URISyntaxException {
        log.debug("REST request to save book item: {}", book);
        if (book.getId() != null) {
            throw new BadRequestAlertException("A new book item cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookItem result = bookItemRepository.save(book);
        return ResponseEntity
            .created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/book/items/{id}")
    public ResponseEntity<BookItem> updateBookItem(@RequestBody BookItem book, @PathVariable UUID id) {
        log.debug("REST request to update book item: {}", book);
        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, book.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookItem result = bookItemRepository.save(book);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, book.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/book/items/{id}")
    public ResponseEntity<Void> deleteBookItem(@PathVariable UUID id) {
        log.debug("REST request to delete book item: {}", id);
        bookItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
