package org.pickwicksoft.libraary.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private static final String DEFAULT_NAME = "German";
    private static final String DEFAULT_FLAG = "\uD83C\uDDE9\uD83C\uDDEA";

    private static final String ENTITY_API_URL = "/api/language";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MockMvc restLanguageMockMvc;

    @Test
    @Transactional
    void getAllLanguages() throws Exception {
        // Get all the languageList
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem("de")))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].flag").value(hasItem(DEFAULT_FLAG)));
    }

    @Test
    @Transactional
    void getLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc
            .perform(get(ENTITY_API_URL_ID, "de"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value("de"))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.flag").value(DEFAULT_FLAG));
    }

    @Test
    @Transactional
    void getNonExistingLanguage() throws Exception {
        // Get the language
        restLanguageMockMvc.perform(get(ENTITY_API_URL_ID, "123")).andExpect(status().isNotFound());
    }
}
