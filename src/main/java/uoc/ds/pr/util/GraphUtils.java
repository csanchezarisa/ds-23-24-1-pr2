package uoc.ds.pr.util;

import edu.uoc.ds.adt.nonlinear.graphs.DirectedEdge;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraph;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.algorithms.MinimumPaths;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.Port;
import uoc.ds.pr.model.Route;

import java.util.ArrayList;
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

        Queue<Vertex<E>> pending = new java.util.LinkedList<>();
        pending.add(srcVertex);

        return existConnection(graph, dstVertex, pending, new HashSet<>());
    }


    /**
     * Recursive method that tries to find a path between source and destination vertexes.
     *
     * @param graph   graph to be analysed
     * @param dst     destination vertex
     * @param pending queue of vertexes that are pending to be analysed
     * @param visited collection of visited vertexes
     * @return boolean indicating whether the path exists or not
     */
    private static <E, L> boolean existConnection(DirectedGraph<E, L> graph, Vertex<E> dst,
                                                  Queue<Vertex<E>> pending, Set<Vertex<E>> visited) {
        if (pending.isEmpty()) {
            return false;
        }

        if (pending.contains(dst)) {
            return true;
        }

        Vertex<E> src = pending.remove();
        pending.addAll(edgeIteratorToVertedJavaSet(graph.edgesWithSource(src)));
        pending.removeAll(visited);
        visited.add(src);

        return existConnection(graph, dst, pending, visited);
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


    /**
     * Converts and edge iterator into a set of edges
     *
     * @param it edge iterator
     * @return set of edges
     */
    private static <E, L> Set<DirectedEdge<L, E>> edgeIteratorToDirectedJavaSet(Iterator<Edge<L, E>> it) {
        Set<DirectedEdge<L, E>> result = new HashSet<>();

        while (it.hasNext()) {
            result.add((DirectedEdge<L, E>) it.next());
        }

        return result;
    }

    /**
     * Calculates and returns an iterator with the best route based on the number of ports
     *
     * @param graph to be analysed
     * @param src   source port
     * @param dst   destination port
     * @return iterator containing the best route based on the number of ports
     */
    public static List<Route> bestPortRoute(DirectedGraph<Port, Route> graph, Port src, Port dst) {
        Vertex<Port> srcVertex = graph.getVertex(src);
        Vertex<Port> dstVertex = graph.getVertex(dst);

        Set<DirectedEdge<Route, Port>> pending = new HashSet<>(edgeIteratorToDirectedJavaSet(graph.edgesWithSource(srcVertex)));

        List<Route> result = new edu.uoc.ds.adt.sequential.LinkedList<>();
        bestPortRoute(graph, dstVertex, pending)
                .forEach(result::insertEnd);
        return result;
    }

    /**
     * Recursive method that runs through the pending edges, expanding their destination vertex and trying to find
     * the best route based on the number of vertexes contained
     *
     * @param graph   graph to be anaysed
     * @param dst     destination vertex
     * @param pending pending edges to be expanded
     * @return a list containing the best route to arrive to the destination based on the number of ports. Empty list if
     * the route does not exist
     */
    private static java.util.List<Route> bestPortRoute(DirectedGraph<Port, Route> graph, Vertex<Port> dst,
                                                       Set<DirectedEdge<Route, Port>> pending) {

        if (pending.isEmpty()) {
            return new ArrayList<>();
        }

        java.util.List<Route> bestRoute = new ArrayList<>();

        for (var edge : pending) {
            var vertex = edge.getVertexDst();

            if (dst.equals(vertex)) {
                java.util.List<Route> result = new ArrayList<>();
                result.add(edge.getLabel());
                return result;
            }

            Set<DirectedEdge<Route, Port>> newPending = new HashSet<>(edgeIteratorToDirectedJavaSet(graph.edgesWithSource(vertex)));
            java.util.List<Route> finalBestRoute = bestRoute;
            newPending.removeIf(e -> finalBestRoute.contains(e.getLabel()));

            java.util.List<Route> result = bestPortRoute(graph, dst, newPending);
            if (!result.isEmpty()) {
                result.add(0, edge.getLabel());
                if (bestRoute.isEmpty() || result.size() < bestRoute.size()) {
                    bestRoute = result;
                }
            }
        }

        return bestRoute;
    }
}
