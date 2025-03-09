package hu.jenci.movieinfo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "movieapi.tmdb")
@Data
public class TmdbApiConfig {
    private String url;
    private String apiKey;
}
