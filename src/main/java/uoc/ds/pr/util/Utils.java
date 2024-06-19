package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedEdge;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraph;
import edu.uoc.ds.adt.nonlinear.graphs.Edge;
import edu.uoc.ds.adt.nonlinear.graphs.Vertex;
import edu.uoc.ds.adt.sequential.Container;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;

import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and must not be initialized");
    }

    /**
     * Loops an {@link edu.uoc.ds.traversal.Iterator} evaluating the predicate. If there is any match with it, true is
     * returned. Otherwise, false.
     *
     * @param it iterator to loop
     * @param predicate predicate to evaluate each element in the iterator
     * @return true is there is any match with the predicate, else false
     * @param <E> class type of the elements contained in the iterator
     */
    public static <E> boolean anyMatch(Iterator<E> it, Predicate<E> predicate) {
        return find(it, predicate).isPresent();
    }

    /**
     * Loops a {@link edu.uoc.ds.traversal.Traversal} looking for an element that suits the predicate.
     *
     * @param it traversal iterator to loop
     * @param predicate predicate used to evaluate each element in the iterator
     * @return an optional containing the search result
     * @param <E> class type of the elements contained in the iterator
     */
    public static <E> Optional<Position<E>> find(Traversal<E> it, Predicate<E> predicate) {
        while (it.hasNext()) {
            Position<E> pos = it.next();
            if (predicate.test(pos.getElem())) {
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }

    /**
     * Loops an {@link edu.uoc.ds.traversal.Iterator} evaluating the predicate. If there is any match with it, then
     * it's returned.
     *
     * @param it iterator to loop
     * @param predicate predicate to evaluate each element in the iterator
     * @return optional containing the result
     * @param <E> class type of the elements contained in the iterator
     */
    public static <E> Optional<E> find(Iterator<E> it, Predicate<E> predicate) {
        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }


    /**
     * Loops and filters an {@link edu.uoc.ds.traversal.Iterator} evaluating the predicate. Creates a list with the
     * elements that fit the predicate.
     *
     * @param it iterator to loop
     * @param predicate predicate to evaluate each element in the iterator
     * @return new list containing the elements that fit the predicate
     * @param <E> class type of the elements contained in the iterator
     */
    public static <E> List<E> filter(Iterator<E> it, Predicate<E> predicate) {
        List<E> result = new LinkedList<>();

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                result.insertEnd(e);
            }
        }

        return result;
    }

    /**
     * Loops an {@link edu.uoc.ds.traversal.Iterator} and counts the number of elements that fit a predicate.
     *
     * @param it iterator to loop
     * @param predicate predicate to evaluate each element in the iterator
     * @return the number of elements that fit the predicate
     * @param <E> class type of the elements contained in the iterator
     */
    public static <E> int count(Iterator<E> it, Predicate<E> predicate) {
        int count = 0;

        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Converts any collection from DSLib into a List.
     *
     * @param collection collection of elements to be converted
     * @param <E>        class type of the elements contained in the iterator
     * @return list containing all the elements
     */
    public static <E> List<E> toList(Container<E> collection) {
        List<E> result = new LinkedList<>();

        var it = collection.values();
        while (it.hasNext()) {
            result.insertEnd(it.next());
        }

        return result;
    }

    /**
     * Runs through the graph and tries to find a path between the source and the destination vertexes.
     *
     * @param graph graph to be analysed
     * @param src   source vertex
     * @param dst   destination vertex
     * @return boolean indicating whether the path exists or not
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

    private static <E, L> Set<Vertex<E>> edgeIteratorToVertedJavaSet(Iterator<Edge<L, E>> it) {
        Set<Vertex<E>> result = new HashSet<>();

        while (it.hasNext()) {
            var e = (DirectedEdge<L, E>) it.next();
            result.add(e.getVertexDst());
        }

        return result;
    }
}
