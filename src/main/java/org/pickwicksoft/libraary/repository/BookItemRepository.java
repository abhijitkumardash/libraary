package org.pickwicksoft.libraary.repository;

import java.util.UUID;
import org.pickwicksoft.libraary.domain.BookItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookItemRepository extends JpaRepository<BookItem, UUID> {
    @Query(
        "select distinct b from BookItem b left join b.book.authors authors where (upper(authors.name) like upper(concat('%', ?2, '%')) or authors is NULL ) and upper(b.book.title) like upper(concat('%', ?1, '%'))"
    )
    Page<BookItem> findAllByTitleAndAuthor(String title, String name, Pageable pageable);
}
