package hu.jenci.movieinfo.service.movieclientprovider.omdb;

import hu.jenci.movieinfo.config.OmdbApiConfig;
import hu.jenci.movieinfo.service.MovieDetail;
import hu.jenci.movieinfo.service.movieclientprovider.MovieApiClient;
import hu.jenci.movieinfo.service.movieclientprovider.omdb.model.DetailsResponse;
import hu.jenci.movieinfo.service.movieclientprovider.omdb.model.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OmdbApiClient implements MovieApiClient {

    private static final String API_KEY_PARAM = "apikey";
    private static final String T_PARAM = "t";
    private static final String S_PARAM = "s";
    private static final String MOVIE_TYPE = "movie";

    private final WebClient webClient;
    private final OmdbApiConfig apiConfig;

    @Override
    public String getName() {
        return "omdb";
    }

    @Override
    public List<MovieDetail> fetchMovieData(String movieTitle) {
        return searchMovies(movieTitle)
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::buildMovieDetail)
                .collectList()
                .block();
    }

    private Mono<List<SearchResponse.MovieInfo>> searchMovies(String searchString) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .queryParam(S_PARAM, searchString)
                .queryParam(API_KEY_PARAM, apiConfig.getApiKey());

        return webClient.get()
                .uri(builder.toUriString())
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .map(this::collectResponse)
                .onErrorReturn(Collections.emptyList());
    }

    private Mono<MovieDetail> buildMovieDetail(SearchResponse.MovieInfo omdbMovie) {
        return getMovieDetailsByTitle(omdbMovie.getTitle())
                .map(movieDetailsByTitle -> {
                    String director = movieDetailsByTitle
                            .map(DetailsResponse::getDirector)
                            .orElse("");
                    return new MovieDetail(omdbMovie.getTitle(), omdbMovie.getYear(), director);
                });
    }

    private List<SearchResponse.MovieInfo> collectResponse(SearchResponse response) {
        return response != null && response.getSearch() != null
                ? response.getSearch().stream()
                .filter(movieInfo -> MOVIE_TYPE.equalsIgnoreCase(movieInfo.getType()))
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    // Get detail of a movie http://www.omdbapi.com/?t={Specific title of movie}&apikey=<<api key>>
    public Mono<Optional<DetailsResponse>> getMovieDetailsByTitle(String movieTitle) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .queryParam(T_PARAM, movieTitle)
                .queryParam(API_KEY_PARAM, apiConfig.getApiKey());

        String uri = builder.toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(DetailsResponse.class)
                .map(Optional::ofNullable)
                .onErrorReturn(Optional.empty());
    }

}
