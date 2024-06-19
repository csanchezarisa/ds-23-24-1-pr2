package uoc.ds.pr.util;

import edu.uoc.ds.adt.nonlinear.graphs.DirectedEdge;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraph;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.algorithms.MinimumPaths;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.Port;
import uoc.ds.pr.model.Route;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public final class GraphUtils {

    private static final MinimumPaths<Port, Route> MIN_PATH_ALGORITHM = new MinimumPaths<>();

    private GraphUtils() {
        throw new UnsupportedOperationException("This is a utility class and must not be initialized");
    }

    /**
     * Checks if a route exist between two ports.
     *
     * @param graph to be  analysed
     * @param src   source port
     * @param dst   destination port
     * @return boolean indicating whether a route exists or not
     */
    public static <E, L> boolean existConnection(DirectedGraph<E, L> graph, E src, E dst) {
        Vertex<E> srcVertex = graph.getVertex(src);
        Vertex<E> dstVertex = graph.getVertex(dst);

        return existConnection(graph, srcVertex, dstVertex, new java.util.LinkedList<>(), new HashSet<>());
    }


    /**
     * Recursive method that tries to find a path between source and destination vertexes.
     *
     * @param graph   graph to be analysed
     * @param src     current source vertex
     * @param dst     destination vertex
     * @param pending queue of vertexes that are pending to be analysed
     * @param visited collection of visited vertexes
     * @return boolean indicating whether the path exists or not
     */
    private static <E, L> boolean existConnection(DirectedGraph<E, L> graph, Vertex<E> src, Vertex<E> dst,
                                                  Queue<Vertex<E>> pending, Set<Vertex<E>> visited) {
        if (pending.contains(dst)) {
            return true;
        }

        pending.addAll(edgeIteratorToVertedJavaSet(graph.edgesWithSource(src)));
        pending.removeAll(visited);
        visited.add(src);

        if (pending.isEmpty()) {
            return false;
        }
        return existConnection(graph, pending.remove(), dst, pending, visited);
    }

    /**
     * Converts and edge iterator into a set of destination vectors
     *
     * @param it edge iterator
     * @return set of destination vectors
     */
    private static <E, L> Set<Vertex<E>> edgeIteratorToVertedJavaSet(Iterator<Edge<L, E>> it) {
        Set<Vertex<E>> result = new HashSet<>();

        while (it.hasNext()) {
            var e = (DirectedEdge<L, E>) it.next();
            result.add(e.getVertexDst());
        }

        return result;
    }
}
