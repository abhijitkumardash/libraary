package org.pickwicksoft.libraary.repository;

import java.util.List;
import org.pickwicksoft.libraary.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findAllByCategoryId(Long id);
}
