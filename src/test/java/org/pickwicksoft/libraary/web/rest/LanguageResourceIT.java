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
import org.pickwicksoft.libraary.domain.Language;
import org.pickwicksoft.libraary.repository.LanguageRepository;
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
class LanguageResourceIT {

    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATED_NAME = "BBB";

    private static final String ENTITY_API_URL = "/api/language";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguageMockMvc;

    private Language language;

    public static Language createEntity() {
        Language language = new Language();

        language.setName(DEFAULT_NAME);

        return language;
    }

    public static Language createUpdatedEntity() {
        Language language = new Language();

        language.setName(UPDATED_NAME);

        return language;
    }

    public static Language createUpdatedEntity(Language language) {
        language.setName(UPDATED_NAME);

        return language;
    }

    @BeforeEach
    public void initTest() {
        language = createEntity();
    }

    @Test
    @Transactional
    void createLanguage() throws Exception {
        int dbSizeBeforeCreate = languageRepository.findAll().size();
        restLanguageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(language))
            )
            .andExpect(status().isCreated());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(dbSizeBeforeCreate + 1);
        Language testLanguage = languageList.get(languageList.size() - 1);

        assertThat(testLanguage.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createLanguageWithExistingId() throws Exception {
        language.setId(1L);
        int dbSizeBeforeCreate = languageRepository.findAll().size();
        restLanguageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(language))
            )
            .andExpect(status().isBadRequest());

        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(dbSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get all the languageList
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(language.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        // Get the language
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, language.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(language.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeUpdate = languageRepository.findAll().size();

        // Update the language
        Language updatedLanguage = languageRepository.findById(language.getId()).get();
        // Disconnect from session so that the updates on updatedLanguage are not directly saved in db
        em.detach(updatedLanguage);
        updatedLanguage = createUpdatedEntity(updatedLanguage);

        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLanguage.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLanguage))
            )
            .andExpect(status().isOk());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
        Language testLanguage = languageList.get(languageList.size() - 1);

        assertThat(testLanguage.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, language.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(language))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(language))
            )
            .andExpect(status().isBadRequest());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguage() throws Exception {
        int databaseSizeBeforeUpdate = languageRepository.findAll().size();
        language.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(language))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Language in the database
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanguage() throws Exception {
        // Initialize the database
        languageRepository.saveAndFlush(language);

        int databaseSizeBeforeDelete = languageRepository.findAll().size();

        // Delete the language
        restLanguageMockMvc
            .perform(delete(ENTITY_API_URL_ID, language.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Language> languageList = languageRepository.findAll();
        assertThat(languageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
