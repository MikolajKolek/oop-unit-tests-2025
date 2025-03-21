import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class MikolajKolekGraphViewTest {
    public static class IllegalArgumentsTests {
        @Test
        public void add_edge_does_nothing_with_invalid_arguments() {
            GraphView graph = new GraphView();
            graph.addVertex();
            graph.addEdge(1, 0);
            graph.addEdge(0, 2);
            graph.addEdge(-1, 1);
            graph.addEdge(0, -1);
            graph.addEdge(-100, -1000);
            graph.addEdge(10000, 10000);
            assertEquals("[[false]]", graph.asAdjacencyMatrix().toString());
        }

        @Test
        public void remove_edge_does_nothing_with_invalid_arguments() {
            GraphView graph = new GraphView();
            graph.addVertex();
            graph.addEdge(0, 0);
            graph.removeEdge(1, 0);
            graph.removeEdge(0, 2);
            graph.removeEdge(-1, 1);
            graph.removeEdge(0, -1);
            graph.removeEdge(-100, -1000);
            graph.removeEdge(10000, 10000);
            assertEquals("[[true]]", graph.asAdjacencyMatrix().toString());
        }

        @Test
        public void remove_vertex_does_nothing_when_size_is_zero() {
            GraphView graph = new GraphView();
            graph.removeVertex();
            assertEquals("[]", graph.asAdjacencyMatrix().toString());
            assertEquals(0, graph.size());
        }

        @Test
        public void adjacency_map_put_throws_null_pointer_exception_when_argument_is_null() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(NullPointerException.class, () -> graph.asAdjacencyMap().put(null, null));
            assertThrows(NullPointerException.class, () -> graph.asAdjacencyMap().put(0, null));
            assertThrows(NullPointerException.class, () -> graph.asAdjacencyMap().put(null, Set.of()));
        }

        @Test
        public void adjacency_map_put_throws_illegal_argument_exception_when_key_is_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMap().put(1, Set.of()));
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMap().put(-1, Set.of()));
        }

        @Test
        public void adjacency_map_put_throws_illegal_argument_exception_when_value_entries_are_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMap().put(0, Set.of(-1)));
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMap().put(0, Set.of(1)));
        }

        @Test
        public void adjacency_map_get_add_throws_null_pointer_exception_when_argument_is_null() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(NullPointerException.class, () -> ((Set)graph.asAdjacencyMap().get(0)).add(null));
        }

        @Test
        public void adjacency_map_get_add_throws_illegal_argument_exception_when_argument_is_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IllegalArgumentException.class, () -> ((Set)graph.asAdjacencyMap().get(0)).add(-1));
            assertThrows(IllegalArgumentException.class, () -> ((Set)graph.asAdjacencyMap().get(0)).add(1));
        }

        @Test
        public void adjacency_map_get_remove_throws_null_pointer_exception_when_argument_is_null() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(NullPointerException.class, () -> ((Set)graph.asAdjacencyMap().get(0)).remove(null));
        }

        @Test
        public void adjacency_map_get_remove_throws_illegal_argument_exception_when_argument_is_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IllegalArgumentException.class, () -> ((Set)graph.asAdjacencyMap().get(0)).add(-1));
            assertThrows(IllegalArgumentException.class, () -> ((Set)graph.asAdjacencyMap().get(0)).add(1));
        }

        @Test
        public void adjacency_matrix_set_throws_null_pointer_exception_when_argument_is_null() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(NullPointerException.class, () -> graph.asAdjacencyMatrix().set(0, null));
        }

        @Test
        public void adjacency_matrix_set_throws_index_out_of_bounds_exception_when_key_is_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IndexOutOfBoundsException.class, () -> graph.asAdjacencyMatrix().set(1, List.of(false)));
            assertThrows(IndexOutOfBoundsException.class, () -> graph.asAdjacencyMatrix().set(-1, List.of(false)));
        }

        @Test
        public void adjacency_matrix_set_throws_illegal_argument_exception_when_value_is_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMatrix().set(0, List.of()));
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMatrix().set(0, List.of(false, true)));
            assertThrows(IllegalArgumentException.class, () -> graph.asAdjacencyMatrix().set(0, Collections.singletonList(null)));
        }

        @Test
        public void adjacency_matrix_get_set_throws_index_out_of_bounds_exception_when_key_is_invalid() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(IndexOutOfBoundsException.class, () -> ((List)graph.asAdjacencyMatrix().get(0)).set(1, false));
            assertThrows(IndexOutOfBoundsException.class, () -> ((List)graph.asAdjacencyMatrix().get(0)).set(-1, false));
        }

        @Test
        public void adjacency_matrix_get_set_throws_null_pointer_exception_when_value_is_null() {
            GraphView graph = new GraphView();
            graph.addVertex();
            assertThrows(NullPointerException.class, () -> ((List)graph.asAdjacencyMatrix().get(0)).set(0, null));
        }
    }

    public static class SyncTests {
        @Test
        public void large_sync_test() {
            GraphView graph = new GraphView();
            List matrix = graph.asAdjacencyMatrix();
            Map map = graph.asAdjacencyMap();

            assertEquals("[]", matrix.toString());
            assertEquals(List.of(), matrix);
            assertEquals("{}", map.toString());
            assertEquals(Map.of(), map);

            graph.addVertex();
            assertEquals("[[false]]", matrix.toString());
            assertEquals("{0=[]}", map.toString());

            graph.addEdge(0, 0);
            assertEquals("[[true]]", matrix.toString());
            assertEquals("{0=[0]}", map.toString());

            graph.addVertex();
            assertEquals("[[true, false], [false, false]]", matrix.toString());
            assertEquals("{0=[0], 1=[]}", map.toString());

            assertFalse(matrix.isEmpty());
            assertEquals(2, matrix.size());
            assertFalse(map.isEmpty());
            assertFalse(map.entrySet().isEmpty());
            assertEquals(2, map.size());
            assertEquals(2, map.entrySet().size());
            assertTrue(map.entrySet().contains(Map.entry(0, Set.of(0))));

            ((List) matrix.get(1)).set(1, true);
            assertEquals("[[true, false], [false, true]]", matrix.toString());
            assertEquals("{0=[0], 1=[1]}", map.toString());

            ((Set) map.get(1)).add(0);
            assertEquals("[[true, false], [true, true]]", matrix.toString());
            assertEquals("{0=[0], 1=[0, 1]}", map.toString());

            matrix = graph.asAdjacencyMatrix();
            map = graph.asAdjacencyMap();

            graph.addVertex();
            matrix.set(2, List.of(true, true, true));
            assertEquals("[[true, false, false], [true, true, false], [true, true, true]]", matrix.toString());
            assertEquals("{0=[0], 1=[0, 1], 2=[0, 1, 2]}", map.toString());

            map.put(2, Set.of());
            assertEquals("[[true, false, false], [true, true, false], [false, false, false]]", matrix.toString());
            assertEquals("{0=[0], 1=[0, 1], 2=[]}", map.toString());

            graph.addEdge(1, 2);
            assertEquals("[true, true, true]", matrix.get(1).toString());
            assertEquals("[0, 1, 2]", map.get(1).toString());

            graph.removeEdge(2, 0);
            assertEquals("{0=[0], 1=[0, 1, 2], 2=[]}", map.toString());
            assertEquals(Map.of(0, Set.of(0), 1, Set.of(0, 1, 2), 2, Set.of()), map);
            assertEquals("[false, false, false]", matrix.get(2).toString());

            graph.removeVertex();
            assertEquals("[[true, false], [true, true]]", matrix.toString());
            assertEquals(List.of(List.of(true, false), List.of(true, true)), matrix);
            assertFalse(map.containsKey(2));
            assertEquals("{0=[0], 1=[0, 1]}", map.toString());
            assertEquals(Set.of(Map.entry(0, Set.of(0)), Map.entry(1, Set.of(0, 1))), map.entrySet());

            graph.removeEdge(1, 0);
            Iterator it = matrix.iterator();
            assertTrue(it.hasNext());
            assertEquals(List.of(true, false), it.next());
            assertTrue(it.hasNext());
            assertEquals("[false, true]", it.next().toString());

            it = map.entrySet().iterator();
            assertTrue(it.hasNext());
            assertEquals(Map.entry(0, Set.of(0)), it.next());
            assertTrue(it.hasNext());
            assertEquals("1=[1]", it.next().toString());

            assertEquals(2, graph.size());
            matrix.clear();
            assertEquals("[]", matrix.toString());
            assertEquals("{}", map.toString());
            assertEquals(0, graph.size());

            graph.addVertex();
            assertEquals("[[false]]", matrix.toString());
            assertEquals("{0=[]}", map.toString());

            map.clear();
            assertEquals("[]", matrix.toString());
            assertEquals("{}", map.toString());
            assertEquals(0, graph.size());
        }
    }
}
