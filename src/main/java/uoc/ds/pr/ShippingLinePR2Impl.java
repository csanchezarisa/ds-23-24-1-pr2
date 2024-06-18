package uoc.ds.pr;

import java.util.Comparator;
import java.util.Date;

import edu.uoc.ds.adt.nonlinear.HashTable;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DSArray;
import uoc.ds.pr.util.DSLinkedList;
import uoc.ds.pr.util.OrderedVector;
import uoc.ds.pr.util.Utils;


public class ShippingLinePR2Impl implements ShippingLinePR2 {

    private static final int MAX = 15;

    private DSArray<Ship> ships;
    private HashTable<String, Route> routes;
    private DSLinkedList<Client> clients;
    private DSLinkedList<Voyage> voyages;
    private HashTable<String, Port> ports;
    private DSLinkedList<Category> categories;
    private HashTable<String, Product> products;

    private OrderedVector<Client>  bestClient;
	private OrderedVector<Route> bestRoute;





    public ShippingLinePR2Impl() {
        ships = new DSArray<>(MAX_NUM_SHIPS);
        routes = new HashTable<>();
        clients = new DSLinkedList<>(Client.CMP);
        voyages = new DSLinkedList<>(Voyage.CMP);
        ports = new HashTable<>();
        categories = new DSLinkedList<>(Comparator.comparing(Category::getId));
        products = new HashTable<>();
        bestClient = new OrderedVector<>(MAX_CLIENTS, Client.CMP_V);
        bestRoute = new OrderedVector<>(MAX_NUM_ROUTES, Route.CMP_V);
    }


    @Override
    public void addShip(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingLots, int unLoadTimeinMinutes) {
        Ship ship = getShip(id);
        if (ship == null) {
            ship = new Ship(id, name, nArmChairs, nCabins2, nCabins4, nParkingLots, unLoadTimeinMinutes);
            this.ships.put(id, ship);
        }
        else {
            ship.update(name, nArmChairs, nCabins2, nCabins4, nParkingLots, unLoadTimeinMinutes);
        }
    }

    @Override
    public void addRoute(String id, String beginningPort, String arrivalPort, double kms) throws SrcPortNotFoundException, DstPortNotFoundException, RouteAlreadyExistException {
        final Port srcPort = getPort(beginningPort);
        if (srcPort == null) {
            throw new SrcPortNotFoundException();
        }

        final Port dstPort = getPort(arrivalPort);
        if (dstPort == null) {
            throw new DstPortNotFoundException();
        }

        if (Utils.anyMatch(routes.values(), r -> r.getDstPort().equals(dstPort) && r.getSrcPort().equals(srcPort))) {
            throw new RouteAlreadyExistException();
        }

        Route route = getRoute(id);
        if (route == null) {
            route = new Route(id, srcPort, dstPort, kms);
            this.routes.put(id, route);
        }
        else {
            route.update(srcPort, dstPort);
        }
    }

    public void addClient(String id, String name, String surname) {

        Client client = getClient(id);
        if (client == null) {
            client = new Client(id, name, surname);
            clients.insertEnd(client);
        }
        else {
            client.update(name, surname);
        }
    }

    @Override
    public void addPort(String id, String name, String imageUrl, String description) {
        Port port = getPort(id);
        if (port != null) {
            port.update(imageUrl, description, name);
            return;
        }
        port = new Port(id, imageUrl, description, name);
        ports.put(id, port);
    }

    @Override
    public void addCategory(String id, String name) {
        Category category = getCategory(id);
        if (category != null) {
            category.update(name);
            return;
        }
        category = new Category(id, name);
        categories.insertEnd(category);
    }

    @Override
    public void addProduct(String id, String name, String description, String idCategory) throws CategoryNotFoundException {
        Category category = getCategory(idCategory);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        category.deleteProduct(id);

        Product product = getProduct(id);
        if (product != null) {
            product.update(name, description, category);
        } else {
            product = new Product(id, name, description, category);
            products.put(id, product);
        }
        category.addProduct(product);
    }


    @Override
    public void addVoyage(String id, Date departureDt, Date arrivalDt, String idShip, String idRoute) throws ShipNotFoundException, RouteNotFoundException {

        Ship ship = getShip(idShip);
        if (ship == null) {
            throw new ShipNotFoundException();
        }

        Route route = getRoute(idRoute);
        if (route == null) {
            throw new RouteNotFoundException();
        }

        Voyage voyage = getVoyage(id);
        if (voyage == null) {
            voyage = new Voyage(id, departureDt, arrivalDt, ship, route);
            voyages.insertEnd(voyage);
            route.addVoyage(voyage);
            updateBestRoute(voyage.getRoute());
        }
        else {
            if (!idRoute.equals(voyage.getRoute().getId())) {
                Route oldRoute = voyage.getRoute();
                oldRoute.remove(voyage);
                route.addVoyage(voyage);
            }
            voyage.update(departureDt, arrivalDt, ship, route);
        }

    }


    public void reserve(String[] clients, String idVoyage, AccommodationType accommodationType, String idVehicle, double price)
            throws ClientNotFoundException, VoyageNotFoundException, ParkingFullException,
            NoAcommodationAvailableException, MaxExceededException, ReservationAlreadyExistsException {

        boolean parkingReserve = false;
        Voyage voyage = getVoyage(idVoyage);
        if (voyage == null) {
            throw new VoyageNotFoundException();
        }
        List<Client> clientLinkedList = getAllClients(clients, voyage);
        if (accommodationType == AccommodationType.CABIN2 && clientLinkedList.size()>MAX_PEOPLE_CABIN2) {
            throw new MaxExceededException();
        }
        else if (accommodationType == AccommodationType.CABIN4 && clientLinkedList.size()>MAX_PEOPLE_CABIN4) {
            throw new MaxExceededException();
        }


        parkingReserve = (idVehicle!=null && !voyage.parkingFull());

        if (voyage.parkingFull()) {
            throw new ParkingFullException();
        }
        Reservation reservation = null;

        if (voyage.isAcommodationAvailable(accommodationType, clientLinkedList.size())) {
            voyage.addReservation(clientLinkedList, accommodationType, price, idVehicle);
        }
        else {
            throw new NoAcommodationAvailableException();
        }

    }

    public void load(String idClient, String idVoyage, Date dt) throws
            LoadingAlreadyException, ClientNotFoundException, VoyageNotFoundException, ReservationNotFoundException {
        Client client = getClient(idClient);
        if (client == null) {
            throw new ClientNotFoundException();
        }

        Voyage voyage = getVoyage(idVoyage);
        if (voyage == null) {
            throw new VoyageNotFoundException();
        }

        Reservation reservation = client.findReservation(idVoyage);
        if (reservation == null) {
            throw new ReservationNotFoundException();
        }

        if (reservation.isLoaded()) {
            throw new LoadingAlreadyException();
        }
        reservation.loaded();
        if (reservation.hasParkingLot()) {
            reservation.loadVehicle();
            client.addVoyage(reservation.getVoyage());
            updateBestClient(client);
        }
    }

    public Iterator<Reservation> unload(String idVoyage) throws VoyageNotFoundException {
        Voyage voyage = getVoyage(idVoyage);
        if (voyage == null) {
            throw new VoyageNotFoundException();
        }
        Ship ship = voyage.getShip();
        int unLoadTimeInMinutes = ship.getUnLoadTimeInMinutes();
        Iterator<Reservation> it = voyage.parking();
        Reservation reservation = null;
        while (it.hasNext()) {
            reservation = it.next();
            reservation.setUnLoadTimeInMinutes(unLoadTimeInMinutes);
            unLoadTimeInMinutes += ship.getUnLoadTimeInMinutes();
        }
        return voyage.parking();
    }

    @Override
    public int unloadTime(String idVehicle, String idVoyage) throws LandingNotDoneException, VoyageNotFoundException, VehicleNotFoundException {
        return 0;
    }

    public Iterator<Reservation> getClientReservations(String idClient) throws NoReservationException {
        Client client = getClient(idClient);
        Iterator<Reservation> it = client.reservations();
        if (!it.hasNext()) throw new NoReservationException();

        return it;
    }

    @Override
    public Iterator<Reservation> getVoyageReservations(String idVoyage) throws NoReservationException {
        Voyage voyage = getVoyage(idVoyage);
        Iterator<Reservation> it = voyage.reservations();
        if (!it.hasNext()) throw new NoReservationException();

        return it;
    }

    public Iterator<Reservation> getAccommodationReservations(String idVoyage, AccommodationType accommodationType) throws NoReservationException {
        Voyage voyage = getVoyage(idVoyage);
        Iterator<Reservation> it = voyage.reservations(accommodationType);
        if (!it.hasNext()) throw new NoReservationException();

        return it;

    }

    public Client getMostTravelerClient() throws NoClientException {
        if (bestClient.isEmpty()) {
            throw new NoClientException();
        }
        return bestClient.elementAt(0);
    }

    public Route getMostTraveledRoute() throws NoRouteException {
        if (bestRoute.isEmpty()) {
            throw new NoRouteException();
        }
        return bestRoute.elementAt(0);
    }

    private void updateBestClient(Client client) {
        bestClient.delete(client);
        bestClient.update(client);

    }

    private void updateBestRoute(Route route) {
        bestRoute.delete(route);
        bestRoute.update(route);
    }


    private List<Client> getAllClients(String[] clients, Voyage voyage) throws ClientNotFoundException, ReservationAlreadyExistsException{
        LinkedList<Client> clientLinkedList = new LinkedList<>();
        Client client = null;
        for (String sClient : clients) {
            client = getClient(sClient);
            if (client == null) {
                throw new ClientNotFoundException();
            }
            if (!client.hasReservation(voyage)) {
                clientLinkedList.insertEnd(client);
            }
            else {
                throw new ReservationAlreadyExistsException();
            }
        }
        return clientLinkedList;
    }

    @Override
    public Iterator<Product> getProductsByCategory(String categoryId) throws CategoryNotFoundException, NoProductsException {
        final Category category = getCategory(categoryId);
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        if (category.numProducts() < 1) {
            throw new NoProductsException();
        }

        return category.products();
    }

    @Override
    public void linkProduct(String productId, String shipId) throws ProductNotFoundException, ShipNotFoundException, ProductAlreadyOnMenuException {
        Product product = getProduct(productId);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        Ship ship = getShip(shipId);
        if (ship == null) {
            throw new ShipNotFoundException();
        }

        if (Utils.anyMatch(ship.products(), p -> p.equals(product))) {
            throw new ProductAlreadyOnMenuException();
        }

        ship.addProduct(product);
    }

    @Override
    public void unlinkProduct(String productId, String shipId) throws ProductNotFoundException, ShipNotFoundException, ProductNotInMenuException {
        Product product = getProduct(productId);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        Ship ship = getShip(shipId);
        if (ship == null) {
            throw new ShipNotFoundException();
        }

        if (!Utils.anyMatch(ship.products(), p -> p.equals(product))) {
            throw new ProductNotInMenuException();
        }
        ship.deleteProduct(product);
    }

    @Override
    public Iterator<Product> getVoyageProductsByCategory(String voyageId, String categoryId) throws VoyageNotFoundException, CategoryNotFoundException, NoProductsException {
        final Voyage voyage = getVoyage(voyageId);
        if (voyage == null) {
            throw new VoyageNotFoundException();
        }

        final Category category = getCategory(categoryId);
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        List<Product> productsByCategory = Utils.filter(voyage.getShip().products(), p -> p.getCategory().equals(category));
        if (productsByCategory.isEmpty()) {
            throw new NoProductsException();
        }
        return productsByCategory.values();
    }

    @Override
    public void makeOrder(String clientId, String voyageId, String[] products, double price) throws ClientNotFoundException, VoyageNotFoundException, ProductNotFoundException, ProductNotInMenuException, ClientIsNotInVoyageException {

    }

    @Override
    public Order serveOrder(String voyageId) throws VoyageNotFoundException, NoOrdersException {
        return null;
    }

    @Override
    public Iterator<Order> getOrdersByClient(String clientId) throws ClientNotFoundException, NoOrdersException {
        return null;
    }

    @Override
    public Iterator<Order> getOrdersByShip(String shipId) throws ShipNotFoundException, NoOrdersException {
        return null;
    }

    @Override
    public LoyaltyLevel getLevel(String clientId) throws ClientNotFoundException {
        return null;
    }

    @Override
    public Iterator<Route> getRoutesByOrigin(String portId) throws NoRouteException {
        final Port srcPort = getPort(portId);

        List<Route> routesByPort = Utils.filter(routes.values(), r -> r.getSrcPort().equals(srcPort));
        if (routesByPort.isEmpty()) {
            throw new NoRouteException();
        }
        return routesByPort.values();
    }

    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/


    public Ship getShip(String id) {
        return ships.get(id);
    }

    @Override
    public Port getPort(String id) {
        return ports.get(id);
    }

    public Client getClient(String id) {
        return clients.get(new Client(id));
    }

    public Route getRoute(String idRoute) {
        return routes.get(idRoute);
    }

    public Voyage getVoyage(String id) {
        return voyages.get(new Voyage(id));
    }

    public int numShips() {
        return ships.size();
    }

    public int numRoutes() {
        return routes.size();
    }

    public int numClients() {
        return clients.size();
    }

    public int numVoyages() {return voyages.size(); }

    @Override
    public Route getRoute(String idBeginningPort, String idArrivalPort) throws NoRouteException, SamePortException {
        if (idArrivalPort.equalsIgnoreCase(idBeginningPort)) {
            throw new SamePortException();
        }

        final Port srcPort = getPort(idBeginningPort);
        final Port dstPort = getPort(idArrivalPort);

        return Utils.find(routes.values(), r -> r.getSrcPort().equals(srcPort) && r.getDstPort().equals(dstPort))
                .orElseThrow(NoRouteException::new);
    }

    @Override
    public Iterator<Voyage> getVoyagesByRoute(String routeId) throws NoVoyagesException {
        return null;
    }

    @Override
    public Iterator<Client> best5Clients() throws NoClientException {
        return null;
    }

    @Override
    public boolean existsRouteBetween(String idAPort, String idBPort) throws SamePortException, SrcPortNotFoundException, DstPortNotFoundException {
        return false;
    }

    @Override
    public Iterator<Route> getBestKmsRoute(String idAPort, String idBPort) throws SamePortException, SrcPortNotFoundException, DstPortNotFoundException, NoRouteException {
        return null;
    }

    @Override
    public Iterator<Route> getBestPortsRoute(String idAPort, String idBPort) throws SamePortException, SrcPortNotFoundException, DstPortNotFoundException, NoRouteException {
        return null;
    }

    @Override
    public int numPorts() {
        return ports.size();
    }

    @Override
    public int numCategories() {
        return categories.size();
    }

    @Override
    public int numProducts() {
        return products.size();
    }

    @Override
    public int numProducts(String categoryId) {
        return getCategory(categoryId).numProducts();
    }

    @Override
    public int numProductsByShip(String shipId) {
        return getShip(shipId).numProducts();
    }

    @Override
    public int numOrders(String voyageId) {
        return 0;
    }

    @Override
    public Category getCategory(String id) {
        return categories.get(new Category(id));
    }

    public Product getProduct(String id) {
        return products.get(id);
    }
}
