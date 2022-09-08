package org.pickwicksoft.libraary.web.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.pickwicksoft.libraary.domain.Language;
import org.pickwicksoft.libraary.repository.LanguageRepository;
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
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(org.pickwicksoft.libraary.web.rest.BookResource.class);

    private static final String ENTITY_NAME = "Language";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanguageRepository languageRepository;

    public LanguageResource(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @GetMapping("/language")
    public List<Language> getLanguages() {
        log.debug("REST request to get all languages");
        return languageRepository.findAll();
    }

    @GetMapping("/language/{id}")
    public ResponseEntity<Language> getLanguage(@PathVariable Long id) {
        log.debug("REST request to get language : {}", id);
        var language = languageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(language);
    }

    @PostMapping("/language")
    public ResponseEntity<Language> createLanguage(@Valid @RequestBody Language language) throws URISyntaxException {
        log.debug("REST request to save Language : {}", language);
        if (language.getId() != null) {
            throw new BadRequestAlertException("A new language cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = languageRepository.save(language);
        return ResponseEntity
            .created(new URI("api/language" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/language/{id}")
    public ResponseEntity<Language> updateLanguage(@RequestBody Language language, @PathVariable Long id) {
        log.debug("REST request to update language : {}", language);
        if (language.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, language.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Language result = languageRepository.save(language);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, language.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/language/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        log.debug("REST request to delete language : {}", id);
        languageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
