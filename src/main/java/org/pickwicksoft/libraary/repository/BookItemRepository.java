package org.pickwicksoft.libraary.repository;

import org.pickwicksoft.libraary.domain.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookItemRepository
    extends PagingAndSortingRepository<BookItem, Long>, JpaSpecificationExecutor<BookItem>, JpaRepository<BookItem, Long> {}
