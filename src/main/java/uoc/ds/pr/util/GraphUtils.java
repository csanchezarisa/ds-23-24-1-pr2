package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.KeyValue;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedEdge;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraph;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.adt.sequential.Set;
import edu.uoc.ds.adt.sequential.SetLinkedListImpl;
import edu.uoc.ds.algorithms.MinimumPaths;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.Port;
import uoc.ds.pr.model.Route;

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

        List<Vertex<E>> pending = new LinkedList<>();
        pending.insertEnd(srcVertex);

        return existConnection(graph, dstVertex, pending, new SetLinkedListImpl<>());
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
                                                  List<Vertex<E>> pending, Set<Vertex<E>> visited) {
        if (pending.isEmpty()) {
            return false;
        }

        if (Utils.contains(pending, dst)) {
            return true;
        }

        Vertex<E> src = pending.deleteFirst();
        pending.insertAll(edgeIteratorToVertexList(graph.edgesWithSource(src)));
        Utils.removeAll(pending, visited);
        visited.add(src);

        return existConnection(graph, dst, pending, visited);
    }

    /**
     * Converts and edge iterator into a list of destination vectors
     *
     * @param it edge iterator
     * @return list of destination vectors
     */
    private static <E, L> List<Vertex<E>> edgeIteratorToVertexList(Iterator<Edge<L, E>> it) {
        List<Vertex<E>> result = new LinkedList<>();

        while (it.hasNext()) {
            var e = (DirectedEdge<L, E>) it.next();
            result.insertEnd(e.getVertexDst());
        }

        return result;
    }


    /**
     * Converts and edge iterator into a set of edges
     *
     * @param it edge iterator
     * @return set of edges
     */
    private static <E, L> List<DirectedEdge<L, E>> edgeIteratorToDirectedEdgeList(Iterator<Edge<L, E>> it) {
        List<DirectedEdge<L, E>> result = new LinkedList<>();

        while (it.hasNext()) {
            result.insertEnd((DirectedEdge<L, E>) it.next());
        }

        return result;
    }

    /**
     * Calculates and returns a list with the best route based on the number of ports
     *
     * @param graph to be analysed
     * @param src   source port
     * @param dst   destination port
     * @return list containing the best route based on the number of ports
     */
    public static List<Route> bestPortRoute(DirectedGraph<Port, Route> graph, Port src, Port dst) {
        Vertex<Port> srcVertex = graph.getVertex(src);
        Vertex<Port> dstVertex = graph.getVertex(dst);

        List<DirectedEdge<Route, Port>> pending = new LinkedList<>();
        pending.insertAll(edgeIteratorToDirectedEdgeList(graph.edgesWithSource(srcVertex)));

        return bestPortRoute(graph, dstVertex, pending);
    }

    /**
     * Recursive method that runs through the pending edges, expanding their destination vertex and trying to find
     * the best route based on the number of vertexes contained
     *
     * @param graph   graph to be analysed
     * @param dst     destination vertex
     * @param pending pending edges to be expanded
     * @return a list containing the best route to arrive to the destination based on the number of ports. Empty list if
     * the route does not exist
     */
    private static List<Route> bestPortRoute(DirectedGraph<Port, Route> graph, Vertex<Port> dst,
                                             List<DirectedEdge<Route, Port>> pending) {

        if (pending.isEmpty()) {
            return new LinkedList<>();
        }

        List<Route> bestRoute = new LinkedList<>();

        var it = pending.values();
        while (it.hasNext()) {
            var edge = it.next();

            var vertex = edge.getVertexDst();

            if (dst.equals(vertex)) {
                List<Route> result = new LinkedList<>();
                result.insertEnd(edge.getLabel());
                return result;
            }

            List<DirectedEdge<Route, Port>> newPending = new LinkedList<>();
            newPending.insertAll(edgeIteratorToDirectedEdgeList(graph.edgesWithSource(vertex)));

            List<Route> finalBestRoute = bestRoute;
            Utils.removeIf(newPending, e -> Utils.contains(finalBestRoute, e.getLabel()));

            List<Route> result = bestPortRoute(graph, dst, newPending);
            if (!result.isEmpty()) {
                result.insertBeginning(edge.getLabel());
                if (bestRoute.isEmpty() || result.size() < bestRoute.size()) {
                    bestRoute = result;
                }
            }
        }

        return bestRoute;
    }

    /**
     * Calculates and returns a list with the best route based on the distance between ports.
     * Uses {@link edu.uoc.ds.algorithms.MinimumPaths} algorithm to calculate the best paths in distance.
     *
     * @param graph to be analysed
     * @param src   source port
     * @param dst   destination port
     * @return a list containing the best route based on the distance between ports
     */
    public static List<Route> bestKmsRoute(DirectedGraph<Port, Route> graph, Port src, Port dst) {
        Vertex<Port> srcVertex = graph.getVertex(src);
        Vertex<Port> dstVertex = graph.getVertex(dst);

        var minPaths = MIN_PATH_ALGORITHM.calculate(graph, srcVertex);
        if (!isConnected(minPaths, dstVertex)) {
            return new LinkedList<>();
        }

        List<Route> bestPath = new LinkedList<>();

        boolean sourceFound;

        do {
            KeyValue<Vertex<Port>, Number> bestEntry = null;
            DirectedEdge<Route, Port> bestEdge = null;

            var it = edgeIteratorToDirectedEdgeList(graph.edgedWithDestination(dstVertex)).values();
            while (it.hasNext()) {
                var edge = it.next();

                KeyValue<Vertex<Port>, Number> edgeEntry = getEntry(minPaths, edge.getVertexSrc());
                if (bestEntry == null || bestEntry.getValue().doubleValue() > edgeEntry.getValue().doubleValue()) {
                    bestEntry = edgeEntry;
                    bestEdge = edge;
                }
            }

            sourceFound = bestEntry.getKey().equals(srcVertex);

            bestPath.insertBeginning(bestEdge.getLabel());
            dstVertex = bestEntry.getKey();
        } while (!sourceFound);

        return bestPath;
    }

    /**
     * Checks if a vertex is accessible in the KeyValues array
     *
     * @param minPaths min paths KeyValues array, result of {@link edu.uoc.ds.algorithms.MinimumPaths} algorithm
     * @param dst      destination vertex
     * @return boolean indicating whether the vertex is accessible or not
     */
    private static <K, V extends Number> boolean isConnected(KeyValue<K, V>[] minPaths, K dst) {
        for (KeyValue<K, V> entry : minPaths) {
            if (entry.getKey().equals(dst)) {
                return entry.getValue().doubleValue() != Double.POSITIVE_INFINITY;
            }
        }
        return false;
    }

    /**
     * Returns an entry in the KeyValues array produced by {@link edu.uoc.ds.algorithms.MinimumPaths}.
     *
     * @param minPaths KeyValues array produced by {@link edu.uoc.ds.algorithms.MinimumPaths} algorithm
     * @param vertex   vertex to find
     * @return KeyValue of the vertex in the array. Null if not found
     */
    private static <K, V extends Number> KeyValue<K, V> getEntry(KeyValue<K, V>[] minPaths, K vertex) {
        for (KeyValue<K, V> entry : minPaths) {
            if (entry.getKey().equals(vertex)) {
                return entry;
            }
        }
        return null;
    }
}
