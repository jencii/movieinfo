package hu.jenci.movieinfo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "movieapi.omdb")
@Data
public class OmdbApiConfig {
    private String apiKey;
}
