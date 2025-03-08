package hu.jenci.movieinfo.controller;

import hu.jenci.movieinfo.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@WebFluxTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieService movieService;

    @Test
    public void testGetMovies() {
        String searchPhrase = "Inception";
        List<Map<String, Object>> mockMovies = Collections.singletonList(Collections.singletonMap("Title", (Object)
                "Inception"));

        Mockito.when(movieService.getFastestMovieResponse(searchPhrase))
                .thenReturn(Mono.just(org.springframework.http.ResponseEntity.ok(mockMovies)));

        webTestClient.get()
                .uri("/movies/{searchPhrase}", searchPhrase)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Map.class)
                .isEqualTo((List)mockMovies);
        ;
    }
}