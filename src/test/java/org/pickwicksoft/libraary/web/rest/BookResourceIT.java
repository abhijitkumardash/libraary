package org.pickwicksoft.libraary.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pickwicksoft.libraary.IntegrationTest;
import org.pickwicksoft.libraary.domain.Book;
import org.pickwicksoft.libraary.repository.BookRepository;
import org.pickwicksoft.libraary.security.AuthoritiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class BookResourceIT {

    private static final String DEFAULT_TITLE = "AAA";
    private static final String UPDATED_TITLE = "BBB";

    private static final String DEFAULT_SUBTITLE = "AAA";
    private static final String UPDATED_SUBTITLE = "BBB";
    private static final byte[] DEFAULT_COVER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COVER = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DESCRIPTION = "AAA";
    private static final String UPDATED_DESCRIPTION = "BBB";
    private static final String DEFAULT_ISBN = "AAA";
    private static final String UPDATED_ISBN = "BBB";
    private static final String DEFAULT_PUBLISHER = "AAA";
    private static final String UPDATED_PUBLISHER = "BBB";
    private static final Integer DEFAULT_PUBLICATIONYEAR = 0;
    private static final Integer UPDATED_PUBLICATIONYEAR = 1;
    private static final Integer DEFAULT_PAGES = 0;
    private static final Integer UPDATED_PAGES = 1;

    private static final String ENTITY_API_URL = "/api/book";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookMockMvc;

    private Book book;

    public static Book createEntity() {
        Book book = new Book();

        book.setCover(DEFAULT_COVER);

        book.setTitle(DEFAULT_TITLE);

        book.setSubtitle(DEFAULT_SUBTITLE);

        book.setDescription(DEFAULT_DESCRIPTION);

        book.setIsbn(DEFAULT_ISBN);

        book.setPublisher(DEFAULT_PUBLISHER);

        book.setPublicationYear(DEFAULT_PUBLICATIONYEAR);

        book.setPages(DEFAULT_PAGES);

        return book;
    }

    public static Book createUpdatedEntity() {
        return createUpdatedEntity(new Book());
    }

    public static Book createUpdatedEntity(Book book) {
        book.setCover(UPDATED_COVER);

        book.setTitle(UPDATED_TITLE);

        book.setSubtitle(UPDATED_SUBTITLE);

        book.setDescription(UPDATED_DESCRIPTION);

        book.setIsbn(UPDATED_ISBN);

        book.setPublisher(UPDATED_PUBLISHER);

        book.setPublicationYear(UPDATED_PUBLICATIONYEAR);

        book.setPages(UPDATED_PAGES);

        return book;
    }

    @BeforeEach
    public void initTest() {
        book = createEntity();
    }

    @Test
    @Transactional
    void createBook() throws Exception {
        int dbSizeBeforeCreate = bookRepository.findAll().size();
        restBookMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isCreated());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(dbSizeBeforeCreate + 1);
        Book testBook = bookList.get(bookList.size() - 1);

        assertThat(testBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBook.getSubtitle()).isEqualTo(DEFAULT_SUBTITLE);
        assertThat(testBook.getCover()).isEqualTo(DEFAULT_COVER);
        assertThat(testBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        //assertThat(testBook.getAuthors().get(0).getName()).isEqualTo(DEFAULT_AUTHOR.getName());
        assertThat(testBook.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testBook.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testBook.getPublicationYear()).isEqualTo(DEFAULT_PUBLICATIONYEAR);
        assertThat(testBook.getPages()).isEqualTo(DEFAULT_PAGES);
    }

    @Test
    @Transactional
    void createBookWithExistingId() throws Exception {
        book.setId(1L);
        int dbSizeBeforeCreate = bookRepository.findAll().size();
        restBookMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(dbSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBooks() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get all the bookList
        restBookMockMvc
            .perform(get(ENTITY_API_URL + ""))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(book.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].subtitle").value(hasItem(DEFAULT_SUBTITLE)))
            .andExpect(jsonPath("$.[*].cover").isArray())
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN)))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)))
            .andExpect(jsonPath("$.[*].publicationYear").value(hasItem(DEFAULT_PUBLICATIONYEAR)))
            .andExpect(jsonPath("$.[*].pages").value(hasItem(DEFAULT_PAGES)));
    }

    @Test
    @Transactional
    void getBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        // Get the book
        restBookMockMvc
            .perform(get(ENTITY_API_URL_ID, book.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(book.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.subtitle").value(DEFAULT_SUBTITLE))
            .andExpect(jsonPath("$.cover").isNotEmpty())
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER))
            .andExpect(jsonPath("$.publicationYear").value(DEFAULT_PUBLICATIONYEAR))
            .andExpect(jsonPath("$.pages").value(DEFAULT_PAGES));
    }

    @Test
    @Transactional
    void getNonExistingBook() throws Exception {
        // Get the book
        restBookMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        int databaseSizeBeforeUpdate = bookRepository.findAll().size();

        // Update the book
        Book updatedBook = bookRepository.findById(book.getId()).get();
        // Disconnect from session so that the updates on updatedBook are not directly saved in db
        em.detach(updatedBook);
        updatedBook = createUpdatedEntity(updatedBook);

        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBook.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBook))
            )
            .andExpect(status().isOk());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
        Book testBook = bookList.get(bookList.size() - 1);

        assertThat(testBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        //assertThat(testBook.getAuthors().get(0).getName()).isEqualTo(UPDATED_AUTHOR.getName());
        assertThat(testBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testBook.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testBook.getPublicationYear()).isEqualTo(UPDATED_PUBLICATIONYEAR);
        assertThat(testBook.getPages()).isEqualTo(UPDATED_PAGES);
    }

    @Test
    @Transactional
    void putNonExistingBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, book.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isBadRequest());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBook() throws Exception {
        int databaseSizeBeforeUpdate = bookRepository.findAll().size();
        book.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(book))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Book in the database
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBook() throws Exception {
        // Initialize the database
        bookRepository.saveAndFlush(book);

        int databaseSizeBeforeDelete = bookRepository.findAll().size();

        // Delete the book
        restBookMockMvc
            .perform(delete(ENTITY_API_URL_ID, book.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Book> bookList = bookRepository.findAll();
        assertThat(bookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
