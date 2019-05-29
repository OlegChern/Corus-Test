package linker.links;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "links")
public class LinkResponse {

    private static final int TTL = 600;

    @Id
    private final String shortLink;
    private final String link;

    // According to MongoDB documentation, it automatically converts any timestamps into UTC
    @JsonIgnore
    @Indexed(expireAfterSeconds = TTL)
    private final LocalDateTime timestamp;

    LinkResponse(String link, String shortLink, LocalDateTime timestamp) {
        this.link = link;
        this.shortLink = shortLink;
        this.timestamp = timestamp;
    }

    public String getShortLink() {
        return shortLink;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
