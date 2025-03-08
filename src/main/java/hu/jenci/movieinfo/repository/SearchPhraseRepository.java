package hu.jenci.movieinfo.repository;

import hu.jenci.movieinfo.entity.SearchPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchPhraseRepository extends JpaRepository<SearchPhrase, Long> {
}
