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
import org.pickwicksoft.libraary.domain.SubCategory;
import org.pickwicksoft.libraary.repository.SubCategoryRepository;
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
class SubCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATED_NAME = "BBB";

    private static final String ENTITY_API_URL = "/api/book/subcategory";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubCategoryRepository subcategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoryMockMvc;

    private SubCategory subcategory;

    public static SubCategory createEntity() {
        SubCategory subcategory = new SubCategory();

        subcategory.setName(DEFAULT_NAME);

        return subcategory;
    }

    public static SubCategory createUpdatedEntity() {
        SubCategory subcategory = new SubCategory();

        subcategory.setName(UPDATED_NAME);

        return subcategory;
    }

    public static SubCategory createUpdatedEntity(SubCategory subcategory) {
        subcategory.setName(UPDATED_NAME);

        return subcategory;
    }

    @BeforeEach
    public void initTest() {
        subcategory = createEntity();
    }

    @Test
    @Transactional
    void createSubCategory() throws Exception {
        int dbSizeBeforeCreate = subcategoryRepository.findAll().size();
        restSubCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategory))
            )
            .andExpect(status().isCreated());

        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(dbSizeBeforeCreate + 1);
        SubCategory testSubCategory = subcategoryList.get(subcategoryList.size() - 1);

        assertThat(testSubCategory.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSubCategoryWithExistingId() throws Exception {
        subcategory.setId(1L);
        int dbSizeBeforeCreate = subcategoryRepository.findAll().size();
        restSubCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategory))
            )
            .andExpect(status().isBadRequest());

        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(dbSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubCategorys() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategoryList
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subcategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSubCategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get the subcategory
        restSubCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, subcategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subcategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSubCategory() throws Exception {
        // Get the subcategory
        restSubCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubCategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();

        // Update the subcategory
        SubCategory updatedSubCategory = subcategoryRepository.findById(subcategory.getId()).get();
        // Disconnect from session so that the updates on updatedSubCategory are not directly saved in db
        em.detach(updatedSubCategory);
        updatedSubCategory = createUpdatedEntity(updatedSubCategory);

        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubCategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubCategory))
            )
            .andExpect(status().isOk());

        // Validate the SubCategory in the database
        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeUpdate);
        SubCategory testSubCategory = subcategoryList.get(subcategoryList.size() - 1);

        assertThat(testSubCategory.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();
        subcategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subcategory.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();
        subcategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();
        subcategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subcategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategory in the database
        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubCategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        int databaseSizeBeforeDelete = subcategoryRepository.findAll().size();

        // Delete the subcategory
        restSubCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, subcategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCategory> subcategoryList = subcategoryRepository.findAll();
        assertThat(subcategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
