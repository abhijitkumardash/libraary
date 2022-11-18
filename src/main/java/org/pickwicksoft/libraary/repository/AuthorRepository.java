package org.pickwicksoft.libraary.repository;

import java.util.List;
import org.pickwicksoft.libraary.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAuthorsByBooksId(Long books_id);
}
