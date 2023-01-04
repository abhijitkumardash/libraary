package org.pickwicksoft.libraary.repository;

import java.util.UUID;
import org.pickwicksoft.libraary.domain.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookItemRepository
    extends PagingAndSortingRepository<BookItem, UUID>, JpaSpecificationExecutor<BookItem>, JpaRepository<BookItem, UUID> {}
