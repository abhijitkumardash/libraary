package org.pickwicksoft.libraary.web.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.pickwicksoft.libraary.domain.Category;
import org.pickwicksoft.libraary.domain.SubCategory;
import org.pickwicksoft.libraary.repository.CategoryRepository;
import org.pickwicksoft.libraary.repository.SubCategoryRepository;
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
@RequestMapping("/api/book")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(org.pickwicksoft.libraary.web.rest.BookResource.class);

    private static final String ENTITY_NAME = "Category";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public CategoryResource(CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = categoryRepository.save(category);
        return ResponseEntity
            .created(new URI("api/book/category" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/subcategory")
    public ResponseEntity<SubCategory> createSubcategory(@Valid @RequestBody SubCategory category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new Subcategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = subCategoryRepository.save(category);
        return ResponseEntity
            .created(new URI("api/book/subcategory" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/category")
    public List<Category> getCategories() {
        log.debug("REST request to get all categories");
        return categoryRepository.findAll();
    }

    @GetMapping("/subcategory")
    public List<SubCategory> getSubcategories() {
        log.debug("REST request to get all Subcategories");
        return subCategoryRepository.findAll();
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        var category = categoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(category);
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<SubCategory> getSubcategory(@PathVariable Long id) {
        log.debug("REST request to get Subcategory : {}", id);
        var category = subCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(category);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<Category> updateCategory(
        @RequestBody @Valid Category category,
        @PathVariable(value = "id", required = false) final Long id
    ) {
        log.debug("REST request to update category : {}", category);
        if (category.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, category.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Category result = categoryRepository.save(category);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, category.getId().toString()))
            .body(result);
    }

    @PutMapping("/subcategory/{id}")
    public ResponseEntity<SubCategory> updateSubcategory(
        @RequestBody @Valid SubCategory category,
        @PathVariable(value = "id", required = false) final Long id
    ) {
        log.debug("REST request to update subcategory : {}", category);
        if (category.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, category.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubCategory result = subCategoryRepository.save(category);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, category.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete category : {}", id);
        categoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @DeleteMapping("/subcategory/{id}")
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        log.debug("REST request to delete subcategory : {}", id);
        subCategoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
