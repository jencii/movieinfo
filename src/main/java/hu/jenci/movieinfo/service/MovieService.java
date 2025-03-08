package hu.jenci.movieinfo.service;

import hu.jenci.movieinfo.event.SearchEvent;
import hu.jenci.movieinfo.service.movieclientprovider.MovieApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ApplicationContext applicationContext;

    @Cacheable(value = "movies")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getFastestMovieResponse(String searchPhrase) {
        applicationContext.publishEvent(new SearchEvent(this, searchPhrase));

        Map<String, MovieApiClient> apiClients = applicationContext.getBeansOfType(MovieApiClient.class);

        List<Flux<Map<String, Object>>> fluxes = new ArrayList<>();
        for (MovieApiClient apiClient : apiClients.values()) {
            fluxes.add(apiClient.fetchMovieData(searchPhrase).subscribeOn(Schedulers.parallel()));
        }

        return Flux.merge(fluxes)
//                .next()
//                .flatMap(firstResult -> Mono.just(ResponseEntity.ok(List.of(firstResult))));
                .collectList()
                .map(ResponseEntity::ok);
    }
}
