package hu.jenci.movieinfo.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class SearchEvent extends ApplicationEvent {

    private final String searchString;
    private final String apiName;

    public SearchEvent(Object source, String searchString, String apiName) {
        super(source);
        this.searchString = searchString;
        this.apiName = apiName;
    }
}
