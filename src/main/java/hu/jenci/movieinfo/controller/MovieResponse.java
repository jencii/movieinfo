package hu.jenci.movieinfo.controller;

import hu.jenci.movieinfo.service.MovieDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieResponse {
    private List<MovieDetail> movies;
}
