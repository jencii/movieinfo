package hu.jenci.movieinfo.service.movieclientprovider;

import hu.jenci.movieinfo.config.TmdbApiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TmdbApiClient implements MovieApiClient {

    private final WebClient webClient;
    private final TmdbApiConfig tmdbApiConfig;

    @Override
    public Flux<Map<String, Object>> fetchMovieData(String movieTitle) {
        String tmdbUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + tmdbApiConfig.getApiKey()
                + "&query=" + movieTitle + "&include_adult=true";
        return webClient.get()
                .uri(tmdbUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMapIterable(response -> (List<Map<String, Object>>) response.get("results"))
                .map(this::buildResult);
    }

    private Map<String, Object> buildResult(Map<String, Object> movie) {
        Map<String, Object> result = new HashMap<>();
        result.put("source", "TMDB");
        result.put("Title", movie.get("title"));
        result.put("Year", validYear((String) movie.get("release_date")));
        return result;
    }

}
