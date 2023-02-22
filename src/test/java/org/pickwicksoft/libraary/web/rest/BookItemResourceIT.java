package org.pickwicksoft.libraary.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pickwicksoft.libraary.IntegrationTest;
import org.pickwicksoft.libraary.domain.*;
import org.pickwicksoft.libraary.repository.BookItemRepository;
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
class BookItemResourceIT {

    private static final String DEFAULT_BARCODE = "AAA";
    private static final String UPDATED_BARCODE = "BBB";
    private static final Boolean DEFAULT_ISREFERENCEONLY = false;
    private static final Boolean UPDATED_ISREFERENCEONLY = true;
    private static final Date DEFAULT_BORROWED = new Date();
    private static final Date UPDATED_BORROWED = new Date();
    private static final Date DEFAULT_DUEDATE = new Date();
    private static final Date UPDATED_DUEDATE = new Date();
    private static final Double DEFAULT_PRICE = (double) 0;
    private static final Double UPDATED_PRICE = 1.0;
    private static final BookFormat DEFAULT_FORMAT = BookFormat.HARDCOVER;
    private static final BookFormat UPDATED_FORMAT = BookFormat.MAGAZINE;
    private static final BookStatus DEFAULT_STATUS = BookStatus.AVAILABLE;
    private static final BookStatus UPDATED_STATUS = BookStatus.LOANED;
    private static final Date DEFAULT_DATEOFPURCHASE = new Date();
    private static final Date UPDATED_DATEOFPURCHASE = new Date();
    private static final Date DEFAULT_PUBLICATIONDATE = new Date();
    private static final Date UPDATED_PUBLICATIONDATE = new Date();

    private static final String ENTITY_API_URL = "/api/book/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookItemRepository bookitemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookItemMockMvc;

    private BookItem bookitem;

    public static BookItem createUpdatedEntity(BookItem bookitem) {
        bookitem.setBarcode(UPDATED_BARCODE);

        bookitem.setReferenceOnly(UPDATED_ISREFERENCEONLY);

        bookitem.setBorrowed(UPDATED_BORROWED);

        bookitem.setDueDate(UPDATED_DUEDATE);

        bookitem.setPrice(UPDATED_PRICE);

        bookitem.setFormat(UPDATED_FORMAT);

        bookitem.setStatus(UPDATED_STATUS);

        bookitem.setDateOfPurchase(UPDATED_DATEOFPURCHASE);

        bookitem.setPublicationDate(UPDATED_PUBLICATIONDATE);

        return bookitem;
    }

    public BookItem createEntity() {
        BookItem bookitem = new BookItem();

        bookitem.setBarcode(DEFAULT_BARCODE);

        bookitem.setReferenceOnly(DEFAULT_ISREFERENCEONLY);

        bookitem.setBorrowed(DEFAULT_BORROWED);

        bookitem.setDueDate(DEFAULT_DUEDATE);

        bookitem.setPrice(DEFAULT_PRICE);

        bookitem.setFormat(DEFAULT_FORMAT);

        bookitem.setStatus(DEFAULT_STATUS);

        bookitem.setDateOfPurchase(DEFAULT_DATEOFPURCHASE);

        bookitem.setPublicationDate(DEFAULT_PUBLICATIONDATE);

        bookitem.setBook(this.createBook(false));

        return bookitem;
    }

    private Book createBook(Boolean updated) {
        Book book = new Book();
        book.setIsbn(9999999999999L);
        if (updated) {
            book.setTitle("AAA");
        } else {
            book.setTitle("BBB");
        }
        book.setSubtitle("AAA");
        book.setCover(TestUtil.createByteArray(1, "0"));
        book.addAuthor(new Author("AAA"));
        book.setPublisher("Test Publisher");
        book.setPublicationYear(2020);
        book.setPages(100);
        book.setDescription("Test Subject");
        book = bookRepository.save(book);
        return book;
    }

    public BookItem createUpdatedEntity() {
        BookItem bookitem = new BookItem();

        bookitem.setBarcode(UPDATED_BARCODE);

        bookitem.setReferenceOnly(UPDATED_ISREFERENCEONLY);

        bookitem.setBorrowed(UPDATED_BORROWED);

        bookitem.setDueDate(UPDATED_DUEDATE);

        bookitem.setPrice(UPDATED_PRICE);

        bookitem.setFormat(UPDATED_FORMAT);

        bookitem.setStatus(UPDATED_STATUS);

        bookitem.setDateOfPurchase(UPDATED_DATEOFPURCHASE);

        bookitem.setPublicationDate(UPDATED_PUBLICATIONDATE);

        bookitem.setBook(createBook(true));

        return bookitem;
    }

    @BeforeEach
    public void initTest() {
        bookitem = createEntity();
    }

    @Test
    @Transactional
    void createBookItem() throws Exception {
        int dbSizeBeforeCreate = bookitemRepository.findAll().size();
        restBookItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookitem))
            )
            .andExpect(status().isCreated());

        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(dbSizeBeforeCreate + 1);
        BookItem testBookItem = bookitemList.get(bookitemList.size() - 1);

        assertThat(testBookItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testBookItem.getReferenceOnly()).isEqualTo(DEFAULT_ISREFERENCEONLY);
        assertThat(testBookItem.getBorrowed()).isEqualTo(DEFAULT_BORROWED);
        assertThat(testBookItem.getDueDate()).isEqualTo(DEFAULT_DUEDATE);
        assertThat(testBookItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testBookItem.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testBookItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBookItem.getDateOfPurchase()).isEqualTo(DEFAULT_DATEOFPURCHASE);
        assertThat(testBookItem.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATIONDATE);
    }

    @Test
    @Transactional
    void createBookItemWithExistingId() throws Exception {
        bookitem.setId(UUID.randomUUID());
        int dbSizeBeforeCreate = bookitemRepository.findAll().size();
        restBookItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookitem))
            )
            .andExpect(status().isBadRequest());

        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(dbSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookItems() throws Exception {
        // Initialize the database
        bookitemRepository.saveAndFlush(bookitem);

        // Get all the bookitems
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookitem.getId().toString())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].referenceOnly").value(hasItem(DEFAULT_ISREFERENCEONLY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getAllBookItemsByTitleAndIsbnAndAuthor() throws Exception {
        // Initialize the database
        bookitemRepository.saveAndFlush(bookitem);

        // Get all the bookitems
        restBookItemMockMvc
            .perform(
                get(
                    ENTITY_API_URL +
                    "?sort=id,desc&title=" +
                    bookitem.getBook().getTitle() +
                    "&isbn=" +
                    bookitem.getBook().getIsbn().toString() +
                    "&author=" +
                    bookitem.getBook().getAuthors().get(0).getName()
                )
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookitem.getId().toString())))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].referenceOnly").value(hasItem(DEFAULT_ISREFERENCEONLY)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)));
    }

    @Test
    @Transactional
    void getAllBookItemsByNotExistingTitle() throws Exception {
        // Initialize the database
        bookitemRepository.saveAndFlush(bookitem);

        // Get all the bookitems
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&title=notexisting"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*]").isEmpty());
    }

    @Test
    @Transactional
    void getBookItem() throws Exception {
        // Initialize the database
        bookitemRepository.saveAndFlush(bookitem);

        // Get the bookitem
        restBookItemMockMvc
            .perform(get(ENTITY_API_URL_ID, bookitem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookitem.getId().toString()))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.referenceOnly").value(DEFAULT_ISREFERENCEONLY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE));
    }

    @Test
    @Transactional
    void getNonExistingBookItem() throws Exception {
        // Get the bookitem
        restBookItemMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBookItem() throws Exception {
        // Initialize the database
        bookitemRepository.saveAndFlush(bookitem);

        int databaseSizeBeforeUpdate = bookitemRepository.findAll().size();

        // Update the bookitem
        BookItem updatedBookItem = bookitemRepository.findById(bookitem.getId()).get();
        // Disconnect from session so that the updates on updatedBookItem are not directly saved in db
        em.detach(updatedBookItem);
        updatedBookItem = createUpdatedEntity(updatedBookItem);

        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookItem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookItem))
            )
            .andExpect(status().isOk());

        // Validate the BookItem in the database
        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(databaseSizeBeforeUpdate);
        BookItem testBookItem = bookitemList.get(bookitemList.size() - 1);

        assertThat(testBookItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testBookItem.getReferenceOnly()).isEqualTo(UPDATED_ISREFERENCEONLY);
        assertThat(testBookItem.getBorrowed()).isEqualTo(UPDATED_BORROWED);
        assertThat(testBookItem.getDueDate()).isEqualTo(UPDATED_DUEDATE);
        assertThat(testBookItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testBookItem.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testBookItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBookItem.getDateOfPurchase()).isEqualTo(UPDATED_DATEOFPURCHASE);
        assertThat(testBookItem.getPublicationDate()).isEqualTo(UPDATED_PUBLICATIONDATE);
    }

    @Test
    @Transactional
    void putNonExistingBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookitemRepository.findAll().size();
        bookitem.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookitem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookitem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookitemRepository.findAll().size();
        bookitem.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookitem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookItem() throws Exception {
        int databaseSizeBeforeUpdate = bookitemRepository.findAll().size();
        bookitem.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookitem))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookItem in the database
        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookItem() throws Exception {
        // Initialize the database
        bookitemRepository.saveAndFlush(bookitem);

        int databaseSizeBeforeDelete = bookitemRepository.findAll().size();

        // Delete the bookitem
        restBookItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookitem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookItem> bookitemList = bookitemRepository.findAll();
        assertThat(bookitemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
