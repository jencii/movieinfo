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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OmdbApiClient implements MovieApiClient {

    private final WebClient webClient;
    private final OmdbApiConfig apiConfig;

    @Override
    public String getName() {
        return "omdb";
    }

    @Override
    public List<MovieDetail> fetchMovieData(String movieTitle) {
        List<SearchResponse.MovieInfo> movieInfos = searchMovies(movieTitle);
        return movieInfos.stream()
                .map(this::buildMovieDetail)
                .toList();
    }

    private List<SearchResponse.MovieInfo> searchMovies(String searchString) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .queryParam("s", searchString)
                .queryParam("apikey", apiConfig.getApiKey());

        return webClient.get()
                .uri(builder.toUriString())
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .map(this::collectResponse)
                .onErrorReturn(Collections.emptyList())
                .block();
    }

    private MovieDetail buildMovieDetail(SearchResponse.MovieInfo omdbMovie) {
        Optional<DetailsResponse> movieDetailsByTitle = getMovieDetailsByTitle(omdbMovie.getTitle());
        String director = movieDetailsByTitle
                .map(DetailsResponse::getDirector)
                .orElse("");
        return new MovieDetail(omdbMovie.getTitle(), omdbMovie.getYear(), director);
    }

    private List<SearchResponse.MovieInfo> collectResponse(SearchResponse response) {
        return response != null && response.getSearch() != null
                ? response.getSearch().stream()
                .filter(movieInfo -> "movie".equalsIgnoreCase(movieInfo.getType()))
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    // Get detail of a movie http://www.omdbapi.com/?t={Specific title of movie}&apikey=<<api key>>
    public Optional<DetailsResponse> getMovieDetailsByTitle(String movieTitle) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConfig.getUrl())
                .queryParam("t", movieTitle)
                .queryParam("apikey", apiConfig.getApiKey());

        String uri = builder.toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(DetailsResponse.class)
                .map(Optional::ofNullable)
                .onErrorReturn(Optional.empty())
                .block();
    }

}
