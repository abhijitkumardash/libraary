package org.pickwicksoft.libraary.repository;

import org.pickwicksoft.libraary.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, String> {}
