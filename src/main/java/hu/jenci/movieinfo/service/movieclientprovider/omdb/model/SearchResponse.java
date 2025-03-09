package hu.jenci.movieinfo.service.movieclientprovider.omdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    @JsonProperty("Search")
    private List<MovieInfo> search;
    @JsonProperty("totalResults")
    private String totalResults;
    @JsonProperty("Response")
    private String Response;

    @Data
    public static class MovieInfo {
        @JsonProperty("Title")
        private String title;
        @JsonProperty("Year")
        private String year;
        @JsonProperty("imdbID")
        private String imdbID;
        @JsonProperty("Type")
        private String type;
        @JsonProperty("Poster")
        private String poster;
    }
}