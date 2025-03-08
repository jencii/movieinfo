package hu.jenci.movieinfo.service.movieclientprovider;

import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface MovieApiClient {
    default String validYear(String releaseDate) {
        if (releaseDate != null && releaseDate.length() >= 4 && releaseDate.substring(0, 4).matches("\\d{4}")) {
            return releaseDate.substring(0, 4);
        }
        return "N/A";
    }

    Flux<Map<String, Object>> fetchMovieData(String searchString);
}
