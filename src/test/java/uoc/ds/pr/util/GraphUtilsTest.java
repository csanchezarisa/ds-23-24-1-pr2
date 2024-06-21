package uoc.ds.pr.util;

import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraph;
import edu.uoc.ds.adt.nonlinear.graphs.DirectedGraphImpl;
import edu.uoc.ds.adt.sequential.List;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.model.Port;
import uoc.ds.pr.model.Route;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class GraphUtilsTest {

    private DirectedGraph<Port, Route> graph;
    private Map<String, Port> ports;

    @Before
    public void setUp() {
        graph = new DirectedGraphImpl<>();
        ports = new HashMap<>();

        var portA = graph.newVertex(new Port("A", "Port A", "image.com", "Port A"));
        var portB = graph.newVertex(new Port("B", "Port B", "image.com", "Port B"));
        var portC = graph.newVertex(new Port("C", "Port C", "image.com", "Port C"));
        var portD = graph.newVertex(new Port("D", "Port D", "image.com", "Port D"));
        var portE = graph.newVertex(new Port("E", "Port E", "image.com", "Port E"));

        graph.newEdge(portA, portB).setLabel(new Route("A-B", portA.getValue(), portB.getValue(), 20D));
        graph.newEdge(portA, portC).setLabel(new Route("A-C", portA.getValue(), portC.getValue(), 10D));
        graph.newEdge(portC, portD).setLabel(new Route("C-D", portC.getValue(), portD.getValue(), 5D));
        graph.newEdge(portD, portE).setLabel(new Route("D-E", portD.getValue(), portE.getValue(), 5D));
        graph.newEdge(portB, portE).setLabel(new Route("B-E", portB.getValue(), portE.getValue(), 50D));

        ports.put(portA.getValue().getId(), portA.getValue());
        ports.put(portB.getValue().getId(), portB.getValue());
        ports.put(portC.getValue().getId(), portC.getValue());
        ports.put(portD.getValue().getId(), portD.getValue());
        ports.put(portE.getValue().getId(), portE.getValue());
    }

    @Test
    public void testExistConnection() {
        assertTrue(GraphUtils.existConnection(graph, ports.get("A"), ports.get("E")));
        assertFalse(GraphUtils.existConnection(graph, ports.get("E"), ports.get("B")));
    }

    @Test
    public void testBestPortRoute() {
        List<Route> result = GraphUtils.bestPortRoute(graph, ports.get("C"), ports.get("B"));
        assertTrue(result.isEmpty());

        result = GraphUtils.bestPortRoute(graph, ports.get("A"), ports.get("E"));
        assertFalse(result.isEmpty());
        var it = result.values();
        assertEquals("A-B", it.next().getId());
        assertEquals("B-E", it.next().getId());
        assertFalse(it.hasNext());
    }

    @Test
    public void testBestKmsRoute() {
        List<Route> result = GraphUtils.bestKmsRoute(graph, ports.get("C"), ports.get("B"));
        assertTrue(result.isEmpty());

        result = GraphUtils.bestKmsRoute(graph, ports.get("A"), ports.get("E"));
        assertFalse(result.isEmpty());
        var it = result.values();
        assertEquals("A-C", it.next().getId());
        assertEquals("C-D", it.next().getId());
        assertEquals("D-E", it.next().getId());
        assertFalse(it.hasNext());
    }
}