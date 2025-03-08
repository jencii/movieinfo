package hu.jenci.movieinfo.event;

import hu.jenci.movieinfo.entity.SearchPhrase;
import hu.jenci.movieinfo.repository.SearchPhraseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchEventHandler {

    private final SearchPhraseRepository searchPhraseRepository;

    @Async
    @EventListener
    public void onSearchEvent(SearchEvent event) {
        SearchPhrase searchPhrase = new SearchPhrase();
        searchPhrase.setSearchString(event.getSearchString());
        searchPhraseRepository.save(searchPhrase); // counter?
    }
}
