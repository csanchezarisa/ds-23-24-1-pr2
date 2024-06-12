package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.net.InterfaceAddress;


public class ShippingLinePR2Test extends ShippingLinePR1Test {


    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.theShippingLine = FactoryShippingLine.getShippingLine();
        Assert.assertEquals(23, this.theShippingLine.numPorts());


    }


    @Test
    public void addPortTest() {
        theShippingLine.addPort("ESTAR", "Talagona", "https://imageT", "description (TAL)");
        Assert.assertEquals(24, this.theShippingLine.numPorts());

        theShippingLine.addPort("ESTAR", "Tarragona", "https://imageT", "description (TARRAGONA)");
        Assert.assertEquals(24, this.theShippingLine.numPorts());

        theShippingLine.addPort("ESVLC", "Valencia", "https://imageVLC", "description (VLC)");
        Assert.assertEquals(25, this.theShippingLine.numPorts());
    }

    @Test
    public void addCategoryTest() {
        Assert.assertEquals(0, this.theShippingLine.numCategories());
        theShippingLine.addCategory("S", "Soup");
        theShippingLine.addCategory("M", "Meats");
        theShippingLine.addCategory("V", "Vegetables");
        theShippingLine.addCategory("F", "Fish");
        theShippingLine.addCategory("FR", "friuts");
        theShippingLine.addCategory("D", "Drinks");

        Category c = theShippingLine.getCategory("FR");
        Assert.assertEquals("friuts", c.getName());
        Assert.assertEquals(6, this.theShippingLine.numCategories());
        theShippingLine.addCategory("FR", "fruits");
        c = theShippingLine.getCategory("FR");
        Assert.assertEquals("fruits", c.getName());
        Assert.assertEquals(6, this.theShippingLine.numCategories());
    }


    @Test
    public void addProductTest() throws DSException {
        addCategoryTest();

        Assert.assertThrows(CategoryNotFoundException.class, () ->
                theShippingLine.addProduct("P1", "Apple", "Royal Gala", "XXXXX"));


        theShippingLine.addProduct("P1", "Apple", "Royal Gala", "V");
        theShippingLine.addProduct("P2", "Pear", "Sant Joan", "V");
        Assert.assertEquals(2, this.theShippingLine.numProducts());
        Assert.assertEquals(2, this.theShippingLine.numProducts("V"));
        theShippingLine.addProduct("P3", "Cod", "Icelandic", "F");
        Assert.assertEquals(3, this.theShippingLine.numProducts());
        Assert.assertEquals(2, this.theShippingLine.numProducts("V"));
        Assert.assertEquals(1, this.theShippingLine.numProducts("F"));
        theShippingLine.addProduct("P3", "Cod", "Icelandic", "F");
        Assert.assertEquals(2, this.theShippingLine.numProducts("V"));
        Assert.assertEquals(1, this.theShippingLine.numProducts("F"));

        theShippingLine.addProduct("P4", "Hake", "Icelandic", "F");
        Assert.assertEquals(2, this.theShippingLine.numProducts("V"));
        Assert.assertEquals(2, this.theShippingLine.numProducts("F"));
        Assert.assertEquals(4, this.theShippingLine.numProducts());

        theShippingLine.addProduct("P5", "Beef", "Irish", "M");
        Assert.assertEquals(2, this.theShippingLine.numProducts("V"));
        Assert.assertEquals(2, this.theShippingLine.numProducts("F"));
        Assert.assertEquals(1, this.theShippingLine.numProducts("M"));
        Assert.assertEquals(5, this.theShippingLine.numProducts());

    }


    @Test
    public void getProductsByCategoryTest() throws DSException {

        Assert.assertThrows(CategoryNotFoundException.class, () ->
                theShippingLine.getProductsByCategory("XXXXX"));

        addProductTest();

        Assert.assertThrows(NoProductsException.class, () ->
                theShippingLine.getProductsByCategory("D"));

        Assert.assertEquals(2, this.theShippingLine.numProducts("V"));
        Iterator<Product> it = theShippingLine.getProductsByCategory("V");

        Product p;

        Assert.assertTrue(it.hasNext());
        p = it.next();
        Assert.assertEquals("Apple", p.getName());

        Assert.assertTrue(it.hasNext());
        p = it.next();
        Assert.assertEquals("Pear", p.getName());

        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void linkProductTest() throws DSException {
        addProductTest();

        Assert.assertThrows(ProductNotFoundException.class, () ->
                theShippingLine.linkProduct("XXX", "shipId1"));

        Assert.assertThrows(ShipNotFoundException.class, () ->
                theShippingLine.linkProduct("P1", "XXX"));

        theShippingLine.linkProduct("P1", "shipId1");
        theShippingLine.linkProduct("P2", "shipId1");
        theShippingLine.linkProduct("P3", "shipId1");

        theShippingLine.linkProduct("P1", "shipId4");
        theShippingLine.linkProduct("P2", "shipId4");
        theShippingLine.linkProduct("P3", "shipId4");


        theShippingLine.linkProduct("P1", "shipId2");
        theShippingLine.linkProduct("P3", "shipId2");

        Assert.assertEquals(3, theShippingLine.numProductsByShip("shipId1"));
        Assert.assertEquals(2, theShippingLine.numProductsByShip("shipId2"));
        Assert.assertEquals(3, theShippingLine.numProductsByShip("shipId4"));

        Assert.assertThrows(ProductAlreadyOnMenuException.class, () ->
                theShippingLine.linkProduct("P1", "shipId1"));

    }

    @Test
    public void unlinkProductTest() throws DSException {
        linkProductTest();

        Assert.assertThrows(ProductNotFoundException.class, () ->
                theShippingLine.unlinkProduct("XXX", "shipId1"));

        Assert.assertThrows(ShipNotFoundException.class, () ->
                theShippingLine.unlinkProduct("P1", "XXX"));

        Assert.assertThrows(ProductNotInMenuException.class, () ->
                theShippingLine.unlinkProduct("P4", "shipId1"));
        Assert.assertThrows(ProductNotInMenuException.class, () ->
                theShippingLine.unlinkProduct("P2", "shipId2"));


        Assert.assertEquals(3, theShippingLine.numProductsByShip("shipId1"));
        theShippingLine.unlinkProduct("P1", "shipId1");
        Assert.assertEquals(2, theShippingLine.numProductsByShip("shipId1"));
        theShippingLine.unlinkProduct("P2", "shipId1");
        Assert.assertEquals(1, theShippingLine.numProductsByShip("shipId1"));
        theShippingLine.unlinkProduct("P3", "shipId1");
        Assert.assertEquals(0, theShippingLine.numProductsByShip("shipId1"));

        Assert.assertEquals(2, theShippingLine.numProductsByShip("shipId2"));
        theShippingLine.unlinkProduct("P1", "shipId2");
        Assert.assertEquals(1, theShippingLine.numProductsByShip("shipId2"));
        theShippingLine.unlinkProduct("P3", "shipId2");
        Assert.assertEquals(0, theShippingLine.numProductsByShip("shipId2"));
    }

    @Test
    public void getVoyageProductsByCategoryTest() throws DSException {
        addVoyageTest();
        linkProductTest();

        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.getVoyageProductsByCategory("XXX", "F"));

        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.getVoyageProductsByCategory("Ship2", "XXX"));


        Iterator<Product> it = theShippingLine.getVoyageProductsByCategory("voyageId2", "F");

        Assert.assertTrue(it.hasNext());
        Product p = it.next();

        Assert.assertEquals("P3", p.getId());
        Assert.assertEquals("F", p.getCategory().getId());
        Assert.assertFalse(it.hasNext());

        it = theShippingLine.getVoyageProductsByCategory("voyageId2", "V");

        Assert.assertTrue(it.hasNext());
        p = it.next();

        Assert.assertEquals("P1", p.getId());
        Assert.assertEquals("V", p.getCategory().getId());
        Assert.assertFalse(it.hasNext());

        Assert.assertThrows(NoProductsException.class, () ->
                theShippingLine.getVoyageProductsByCategory("voyageId3", "V"));
    }


    @Test
    public void makeOrderTest() throws DSException {
        loadTest();
        linkProductTest();
        String[] productsOK = {"P1", "P2", "P3"};

        String[] productsKO1 = {"P1", "P6", "P3"};
        String[] productsKO2 = {"P1", "P2", "P3", "P5"};

        Assert.assertThrows(ClientNotFoundException.class, () ->
                theShippingLine.makeOrder("clientIdXXXX", "voyageId1", productsOK, 25.3));

        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.makeOrder("clientId1", "voyageIdXXXX", productsOK, 25.3));

        Assert.assertThrows(ClientIsNotInVoyageException.class, () ->
                theShippingLine.makeOrder("clientId18", "voyageId1", productsOK, 25.3));

        Assert.assertThrows(ProductNotFoundException.class, () ->
                theShippingLine.makeOrder("clientId1", "voyageId1", productsKO1, 25.3));

        Assert.assertThrows(ProductNotInMenuException.class, () ->
                theShippingLine.makeOrder("clientId1", "voyageId1", productsKO2, 25.3));

        theShippingLine.makeOrder("clientId1", "voyageId1", productsOK, 25.3);
        theShippingLine.makeOrder("clientId2", "voyageId1", productsOK, 25.3);
        theShippingLine.makeOrder("clientId3", "voyageId1", productsOK, 25.3);
        theShippingLine.makeOrder("clientId4", "voyageId1", productsOK, 25.3);
        theShippingLine.makeOrder("clientId5", "voyageId1", productsOK, 25.3);
        theShippingLine.makeOrder("clientId6", "voyageId1", productsOK, 25.3);
        theShippingLine.makeOrder("clientId15", "voyageId1", productsOK, 25.3);
        Assert.assertEquals(7, theShippingLine.numOrders("voyageId1"));

        theShippingLine.makeOrder("clientId1", "voyageId1", productsOK, 25.3);
        Assert.assertEquals(8, theShippingLine.numOrders("voyageId1"));
    }

    @Test
    public void serveOrderTest() throws DSException {
        makeOrderTest();

        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.serveOrder("XXXX"));

        Assert.assertThrows(NoOrdersException.class, () ->
                theShippingLine.serveOrder("voyageId2"));

        Order order1 = theShippingLine.serveOrder("voyageId1");
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.BRONZE, order1.getClient().getLevel());
        Assert.assertEquals("clientId1", order1.getClient().getId());

        Order order2 = theShippingLine.serveOrder("voyageId1");
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.BRONZE, order2.getClient().getLevel());
        Assert.assertEquals("clientId2", order2.getClient().getId());

        Order order3 = theShippingLine.serveOrder("voyageId1");
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.BRONZE, order3.getClient().getLevel());
        Assert.assertEquals("clientId3", order3.getClient().getId());
    }

    @Test
    public void getOrdersByClientTest() throws DSException {
        makeOrderTest();

        Assert.assertThrows(ClientNotFoundException.class, () ->
                theShippingLine.getOrdersByClient("clientXXXX"));

        Assert.assertThrows(NoOrdersException.class, () ->
                theShippingLine.getOrdersByClient("clientId14"));


        Iterator<Order> it = theShippingLine.getOrdersByClient("clientId1");
        Order o1 = it.next();
        Assert.assertEquals(25.3, o1.getPrice(), 0);

        Order o2 = it.next();
        Assert.assertEquals(25.3, o2.getPrice(), 0);

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getOrdersByShipTest() throws DSException {
        serveOrderTest();
        Assert.assertThrows(ShipNotFoundException.class, () ->
                theShippingLine.getOrdersByShip("shipXXXX"));

        Assert.assertThrows(NoOrdersException.class, () ->
                theShippingLine.getOrdersByShip("shipId5"));


        Iterator<Order> it = theShippingLine.getOrdersByShip("shipId4");
        Assert.assertTrue(it.hasNext());
        Order o = it.next();
        Assert.assertEquals("clientId1", o.getClient().getId());

        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId2", o.getClient().getId());

        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId3", o.getClient().getId());


        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId4", o.getClient().getId());

        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId5", o.getClient().getId());

        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId6", o.getClient().getId());

        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId15", o.getClient().getId());


        Assert.assertTrue(it.hasNext());
        o = it.next();
        Assert.assertEquals("clientId1", o.getClient().getId());


        Assert.assertFalse(it.hasNext());
    }


    @Test
    public void getLevelTest() throws DSException {

        Assert.assertThrows(ClientNotFoundException.class, () ->
                theShippingLine.getLevel("clientXXXX"));

        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.BRONZE, theShippingLine.getLevel("clientId1"));

    }


    @Test
    public void getRoutesByOriginTest() throws DSException {
        Assert.assertThrows(NoRouteException.class, () ->
                theShippingLine.getRoutesByOrigin("DBK"));

        Route route = null;
        Iterator<Route> it = theShippingLine.getRoutesByOrigin("ATH");
        Assert.assertTrue(it.hasNext());
        route = it.next();
        Assert.assertEquals("routeId1", route.getId());

        Assert.assertTrue(it.hasNext());
        route = it.next();
        Assert.assertEquals("routeId4", route.getId());

        Assert.assertTrue(it.hasNext());
        route = it.next();
        Assert.assertEquals("routeId5", route.getId());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void getRouteTest() throws DSException {
        Assert.assertThrows(SamePortException.class, () ->
                theShippingLine.getRoute("MLG", "MLG"));

        Assert.assertThrows(NoRouteException.class, () ->
                theShippingLine.getRoute("MLG", "ATH"));


        Route route = theShippingLine.getRoute("MLG", "TAN");

        Assert.assertEquals("routeId6", route.getId());

    }

    @Test
    public void getVoyagesByRouteTest() throws NoVoyagesException {
        Assert.assertThrows(NoVoyagesException.class, () ->
                theShippingLine.getVoyagesByRoute("routeId3"));


        Iterator<Voyage> it = theShippingLine.getVoyagesByRoute("routeId2");
        Voyage voyage = null;

        Assert.assertTrue(it.hasNext());
        voyage = it.next();
        Assert.assertEquals("voyageId2", voyage.getId());

        Assert.assertTrue(it.hasNext());
        voyage = it.next();
        Assert.assertEquals("voyageId3", voyage.getId());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void best5ClientsTest() throws DSException {

        Assert.assertThrows(NoClientException.class, () ->
                theShippingLine.best5Clients());
        makeOrderTest();

        Iterator<Client> it = theShippingLine.best5Clients();

        Client client = null;
        Assert.assertTrue(it.hasNext());
        client = it.next();
        Assert.assertEquals("clientId1", client.getId());
        Assert.assertEquals(2, client.numOrders());

        Assert.assertTrue(it.hasNext());
        client = it.next();
        Assert.assertEquals("clientId2", client.getId());
        Assert.assertEquals(1, client.numOrders());

        Assert.assertTrue(it.hasNext());
        client = it.next();
        Assert.assertEquals("clientId3", client.getId());
        Assert.assertEquals(1, client.numOrders());

        Assert.assertTrue(it.hasNext());
        client = it.next();
        Assert.assertEquals("clientId4", client.getId());
        Assert.assertEquals(1, client.numOrders());

        Assert.assertTrue(it.hasNext());
        client = it.next();
        Assert.assertEquals("clientId5", client.getId());
        Assert.assertEquals(1, client.numOrders());

    }



}