/**
 * PO 2024/25, Problem G - Hacker
 *
 * @author YOUR NAME
 */

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Hacker {
    Hacker(NewsService newsService) {}
    
    /**
     * Analyzes and returns class structure information as String
     * @return formatted class analysis
     */
    public String analyzeClasses() {
        return null;
    }

    /**
     * Generates separate strings with information about fields, methods and inner classes
     * * @param clazz class to analyze
     * * @return map with keys: "fields", "methods", "innerClasses" and their corresponding descriptions
     */
    public Map<String, String> classInfo(Class<?> clazz) {
        return null;
    }

    /**
     * Gets the registered users map through reflection
     * @return map of username to User objects
     */
    @SuppressWarnings("unchecked")
    public Map<String, User> getUsersMap() throws Exception {
        return null;
    }

    /**
     * Gets the currently logged in user through reflection
     */
    public User getLoggedInUser() throws Exception {
        return null;
    }

    /**
     * Creates a user without invoking the constructor
     * @return newly created user
     */
    public User createUserWithoutConstructor(String username, String password, String role) throws Exception {
        return null;
    }

    /**
     * Changes a user's role through reflection
     */
    public void changeUserRole(User user, String newRole) throws Exception {}

    /**
     * Impersonates another user by modifying loggedInUser
     */
    public void impersonateUser(String username) throws Exception {}

    /**
     * Modifies all tweets of logged in user
     */
    public void modifyTweetsList() throws Exception {}

    /**
     * Creates a backdoor admin account
     */
    public void createBackdoorAdmin() throws Exception {}

    /**
     * Overrides authorization
     */
    public void overrideAuthorizationCheck() throws Exception {}

    // Helper methods

    /**
     * Adds a user to the special access list through AdminTools
     */
    public void addUserViaAdminTools(User user) throws Exception {}
}
