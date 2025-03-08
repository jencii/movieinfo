package hu.jenci.movieinfo.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class SearchEvent extends ApplicationEvent {

    private String searchString;

    public SearchEvent(Object source, String searchString) {
        super(source);
        this.searchString = searchString;
    }
}
