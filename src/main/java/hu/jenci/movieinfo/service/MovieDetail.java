package hu.jenci.movieinfo.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MovieDetail implements Serializable {

    private String title;
    private String year;
    private String director;
}
