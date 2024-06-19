package uoc.ds.pr.util;

import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraph;
import edu.uoc.ds.algorithms.MinimumPaths;
import uoc.ds.pr.model.Port;
import uoc.ds.pr.model.Route;

import java.util.Arrays;

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
    public static boolean existsRouteBetween(DirectedGraph<Port, Route> graph, Port src, Port dst) {

        var minPaths = MIN_PATH_ALGORITHM.calculate(graph, graph.getVertex(src));

        double cost = Arrays.stream(minPaths)
                .filter(k -> k.getKey().getValue().equals(dst))
                .map(k -> k.getValue().doubleValue())
                .findFirst()
                .orElse(Double.POSITIVE_INFINITY);

        return cost != Double.POSITIVE_INFINITY;
    }
}
