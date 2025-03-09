package hu.jenci.movieinfo.service.movieclientprovider.tmdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreditsResponse {
    private int id;
    private List<Crew> crew;
    private List<Cast> cast;

    public List<Crew> getDirectors() {
        return this.crew.stream()
                .filter(c -> "Director".equals(c.getJob()))
                .toList();
    }

    @Data
    public static class Crew {
        private boolean adult;
        @JsonProperty("credit_id")
        private String creditId;
        private String department;
        private int gender;
        private int id;
        private String job;
        @JsonProperty("known_for_department")
        private String knownForDepartment;
        private String name;
        @JsonProperty("original_name")
        private String originalName;
        private double popularity;
        @JsonProperty("profile_path")
        private String profilePath;
    }

    @Data
    public static class Cast {
        private boolean adult;
        @JsonProperty("cast_id")
        private int castId;
        private String character;
        @JsonProperty("credit_id")
        private String creditId;
        private int gender;
        private int id;
        @JsonProperty("known_for_department")
        private String knownForDepartment;
        private String name;
        private int order;
        @JsonProperty("original_name")
        private String originalName;
        private double popularity;
        @JsonProperty("profile_path")
        private String profilePath;
    }
}
