package org.pickwicksoft.libraary.repository;

import org.pickwicksoft.libraary.domain.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookItemRepository extends JpaRepository<BookItem, Long> {}
