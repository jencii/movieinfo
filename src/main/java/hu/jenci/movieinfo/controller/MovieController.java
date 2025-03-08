package hu.jenci.movieinfo.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@CacheConfig(cacheNames = {"movies"})
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Operation(
            summary = "Filmek keresése",
            description = "Ez a végpont lehetővé teszi, hogy filmeket keressen.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sikeres keresés",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation
                            = Map.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Hiba",
                    content = @Content)
    })
    @GetMapping("/{searchString}")
    public Mono<ResponseEntity<List<Map<String, Object>>>> getMovie(@PathVariable String searchString) {
        return movieService.getFastestMovieResponse(searchString);
    }
}
