package hu.jenci.movieinfo.service.movieclientprovider.tmdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private int page;
    private List<MovieResult> results;
    @JsonProperty("total_pages")
    private int totalPages;
    @JsonProperty("total_results")
    private int totalResults;

    @Data
    public static class MovieResult {
        private boolean adult;
        @JsonProperty("backdrop_path")
        private String backdropPath;
        @JsonProperty("genre_ids")
        private List<Integer> genreIds;
        private int id;
        @JsonProperty("original_language")
        private String originalLanguage;
        @JsonProperty("original_title")
        private String originalTitle;
        private String overview;
        private double popularity;
        @JsonProperty("poster_path")
        private String posterPath;
        @JsonProperty("release_date")
        private String releaseDate;
        private String title;
        private boolean video;
        @JsonProperty("vote_average")
        private double voteAverage;
        @JsonProperty("vote_count")
        private int voteCount;
    }
}
