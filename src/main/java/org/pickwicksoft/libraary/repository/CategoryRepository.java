package org.pickwicksoft.libraary.repository;

import org.pickwicksoft.libraary.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
