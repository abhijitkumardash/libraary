package org.pickwicksoft.libraary.service;

import java.util.List;
import org.pickwicksoft.libraary.domain.Author;
import org.pickwicksoft.libraary.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> saveAuthors(List<Author> authors) {
        return authorRepository.saveAll(authors);
    }
}
