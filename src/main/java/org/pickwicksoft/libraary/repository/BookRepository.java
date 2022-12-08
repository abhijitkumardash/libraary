package org.pickwicksoft.libraary.repository;

import java.util.List;
import org.pickwicksoft.libraary.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(
        "select distinct b from Book b left join b.authors authors where COALESCE(upper(authors.name),'') like upper(concat('%', ?2, '%')) and upper(b.title) like upper(concat('%', ?1, '%'))"
    )
    Page<Book> findByTitleAndAuthor(String title, String name, Pageable pageable);
}
