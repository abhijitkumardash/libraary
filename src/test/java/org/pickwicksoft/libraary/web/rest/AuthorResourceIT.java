package org.pickwicksoft.libraary.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pickwicksoft.libraary.IntegrationTest;
import org.pickwicksoft.libraary.domain.Author;
import org.pickwicksoft.libraary.domain.Book;
import org.pickwicksoft.libraary.repository.AuthorRepository;
import org.pickwicksoft.libraary.repository.BookRepository;
import org.pickwicksoft.libraary.security.AuthoritiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class AuthorResourceIT {

    private static final String DEFAULT_NAME = "AAA";

    private static final String UPDATED_NAME = "BBB";
    private static final String ENTITY_API_URL = "/api/author";

    private static final String ENTITY_API_URL_BOOK = "/api/book";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthorMockMvc;

    private Author author;

    @Autowired
    private BookRepository bookRepository;

    public Author createEntity() {
        Author author = new Author();
        author.setName(DEFAULT_NAME);
        return author;
    }

    public Author createUpdatedEntity() {
        Author author = new Author();
        author.setName(UPDATED_NAME);
        return author;
    }

    private Book createBook(Boolean updated) {
        Book book = new Book();
        book.setIsbn(UUID.randomUUID().toString());
        if (updated == true) {
            book.setTitle("AAA");
        } else {
            book.setTitle("BBB");
        }
        book.setSubtitle("AAA");
        book.setCover(TestUtil.createByteArray(1, "0"));
        book.setPublisher("Test Publisher");
        book.setPublicationYear(2020);
        book.setPages(100);
        book.setDescription("Test Subject");
        book = bookRepository.save(book);
        return book;
    }

    @BeforeEach
    public void initTest() {
        author = createEntity();
    }

    @Test
    @Transactional
    void getAllAuthors() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        restAuthorMockMvc
            .perform(MockMvcRequestBuilders.get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        restAuthorMockMvc
            .perform(MockMvcRequestBuilders.get(ENTITY_API_URL + "/" + author.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(author.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAuthor() throws Exception {
        restAuthorMockMvc.perform(MockMvcRequestBuilders.get(ENTITY_API_URL + "/" + Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void getAuthorsByBookId() throws Exception {
        // Initialize the database
        var _author = authorRepository.saveAndFlush(author);
        Book book = createBook(false);
        book.addAuthor(_author);
        bookRepository.saveAndFlush(book);

        restAuthorMockMvc
            .perform(MockMvcRequestBuilders.get(ENTITY_API_URL_BOOK + "/" + book.getId() + "/authors"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(_author.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAuthorsByNonExistingBookId() throws Exception {
        restAuthorMockMvc
            .perform(MockMvcRequestBuilders.get(ENTITY_API_URL_BOOK + "/" + Long.MAX_VALUE + "/authors"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteAuthor() throws Exception {
        authorRepository.saveAndFlush(author);

        int databaseSizeBeforeDelete = authorRepository.findAll().size();

        restAuthorMockMvc
            .perform(MockMvcRequestBuilders.delete(ENTITY_API_URL + "/" + author.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        var authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void updateAuthor() throws Exception {
        // Initialize the database
        authorRepository.saveAndFlush(author);

        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        // Update the author
        Author updatedAuthor = authorRepository.findById(author.getId()).get();
        // Disconnect from session so that the updates on updatedAuthor are not directly saved in db
        em.detach(updatedAuthor);
        updatedAuthor.setName(UPDATED_NAME);

        restAuthorMockMvc
            .perform(
                MockMvcRequestBuilders
                    .put(ENTITY_API_URL + "/" + updatedAuthor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAuthor))
            )
            .andExpect(status().isOk());

        // Validate the Author in the database
        var authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        var testAuthor = authorList.get(authorList.size() - 1);
        assertThat(testAuthor.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void postNewAuthorToBook() throws Exception {
        var _book = createBook(false);
        bookRepository.saveAndFlush(_book);

        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        restAuthorMockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(ENTITY_API_URL_BOOK + "/" + _book.getId() + "/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(author))
            )
            .andExpect(status().isCreated());

        var authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate + 1);
        var createdAuthor = bookRepository.findById(_book.getId()).get().getAuthors().get(0);
        assertThat(createdAuthor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void postExistingAuthorToBook() throws Exception {
        var _book = createBook(false);
        bookRepository.saveAndFlush(_book);
        var _author = authorRepository.saveAndFlush(author);

        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        restAuthorMockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(ENTITY_API_URL_BOOK + "/" + _book.getId() + "/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(_author))
            )
            .andExpect(status().isCreated());

        var authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        var createdAuthor = bookRepository.findById(_book.getId()).get().getAuthors().get(0);
        assertThat(createdAuthor.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void postExistingAuthorToNonExistingBook() throws Exception {
        var _author = authorRepository.saveAndFlush(author);

        restAuthorMockMvc
            .perform(
                MockMvcRequestBuilders
                    .post(ENTITY_API_URL_BOOK + "/" + Long.MAX_VALUE + "/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(_author))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteAuthorFromBook() throws Exception {
        var _book = createBook(false);
        bookRepository.saveAndFlush(_book);
        var _author = authorRepository.saveAndFlush(author);
        _book.addAuthor(_author);
        bookRepository.saveAndFlush(_book);

        int databaseSizeBeforeUpdate = authorRepository.findAll().size();

        restAuthorMockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete(ENTITY_API_URL_BOOK + "/" + _book.getId() + "/authors/" + _author.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        var authorList = authorRepository.findAll();
        assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
        var book = bookRepository.findById(_book.getId()).get();
        assertThat(book.getAuthors()).isEmpty();
    }

    @Test
    @Transactional
    void deleteAuthorFromNonExistingBook() throws Exception {
        var _author = authorRepository.saveAndFlush(author);

        restAuthorMockMvc
            .perform(
                MockMvcRequestBuilders
                    .delete(ENTITY_API_URL_BOOK + "/" + Long.MAX_VALUE + "/authors/" + _author.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
    }
}
