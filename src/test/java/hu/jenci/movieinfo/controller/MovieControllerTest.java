package hu.jenci.movieinfo.controller;

import hu.jenci.movieinfo.service.MovieDetail;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieService movieService;

    @Test
    void getMovies_ShouldReturnMovieList_WhenMovieTitleExists() throws Exception {
        String movieTitle = "MyTest";
        var expectedServiceResponse = List.of(
                new MovieDetail("MyTest", "2025", "ItsMe"),
                new MovieDetail("MyTest2", "2025", "ItsMe")
        );

        when(movieService.getMovies(movieTitle, "omdb")).thenReturn(expectedServiceResponse);

        webTestClient.get()
                .uri("/movies/{movieTitle}", movieTitle)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)

                .expectBody()
                .jsonPath("$.movies.length()").isEqualTo(2)
                .jsonPath("$.movies[0].title").isEqualTo("MyTest")
                .jsonPath("$.movies[0].year").isEqualTo("2025")
                .jsonPath("$.movies[0].director").isEqualTo("ItsMe")
                ;
    }

    @Test
    void getMovies_ShouldReturnNotFound_WhenMovieTitleDoesNotExist() throws Exception {
        String movieTitle = "NonExistingMovieTitle";

        when(movieService.getMovies(movieTitle, "omdb")).thenReturn(List.of());

        webTestClient.get()
                .uri("/movies/{movieTitle}", movieTitle)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getMovies_ShouldReturnInternalServerError_WhenServiceThrowsException() throws Exception {
        String movieTitle = "Room 0";

        when(movieService.getMovies(movieTitle, "omdb")).thenThrow(new RuntimeException("MyError"));

        webTestClient.get()
                .uri("/movies/{movieTitle}", movieTitle)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}