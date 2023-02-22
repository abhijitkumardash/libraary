package org.pickwicksoft.libraary.repository;

import java.util.Optional;
import org.pickwicksoft.libraary.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends PagingAndSortingRepository<Book, Long>, JpaSpecificationExecutor<Book>, JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(Long isbn);
}
