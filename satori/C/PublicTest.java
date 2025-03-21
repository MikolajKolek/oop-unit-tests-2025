import org.junit.Test;

import static org.junit.Assert.*;

import java.util.*;

public class PublicTest {

    @Test
    public void testGraph() {
        GraphView graph = new GraphView();
        assertEquals(Object.class, graph.getClass().getSuperclass());

        graph.addVertex();
        graph.addEdge(0, 0);
        assertEquals(1, graph.size());
        graph.removeEdge(0, 0);
        assertEquals(1, graph.size());
        graph.removeVertex();
        assertEquals(0, graph.size());
    }

    @Test
    public void testMatrixView() {
        GraphView graph = new GraphView();

        List matrix = graph.asAdjacencyMatrix();
        assertEquals("[]", matrix.toString());
        assertEquals(List.of(), matrix);

        graph.addVertex();
        assertEquals("[[false]]", matrix.toString());

        graph.addEdge(0, 0);
        assertEquals("[[true]]", matrix.toString());

        graph.addVertex();
        assertEquals("[[true, false], [false, false]]", matrix.toString());

        assertFalse(matrix.isEmpty());
        assertEquals(2, matrix.size());

        ((List) matrix.get(1)).set(1, true);
        assertEquals("[[true, false], [false, true]]", matrix.toString());

        graph.addVertex();
        matrix.set(2, List.of(true, true, true));
        assertEquals("[[true, false, false], [false, true, false], [true, true, true]]", matrix.toString());

        graph.removeEdge(2, 0);
        assertEquals("[false, true, true]", matrix.get(2).toString());

        graph.removeVertex();
        assertEquals("[[true, false], [false, true]]", matrix.toString());
        assertEquals(List.of(List.of(true, false), List.of(false, true)), matrix);

        Iterator it = matrix.iterator();
        assertTrue(it.hasNext());
        assertEquals(List.of(true, false), it.next());
        assertTrue(it.hasNext());
        assertEquals("[false, true]", it.next().toString());

        matrix.clear();
        assertEquals("[]", matrix.toString());
    }

    @Test
    public void testMapView() {
        GraphView graph = new GraphView();

        Map map = graph.asAdjacencyMap();
        assertEquals("{}", map.toString());
        assertEquals(Map.of(), map);

        graph.addVertex();
        assertEquals("{0=[]}", map.toString());

        graph.addEdge(0, 0);
        assertEquals("{0=[0]}", map.toString());

        graph.addVertex();
        assertEquals("{0=[0], 1=[]}", map.toString());

        assertFalse(map.isEmpty());
        assertFalse(map.entrySet().isEmpty());
        assertEquals(2, map.size());
        assertEquals(2, map.entrySet().size());
        assertTrue(map.entrySet().contains(Map.entry(0, Set.of(0))));

        ((Set) map.get(1)).add(1);
        assertEquals("{0=[0], 1=[1]}", map.toString());

        graph.addVertex();
        map.put(2, Set.of(1, 2, 0));
        assertEquals("{0=[0], 1=[1], 2=[0, 1, 2]}", map.toString());

        graph.addEdge(1, 2);
        assertEquals("[1, 2]", map.get(1).toString());

        graph.removeEdge(2, 0);
        assertEquals("{0=[0], 1=[1, 2], 2=[1, 2]}", map.toString());
        assertEquals(Map.of(0, Set.of(0), 1, Set.of(1, 2), 2, Set.of(1, 2)), map);

        graph.removeVertex();
        assertFalse(map.containsKey(2));
        assertEquals("{0=[0], 1=[1]}", map.toString());
        assertEquals(Set.of(Map.entry(0, Set.of(0)), Map.entry(1, Set.of(1))), map.entrySet());

        Iterator it = map.entrySet().iterator();
        assertTrue(it.hasNext());
        assertEquals(Map.entry(0, Set.of(0)), it.next());
        assertTrue(it.hasNext());
        assertEquals("1=[1]", it.next().toString());

        map.clear();
        assertEquals("{}", map.toString());
    }

    @Test
    public void testSync() {
        GraphView graph = new GraphView();
        List matrix = graph.asAdjacencyMatrix();
        Map map = graph.asAdjacencyMap();

        graph.addVertex();
        graph.addVertex();

        map.put(0, Set.of(1));
        assertEquals("[[false, true], [false, false]]", matrix.toString());

        matrix.set(1, List.of(true, false));
        assertEquals("{0=[1], 1=[0]}", map.toString());
    }
}
