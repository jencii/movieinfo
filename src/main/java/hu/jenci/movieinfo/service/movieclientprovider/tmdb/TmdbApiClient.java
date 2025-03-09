package hu.jenci.movieinfo.service.movieclientprovider.tmdb;

import hu.jenci.movieinfo.config.TmdbApiConfig;
import hu.jenci.movieinfo.service.MovieDetail;
import hu.jenci.movieinfo.service.movieclientprovider.MovieApiClient;
import hu.jenci.movieinfo.service.movieclientprovider.tmdb.model.CreditsResponse;
import hu.jenci.movieinfo.service.movieclientprovider.tmdb.model.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TmdbApiClient implements MovieApiClient {

    private static final String MOVIE_PATH = "movie";
    private static final String SEARCH_PATH = "search";
    private static final String CREDITS_PATH = "credits";
    private static final String API_KEY_PARAM = "api_key";
    private static final String QUERY_PARAM = "query";
    private static final String INCLUDE_ADULT_PARAM = "include_adult";
    private static final String DIRECTOR_JOB = "Director";
    private static final String YEAR_NA = "N/A";

    private final WebClient webClient;
    private final TmdbApiConfig apiConfig;

    @Override
    public String getName() {
        return "tmdb";
    }

    @Override
    public List<MovieDetail> fetchMovieData(String movieTitle) {
        SearchResponse searchResponse = searchMovies(movieTitle);
        return searchResponse.getResults().stream()
                .map(this::buildMovieDetail)
                .toList();
    }

    private SearchResponse searchMovies(String movieTitle) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .pathSegment(SEARCH_PATH, MOVIE_PATH)
                .queryParam(API_KEY_PARAM, apiConfig.getApiKey())
                .queryParam(QUERY_PARAM, movieTitle)
                .queryParam(INCLUDE_ADULT_PARAM, true);

        return webClient.get()
                .uri(builder.toUriString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .onErrorReturn(new SearchResponse())
                .block();
    }

    private MovieDetail buildMovieDetail(SearchResponse.MovieResult movieResult) {
        String releaseDate = movieResult.getReleaseDate();
        String year = itStartWithYear(releaseDate) ? releaseDate.substring(0, 4) : YEAR_NA;
        var credits = getMovieCredits(movieResult.getId());
        String director = credits.getCrew().stream()
                .filter(c -> DIRECTOR_JOB.equals(c.getJob()))
                .map(CreditsResponse.Crew::getName)
                .findFirst()
                .orElse(YEAR_NA);
        return new MovieDetail(movieResult.getTitle(), year, director);
    }

    private boolean itStartWithYear(String releaseDate) {
        return releaseDate != null && releaseDate.matches("^\\d{4}.*");
    }

    private SearchResponse.MovieResult getMovieDetail(int movieId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .pathSegment(MOVIE_PATH, String.valueOf(movieId))
                .queryParam(API_KEY_PARAM, apiConfig.getApiKey());

        return webClient.get()
                .uri(builder.toUriString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(SearchResponse.MovieResult.class)
                .onErrorReturn(new SearchResponse.MovieResult())
                .block();
    }

    public CreditsResponse getMovieCredits(int movieId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .pathSegment(MOVIE_PATH, String.valueOf(movieId), CREDITS_PATH)
                .queryParam(API_KEY_PARAM, apiConfig.getApiKey());

        return webClient.get()
                .uri(builder.toUriString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(CreditsResponse.class)
                .onErrorReturn(new CreditsResponse())
                .block();
    }
}