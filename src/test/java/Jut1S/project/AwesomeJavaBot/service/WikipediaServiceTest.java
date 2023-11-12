package Jut1S.project.AwesomeJavaBot.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test cases for the WikipediaService class, which interacts with the Wikipedia API.
 */
public class WikipediaServiceTest {

    /**
     * Test the search method with a valid search query.
     * This test checks if a valid search query returns a non-null and non-empty result.
     */
    @Test
    public void testSearchWithValidQuery() {
        String query = "Java"; // Replace with your query
        String result = WikipediaService.search(query);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    /**
     * Test the search method with an empty search query.
     * This test checks if an empty search query returns an error message.
     */
    @Test
    public void testSearchWithEmptyQuery() {
        String query = "";
        String result = WikipediaService.search(query);
        assertNotNull(result);
        assertEquals("An error occurred while processing the request.", result);
    }

    /**
     * Test the search method with a search query that is likely to have results.
     * This test checks if a query that is likely to have results returns a non-null and non-empty result.
     */
    @Test
    public void testSearchWithLikelyResult() {
        String query = "Einstein"; // Query likely to have results
        String result = WikipediaService.search(query);
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
}