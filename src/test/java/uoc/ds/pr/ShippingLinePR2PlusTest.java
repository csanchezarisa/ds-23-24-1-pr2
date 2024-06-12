package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.Port;
import uoc.ds.pr.model.Route;

public class ShippingLinePR2PlusTest extends  ShippingLinePR2Test {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.theShippingLine = FactoryShippingLine.getShippingLine();
        Assert.assertEquals(23, this.theShippingLine.numPorts());
    }


    @Test
    public void existsRouteBetweenTest() throws DSException {

        Assert.assertThrows(SrcPortNotFoundException.class, () ->
                theShippingLine.existsRouteBetween("XXX", "ROME"));

        Assert.assertThrows(DstPortNotFoundException.class, () ->
                theShippingLine.existsRouteBetween("ROME", "XXX"));

        Assert.assertThrows(SamePortException.class, () ->
                theShippingLine.existsRouteBetween("ROME", "ROME"));

        Assert.assertTrue(theShippingLine.existsRouteBetween("ATH", "TUN"));
        Assert.assertFalse(theShippingLine.existsRouteBetween("ATH", "DBK"));

    }

    @Test
    public void getBestKmsRouteTest() throws DSException {
        Assert.assertThrows(SrcPortNotFoundException.class, () ->
                theShippingLine.getBestKmsRoute("XXX", "ROME"));

        Assert.assertThrows(DstPortNotFoundException.class, () ->
                theShippingLine.getBestKmsRoute("ROME", "XXX"));

        Assert.assertThrows(SamePortException.class, () ->
                theShippingLine.getBestKmsRoute("ROME", "ROME"));

        Iterator<Route> it = theShippingLine.getBestKmsRoute("BCN", "VLC");
        double total = 0;

        Route route = null;
        Assert.assertTrue(it.hasNext());
        route = it.next();
        total+=route.getKms();

        Assert.assertEquals("Barcelona-Tarragona", route.toString());
        Assert.assertEquals(100, route.getKms(),0);

        Assert.assertTrue(it.hasNext());
        route = it.next();
        total+=route.getKms();
        Assert.assertEquals("Tarragona-La Ràpita", route.toString());
        Assert.assertEquals(69, route.getKms(),0);


        Assert.assertTrue(it.hasNext());
        route = it.next();
        total+=route.getKms();
        Assert.assertEquals("La Ràpita-Valencia", route.toString());
        Assert.assertEquals(169, route.getKms(),0);


        Assert.assertEquals(338, total,0);

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getBestPortsRouteTest() throws DSException {
        Assert.assertThrows(SrcPortNotFoundException.class, () ->
                theShippingLine.getBestPortsRoute("XXX", "ROME"));

        Assert.assertThrows(DstPortNotFoundException.class, () ->
                theShippingLine.getBestPortsRoute("ROME", "XXX"));

        Assert.assertThrows(SamePortException.class, () ->
                theShippingLine.getBestPortsRoute("ROME", "ROME"));

        Iterator<Route> it = theShippingLine.getBestPortsRoute("BCN", "VLC");
        double total = 0;

        Route route = null;
        Assert.assertTrue(it.hasNext());
        route = it.next();
        total+=route.getKms();
        Assert.assertEquals("Barcelona-Ibiza", route.toString());
        Assert.assertEquals(209, route.getKms(),0);


        Assert.assertTrue(it.hasNext());
        route = it.next();
        total+=route.getKms();
        Assert.assertEquals("Ibiza-Valencia", route.toString());
        Assert.assertEquals(208, route.getKms(),0);

        Assert.assertFalse(it.hasNext());

    }


}
