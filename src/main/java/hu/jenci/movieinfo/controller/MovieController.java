package hu.jenci.movieinfo.controller;

import hu.jenci.movieinfo.service.MovieDetail;
import hu.jenci.movieinfo.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@CacheConfig(cacheNames = {"movies"})
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Operation(
            summary = "Search movies",
            description = "Search movies with apis.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful search",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation
                            = MovieResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error",
                    content = @Content)
    })
    @GetMapping("/{searchString}")
    public ResponseEntity<MovieResponse> getMovie(
            @PathVariable String searchString,
            @RequestParam(value = "api", required = false) String apiName) {
        if (apiName == null) {
            apiName = "omdb";
        }
        var movies = movieService.getMovies(searchString, apiName);

        if (movies.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new MovieResponse(movies));
    }
}
