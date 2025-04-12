import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class DominikKorsaGraphViewTest {
    @Test
    public void map_and_list_size_is_updated_when_size_changes() {
        GraphView graph = new GraphView();

        var map = graph.asAdjacencyMap();
        var list = graph.asAdjacencyMatrix();

        assertEquals(0, graph.size());
        assertEquals(map.size(), graph.size());
        assertEquals(list.size(), graph.size());

        graph.addVertex();
        graph.addVertex();

        assertEquals(2, graph.size());
        assertEquals(map.size(), graph.size());
        assertTrue(map.containsKey(0));
        assertTrue(map.containsKey(1));
        assertFalse(map.containsKey(2));
        assertEquals(list.size(), graph.size());
        assertEquals(((List) list.get(0)).size(), graph.size());

        graph.removeVertex();

        assertEquals(1, graph.size());
        assertEquals(map.size(), graph.size());
        assertTrue(map.containsKey(0));
        assertFalse(map.containsKey(1));
        assertFalse(map.containsKey(2));
        assertEquals(list.size(), graph.size());
        assertEquals(((List) list.get(0)).size(), graph.size());
    }

    @Test
    public void map_get_throws_correct_exceptions() {
        GraphView graph = new GraphView();
        var map = graph.asAdjacencyMap();

        graph.addVertex();

        assertThrows(NullPointerException.class, () -> map.get(null));
        //noinspection SuspiciousMethodCalls
        assertThrows(ClassCastException.class, () -> map.get("foo"));
        assertNull(map.get(-5));
        assertEquals("[]", map.get(0).toString());
    }
}
