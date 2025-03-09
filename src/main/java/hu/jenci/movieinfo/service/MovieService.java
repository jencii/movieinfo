package hu.jenci.movieinfo.service;

import hu.jenci.movieinfo.event.SearchEvent;
import hu.jenci.movieinfo.service.movieclientprovider.MovieApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ApplicationContext applicationContext;

    @Cacheable(value = "movies")
    public List<MovieDetail> getMovies(String searchPhrase,
                                       String apiName) {
        applicationContext.publishEvent(new SearchEvent(this, searchPhrase, apiName));

        MovieApiClient apiClient = applicationContext.getBeansOfType(MovieApiClient.class).values().stream()
                .filter(c -> apiName.toLowerCase().equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such API client: " + apiName));

        return apiClient.fetchMovieData(searchPhrase);
    }
}
