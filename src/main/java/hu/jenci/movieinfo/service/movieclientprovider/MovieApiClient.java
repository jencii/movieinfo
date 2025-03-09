package hu.jenci.movieinfo.service.movieclientprovider;

import hu.jenci.movieinfo.service.MovieDetail;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface MovieApiClient {
    String getName();
    List<MovieDetail> fetchMovieData(String searchString);
}
