import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class PublicTest {

    @Test(expected = RestrictedAreaException.class)
    public void updateDensity_restrictedArea_shouldThrow() {
        int[][] grid = {{100}};
        boolean[][] restricted = {{true}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, restricted);

        analyzer.updateDensity(0, 0, 200);
    }

    @Test
    public void updateDensity_invalidValue_shouldThrow() {
        int[][] grid = {{100}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, new boolean[1][1]);

        try {
            analyzer.updateDensity(0, 0, -5);
            fail("Expected InvalidDensityException");
        } catch (InvalidDensityException e) {
            assertEquals("Density must be 0-10000. Got: -5", e.getMessage());
        }
    }

    @Test
    public void getHistogram_simpleArea_shouldReturnCorrectCounts() {
        int[][] grid = {{1, 2}, {1, 3}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, new boolean[2][2]);

        Map<Integer, Integer> histogram = analyzer.getHistogram(0, 0, 1, 1);

        assertEquals(3, histogram.size());
        assertEquals(2, (int) histogram.get(1));
        assertEquals(1, (int) histogram.get(2));
        assertEquals(1, (int) histogram.get(3));
    }

    @Test
    public void getHistogram_withRestrictedAreas_shouldIgnore() {
        int[][] grid = {{1, 2}, {3, 4}};
        boolean[][] restricted = {{false, true}, {false, false}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, restricted);

        Map<Integer, Integer> histogram = analyzer.getHistogram(0, 0, 1, 1);

        assertEquals(3, histogram.size());
        assertNull(histogram.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getHistogram_invalidCoordinates_shouldThrow() {
        int[][] grid = {{1}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, new boolean[1][1]);

        analyzer.getHistogram(0, 0, 1, 0);
    }

    @Test
    public void getMedian_oddCount_shouldReturnMiddleValue() {
        int[][] grid = {{1, 2, 3}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, new boolean[1][3]);

        double median = analyzer.getMedian(0, 0, 0, 2);
        assertEquals(2.0, median, 0.001);
    }

    @Test
    public void getMedian_evenCount_shouldReturnAverage() {
        int[][] grid = {{1, 2, 3, 4}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, new boolean[1][4]);

        double median = analyzer.getMedian(0, 0, 0, 3);
        assertEquals(2.5, median, 0.001);
    }

    @Test
    public void getMedian_withRestrictedAreas_shouldIgnore() {
        int[][] grid = {{1, 2}, {3, 4}};
        boolean[][] restricted = {{false, true}, {false, false}};
        PopulationDensityAnalyzer analyzer = new PopulationDensityAnalyzer(grid, restricted);

        double median = analyzer.getMedian(0, 0, 1, 1);
        assertEquals(3, median, 0.001);
    }
}