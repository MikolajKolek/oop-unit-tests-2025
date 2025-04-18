import java.util.*;

/**
 * Represents a user in the news service system.
 * Stores user credentials, role, and associated tweets.
 */
public class User {
    private final String username;
    private String password;
    private String role;
    private final List<Tweet> tweets = new ArrayList<>();

    /**
     * Creates a new user with specified credentials
     * @param username unique user identifier
     * @param password user password
     * @param role user role/privilege level
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Adds a new tweet to user's tweet history
     * @param tweet the tweet to add
     */
    public void addTweet(Tweet tweet) {
        tweets.add(tweet);
    }

    // Accessor methods

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the user's role
     */
    public String getRole() {
        return role;
    }

    /**
     * @return an unmodifiable view of user's tweets
     */
    public List<Tweet> getTweets() {
        return Collections.unmodifiableList(tweets);
    }
}
