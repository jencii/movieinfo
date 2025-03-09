package hu.jenci.movieinfo.service;

import hu.jenci.movieinfo.repository.SearchPhraseRepository;
import hu.jenci.movieinfo.service.movieclientprovider.MovieApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

class MovieServiceTest {

    @Mock
    private SearchPhraseRepository searchPhraseRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private MovieApiClient omdbApiClient;

    @Mock
    private MovieApiClient tmdbApiClient;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(applicationContext.getBeansOfType(MovieApiClient.class)).thenReturn(Collections.singletonMap("omdbApiClient", omdbApiClient));
    }

    @Test
    public void testGetMovies() {
        String searchPhrase = "Inception";
        List<Map<String, Object>> mockMovies = Collections.singletonList(Collections.singletonMap("Title", (Object) "Inception"));

//        when(omdbApiClient.fetchMovieData(searchPhrase)).thenReturn(Flux.fromIterable(mockMovies));
//
//        Mono<ResponseEntity<List<Map<String, Object>>>> result = movieService.getMovies(searchPhrase);

//        StepVerifier.create(result)
//                .expectNextMatches(response -> response.getBody().equals(mockMovies))
//                .verifyComplete();
    }

}