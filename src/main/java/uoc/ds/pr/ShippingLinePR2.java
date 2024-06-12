package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;


public interface ShippingLinePR2 extends ShippingLine {

    public static final int BEST_5_CLIENTS = 5;

    enum LoyaltyLevel {
        DIAMOND,
        GOLD,
        SILVER,
        BRONZE
    }

    public void addPort(String id, String name, String imageUrl, String description);

    public void addCategory(String id, String name);

    public void addProduct(String id, String name, String description, String idCategory) throws CategoryNotFoundException;

    public Iterator<Product> getProductsByCategory(String categoryId) throws CategoryNotFoundException, NoProductsException;


    public void  linkProduct(String productId, String shipId) throws ProductNotFoundException, ShipNotFoundException, ProductAlreadyOnMenuException;

    public void unlinkProduct(String productId, String shipId) throws ProductNotFoundException, ShipNotFoundException, ProductNotInMenuException;

    public Iterator<Product> getVoyageProductsByCategory(String voyageId, String categoryId) throws VoyageNotFoundException, CategoryNotFoundException, NoProductsException;

    public void makeOrder(String clientId, String voyageId, String[] products, double price) throws ClientNotFoundException, VoyageNotFoundException,
            ProductNotFoundException, ProductNotInMenuException, ClientIsNotInVoyageException;

    public Order serveOrder(String voyageId) throws VoyageNotFoundException, NoOrdersException;

    public Iterator<Order> getOrdersByClient(String clientId) throws ClientNotFoundException, NoOrdersException;

    public Iterator<Order> getOrdersByShip(String shipId) throws ShipNotFoundException, NoOrdersException;

    public LoyaltyLevel getLevel(String clientId) throws ClientNotFoundException;

    public Iterator<Route> getRoutesByOrigin(String portId) throws NoRouteException;

    public Route getRoute(String idBeginningPort, String idArrivalPort) throws NoRouteException, SamePortException;

    public Iterator<Voyage> getVoyagesByRoute(String routeId) throws NoVoyagesException;

    public Iterator<Client> best5Clients() throws NoClientException;

    public boolean existsRouteBetween(String idAPort, String idBPort) throws SamePortException, SrcPortNotFoundException, DstPortNotFoundException;

    public Iterator<Route> getBestKmsRoute(String idAPort, String idBPort) throws SamePortException, SrcPortNotFoundException, DstPortNotFoundException, NoRouteException;

    public Iterator<Route> getBestPortsRoute(String idAPort, String idBPort) throws SamePortException, SrcPortNotFoundException, DstPortNotFoundException, NoRouteException;


    ////////////
    public int numPorts();

    public int numCategories();

    public int numProducts();
    public int numProducts(String categoryId);

    public int numProductsByShip(String shipId);

    public int numOrders(String voyageId);

    public Category getCategory(String id);
}
