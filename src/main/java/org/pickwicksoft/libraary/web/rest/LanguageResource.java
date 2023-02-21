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
}
