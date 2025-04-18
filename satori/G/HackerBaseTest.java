import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Base test class for Hacker implementation.
 * Students should extend this class and implement their own tests.
 */
public class HackerBaseTest {
    protected NewsService newsService;
    protected Hacker hacker;

    /**
     * Creates fresh NewsService instance before each test
     */
    @Before
    public void setUp() {
        newsService = new NewsService();
        hacker = new Hacker(newsService);
        newsService.register("testUser", "testPass123");
    }

    // ========= BASIC FUNCTIONALITY TESTS =========

    @Test
    public void analyzeClassesShouldReturnFormattedOutput() {
        String result = hacker.analyzeClasses();

        assertNotNull("Output should not be null", result);
        assertTrue("Output should not be empty", result.length() > 0);

        // Verify contains expected headers
        assertTrue(result.contains("NewsService Class Analysis"));
        assertTrue(result.contains("User Class Analysis"));
        assertTrue(result.contains("Tweet Class Analysis"));

        // Verify contains sections
        assertTrue(result.contains("Fields:"));
        assertTrue(result.contains("Methods:"));
        assertTrue(result.contains("Inner Classes:"));
    }

    @Test
    public void shouldGetUsersMap() throws Exception {
        Map<String, User> users = hacker.getUsersMap();
        assertNotNull("Users map should not be null", users);
        assertTrue("Should contain at least initial user", users.size() >= 1);
    }

    // ========= USER MANIPULATION TESTS =========

    @Test
    public void shouldCreateUserWithoutConstructor() throws Exception {
        User user = hacker.createUserWithoutConstructor("ghost", "secret", "admin");
        assertNotNull("Created user should not be null", user);
        assertEquals("Username should match", "ghost", user.getUsername());
    }

    @Test
    public void shouldChangeUserRole() throws Exception {
        User user = new User("user1", "pass", "user");
        hacker.changeUserRole(user, "admin");
        assertEquals("Role should be changed", "admin", user.getRole());
    }

    // ========= SECURITY TESTS =========

    @Test
    public void shouldModifyTweets() throws Exception {
        newsService.login("testUser", "testPass123");
        newsService.postTweet("Original content");
        hacker.modifyTweetsList();
        List<Tweet> afterTweets = newsService.getTweets();
        assertEquals("Tweet count should be 1", 1, afterTweets.size());
        assertTrue("Tweet content should start with [HACKED]", afterTweets.get(0).content().startsWith("[HACKED]"));
        assertTrue("Tweet content should contain original content", afterTweets.get(0).content().contains("Original content"));
    }

    @Test
    public void shouldCreateBackdoorAdmin() throws Exception {
        hacker.createBackdoorAdmin();
        Map<String, User> users = hacker.getUsersMap();
        assertTrue("Backdoor admin should exist in users map", users.containsKey("backdoor_admin"));
    }

    @Test
    public void analyzeClassesShouldReturnCorrectStructure() {
        String output = hacker.analyzeClasses();

        // Check the section headings
        assertTrue(output.contains("=== NewsService Class Analysis ==="));
        assertTrue(output.contains("=== User Class Analysis ==="));
        assertTrue(output.contains("=== Tweet Class Analysis ==="));

        // Check out the sections for each class
        verifyClassSection(output, "NewsService",
                Set.of(
                        "- Map users (modifiers: private final)",
                        "- User loggedInUser (modifiers: private)",
                        "- Function authorizationCheck (modifiers: private final)"
                ),
                Set.of(
                        "- void register(String, String) (modifiers: public)",
                        "- boolean isAuthorized(String) (modifiers: private)",
                        "- List getTweets() (modifiers: public)",
                        "- Function getAuthorizationCheck() (modifiers: public)",
                        "- void login(String, String) (modifiers: public)",
                        "- void logout() (modifiers: public)",
                        "- boolean postTweet(String) (modifiers: public)",
                        "- User getLoggedInUser() (modifiers: public)"
                ),
                Set.of("- AdminTools (modifiers: private static)")
        );

        verifyClassSection(output, "User",
                Set.of(
                        "- String username (modifiers: private final)",
                        "- String password (modifiers: private)",
                        "- String role (modifiers: private)",
                        "- List tweets (modifiers: private final)"
                ),
                Set.of(
                        "- String getPassword() (modifiers: public)",
                        "- void addTweet(Tweet) (modifiers: public)",
                        "- List getTweets() (modifiers: public)",
                        "- String getUsername() (modifiers: public)",
                        "- String getRole() (modifiers: public)"
                ),
                Collections.emptySet()
        );

        verifyClassSection(output, "Tweet",
                Set.of(
                        "- String content (modifiers: private final)",
                        "- Date timestamp (modifiers: private final)"
                ),
                Set.of(
                        "- boolean equals(Object) (modifiers: public final)",
                        "- String toString() (modifiers: public)",
                        "- int hashCode() (modifiers: public final)",
                        "- Date timestamp() (modifiers: public)",
                        "- String content() (modifiers: public)"
                ),
                Collections.emptySet()
        );
    }

    private void verifyClassSection(String output, String className,
                                    Set<String> expectedFields,
                                    Set<String> expectedMethods,
                                    Set<String> expectedInnerClasses) {
        String section = extractSection(output, "=== " + className + " Class Analysis ===");

        Set<String> actualFields = extractItems(section, "Fields:");
        assertEquals("Fields mismatch for " + className, expectedFields, actualFields);

        Set<String> actualMethods = extractItems(section, "Methods:");
        assertEquals("Methods mismatch for " + className, expectedMethods, actualMethods);

        if (!expectedInnerClasses.isEmpty()) {
            Set<String> actualInnerClasses = extractItems(section, "Inner Classes:");
            assertEquals("Inner classes mismatch for " + className, expectedInnerClasses, actualInnerClasses);
        }
    }

    private String extractSection(String output, String header) {
        int start = output.indexOf(header);
        if (start == -1) {
            fail("Section header not found: " + header);
        }

        int nextSection = output.indexOf("===", start + header.length());
        if (nextSection == -1) {
            return output.substring(start);
        }
        return output.substring(start, nextSection);
    }

    private Set<String> extractItems(String section, String header) {
        int start = section.indexOf(header);
        if (start == -1) {
            return Collections.emptySet();
        }

        int end = section.indexOf("\n\n", start);
        if (end == -1) {
            end = section.length();
        }

        String itemsSection = section.substring(start, end);
        return Arrays.stream(itemsSection.split("\n"))
                .filter(line -> line.startsWith("- "))
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
