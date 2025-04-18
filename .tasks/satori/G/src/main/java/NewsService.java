import java.util.*;
import java.util.function.Function;

/**
 * A simple news service (tweeter) implementation that handles:
 * - User registration and authentication
 * - Posting and retrieving tweets
 * - Role-based authorization
 */
public class NewsService {
    private final Map<String, User> users = new HashMap<>();
    private User loggedInUser;
    private final Function<String, Boolean> authorizationCheck = this::isAuthorized;

    /**
     * Returns the authorization check function
     */
    public Function<String, Boolean> getAuthorizationCheck() {
        return authorizationCheck;
    }

    /**
     * Internal class for admin-specific functionality
     */
    private static class AdminTools {
        // List of users with special access privileges
        private final List<User> specialAccessUsers = new ArrayList<>();

        /**
         * Grants special access to a user
         */
        private void grantSpecialAccess(User user) {
            specialAccessUsers.add(user);
        }
    }

    /**
     * Initializes the service with a default admin user
     */
    public NewsService() {
        User admin = new User("admin", "admin123", "admin");
        users.put("admin", admin);
    }

    /**
     * Registers a new user
     */
    public void register(String username, String password) {
        if (users.containsKey(username)) {
            return;
        }
        users.put(username, new User(username, password, "user"));
    }

    /**
     * Authenticates a user
     */
    public void login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user;
        }
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        loggedInUser = null;
    }

    /**
     * Posts a new tweet if user is authorized
     * @return true if tweet was posted
     */
    public boolean postTweet(String content) {
        if (loggedInUser == null || !authorizationCheck.apply(loggedInUser.getRole())) {
            return false;
        }
        loggedInUser.addTweet(new Tweet(content, new Date()));
        return true;
    }

    /**
     * Gets all tweets for current user
     * @return list of tweets or empty list if not logged in
     */
    public List<Tweet> getTweets() {
        if (loggedInUser == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(loggedInUser.getTweets());
    }

    /**
     * Checks if a role has authorization privileges
     */
    private boolean isAuthorized(String role) {
        return "admin".equals(role) || "moderator".equals(role) || "user".equals(role);
    }

    /**
     * Gets the currently logged in user
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
}
