package org.pickwicksoft.libraary.repository;

import java.util.List;
import java.util.UUID;
import org.pickwicksoft.libraary.domain.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookItemRepository extends JpaRepository<BookItem, UUID> {
    @Query(
        "select b from BookItem b where upper(b.book.title) like upper(concat('%', ?1, '%')) and upper(b.book.author) like upper(concat('%', ?2, '%'))"
    )
    List<BookItem> findByBook_TitleIgnoreCaseAndBook_AuthorIgnoreCase(String book_title, String book_author);
}
