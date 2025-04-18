import java.util.Date;

/**
 * Represents a single tweet/post in the news service system.
 * Contains the tweet content and creation timestamp.
 *
 * @param content the text content of the tweet
 * @param timestamp the date and time when the tweet was created
 */
public record Tweet(String content, Date timestamp) {
    /**
     * Provides a formatted string representation of the tweet.
     * @return string in format: "Tweet{content='...', timestamp=...}"
     */
    @Override
    public String toString() {
        return "Tweet{" +
                "content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
