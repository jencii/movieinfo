package hu.jenci.movieinfo.service.movieclientprovider;

import hu.jenci.movieinfo.config.OmdbApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OmdbApiClient implements MovieApiClient {
    private static final String OMDB_API_URL = "http://www.omdbapi.com/?s=";

    private final WebClient webClient;
    private final OmdbApiConfig omdbApiConfig;

    @Override
    public Flux<Map<String, Object>> fetchMovieData(String searchString) {
        String omdbUrl = "http://www.omdbapi.com/?s=" + searchString + "&apikey=" + omdbApiConfig.getApiKey();

        return webClient.get()
                .uri(omdbUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMapIterable(response -> (List<Map<String, String>>) response.get("Search"))
                .map(this::buildResult);
    }

    private Map<String, Object> buildResult(Map<String, String> movie) {
        Map<String, Object> result = new HashMap<>();
        result.put("source", "OMDB");
        result.put("Title", movie.get("Title"));
        result.put("Year", validYear(movie.get("Year")));
        result.put("Director", movie.get("Director"));
        return result;
    }
}
