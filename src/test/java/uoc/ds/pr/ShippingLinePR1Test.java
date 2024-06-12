package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;
import uoc.ds.pr.util.DateUtils;

public class ShippingLinePR1Test {

    protected ShippingLinePR2 theShippingLine;

    @Before
    public void setUp() throws Exception {
        this.theShippingLine = FactoryShippingLine.getShippingLine();
        Assert.assertEquals(7, this.theShippingLine.numShips());
        Assert.assertEquals(15, this.theShippingLine.numRoutes());
        Assert.assertEquals(18, this.theShippingLine.numClients());
        Assert.assertEquals(3, this.theShippingLine.numVoyages());
    }

    @After
    public void tearDown() {
        this.theShippingLine = null;
    }


    @Test
    public void addShipTest() {
        this.theShippingLine.addShip("shipId1000", "XXXXX", 999, 888, 777, 666, 99);
        Ship ship1000 = this.theShippingLine.getShip("shipId1000");
        Assert.assertEquals("XXXXX", ship1000.getName());
        Assert.assertEquals(999, ship1000.getnArmChairs());
        Assert.assertEquals(888, ship1000.getnCabins2());
        Assert.assertEquals(777, ship1000.getnCabins4());
        Assert.assertEquals(666, ship1000.getnParkingSlots());
        Assert.assertEquals(99, ship1000.getUnLoadTimeInMinutes());
        Assert.assertEquals(8, this.theShippingLine.numShips());

        this.theShippingLine.addShip("shipId1000", "Roma", 300, 150, 170, 300, 30);
        ship1000 = this.theShippingLine.getShip("shipId1000");
        Assert.assertEquals(300, ship1000.getnArmChairs());
        Assert.assertEquals(150, ship1000.getnCabins2());
        Assert.assertEquals(170, ship1000.getnCabins4());
        Assert.assertEquals(300, ship1000.getnParkingSlots());
        Assert.assertEquals(30, ship1000.getUnLoadTimeInMinutes());

        Assert.assertEquals(8, this.theShippingLine.numShips());

        this.theShippingLine.addShip("shipId200", "Palermo", 300, 150, 170, 300, 30);
        Assert.assertEquals(9, this.theShippingLine.numShips());

    }


    @Test
    public void addRouteTest() throws DSException{

        Assert.assertThrows(SrcPortNotFoundException.class, () ->
                this.theShippingLine.addRoute("routeId1000", "XXXX", "YYYYY", 100));

        Assert.assertThrows(DstPortNotFoundException.class, () ->
                this.theShippingLine.addRoute("routeId1000", "MLG", "YYYYY", 100));

        this.theShippingLine.addRoute("routeId1000", "EST", "MAL", 1404);
        Route route1000 = this.theShippingLine.getRoute("routeId1000");
        Assert.assertEquals("Estambul", route1000.getSrcPort().getName());
        Assert.assertEquals("Malta", route1000.getDstPort().getName());

        Assert.assertEquals(16, this.theShippingLine.numRoutes());

        this.theShippingLine.addRoute("routeId1000", "GEN", "TUN", 587);
        route1000 = this.theShippingLine.getRoute("routeId1000");
        Assert.assertEquals("Génova", route1000.getSrcPort().getName());
        Assert.assertEquals("Túnez", route1000.getDstPort().getName());

        Assert.assertEquals(16, this.theShippingLine.numRoutes());

        this.theShippingLine.addRoute("routeId2000", "ATH", "NAP", 1291);

        Assert.assertEquals(17, this.theShippingLine.numRoutes());

        Assert.assertThrows(RouteAlreadyExistException.class, () ->
                this.theShippingLine.addRoute("routeId3000", "ATH", "NAP", 1291));

    }


    @Test
    public void addClientTest() {

        theShippingLine.addClient("clientId1000", "XXX", "YYY");
        Client clientId1000 = this.theShippingLine.getClient("clientId1000");
        Assert.assertEquals("XXX", clientId1000.getName());
        Assert.assertEquals("YYY", clientId1000.getSurname());

        Assert.assertEquals(19, this.theShippingLine.numClients());

        theShippingLine.addClient("clientId1000", "Pepet", "I Marieta");
        clientId1000 = this.theShippingLine.getClient("clientId1000");
        Assert.assertEquals("Pepet", clientId1000.getName());
        Assert.assertEquals("I Marieta", clientId1000.getSurname());

        Assert.assertEquals(19, this.theShippingLine.numClients());

        theShippingLine.addClient("clientId2000", "Xeic", "Cugat");

        Assert.assertEquals(20, this.theShippingLine.numClients());
    }

    @Test
    public void addVoyageTest() throws DSException {

        Assert.assertThrows(ShipNotFoundException.class, () ->
        theShippingLine.addVoyage("voyageId1000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "XXXX", "routeId1"));

        Assert.assertThrows(RouteNotFoundException.class, () ->
                theShippingLine.addVoyage("voyageId1000", DateUtils.createDate("30-07-2024 22:50:00"),
                        DateUtils.createDate("31-07-2024 15:50:00"), "shipId1", "xxxxx"));

        theShippingLine.addVoyage("voyageId2000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId1", "routeId5");
        Voyage voyage2000 = theShippingLine.getVoyage("voyageId2000");
        Assert.assertEquals("shipId1", voyage2000.getShip().getId());
        Assert.assertEquals("routeId5", voyage2000.getRoute().getId());

        Assert.assertEquals(4, theShippingLine.numVoyages());

        theShippingLine.addVoyage("voyageId2000", DateUtils.createDate("30-07-2024 22:50:00"),
                DateUtils.createDate("31-07-2024 15:50:00"), "shipId1", "routeId1");
        voyage2000 = theShippingLine.getVoyage("voyageId2000");
        Assert.assertEquals("shipId1", voyage2000.getShip().getId());
        Assert.assertEquals("routeId1", voyage2000.getRoute().getId());
        Assert.assertEquals(4, theShippingLine.numVoyages());
    }

    @Test
    public void reserveTest() throws DSException {
        String[] clientsA = {"clientId1" ,"clientId2", "clientId3", "clientId4", "clientId5", "clientId6"};
        Assert.assertThrows(VoyageNotFoundException.class, () ->
            theShippingLine.reserve(clientsA, "voyageIdXXXX", ShippingLine.AccommodationType.ARMCHAIR,null,200 ));

        String[] clientsX = {"clientId1" ,"clientId99999", "clientId3", "clientId4"};
        Assert.assertThrows(ClientNotFoundException.class, () ->
                theShippingLine.reserve(clientsX, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR,null,200 ));

        Assert.assertThrows(MaxExceededException.class, () ->
                theShippingLine.reserve(clientsA, "voyageId1", ShippingLine.AccommodationType.CABIN2,null,200 ));

        String[] clientsY = {"clientId1" ,"clientId2", "clientId3", "clientId4", "clientId5", "clientId6"};
        Assert.assertThrows(MaxExceededException.class, () ->
                theShippingLine.reserve(clientsY, "voyageId1", ShippingLine.AccommodationType.CABIN4,null,200 ));


        Ship ship4 = this.theShippingLine.getShip("shipId4");
        Assert.assertEquals(10, ship4.getnArmChairs());
        Assert.assertEquals(2, ship4.getnCabins2());
        Assert.assertEquals(12, ship4.getnCabins4());
        Assert.assertEquals(12, ship4.getnParkingSlots());


        Voyage voyage1 = theShippingLine.getVoyage("voyageId1");
        Assert.assertEquals(10, voyage1.getAvailableArmChairs());
        Assert.assertEquals(2, voyage1.getAvailableCabin2());
        Assert.assertEquals(12, voyage1.getAvailableCabin4());
        Assert.assertEquals(12, voyage1.getAvailableParkingSlots());


        theShippingLine.reserve(clientsA, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR,"2251VV",200 );
        Assert.assertEquals(4, voyage1.getAvailableArmChairs());
        Assert.assertEquals(2, voyage1.getAvailableCabin2());
        Assert.assertEquals(12, voyage1.getAvailableCabin4());
        Assert.assertEquals(11, voyage1.getAvailableParkingSlots());

        // Client idClient1 already has a reservation
        String[] clientsAx = {"clientId7" ,"clientId1", "clientId10", "clientId11"};
        Assert.assertThrows(ReservationAlreadyExistsException.class, () ->
          theShippingLine.reserve(clientsAx, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR,null,200 ));

        String[] clientsB = {"clientId7" ,"clientId8"};
        theShippingLine.reserve(clientsB, "voyageId1", ShippingLine.AccommodationType.CABIN2,"B1923AT",200 );
        Assert.assertEquals(4, voyage1.getAvailableArmChairs());
        Assert.assertEquals(1, voyage1.getAvailableCabin2());
        Assert.assertEquals(12, voyage1.getAvailableCabin4());
        Assert.assertEquals(10, voyage1.getAvailableParkingSlots());

        String[] clientsC = {"clientId9" ,"clientId10", "clientId11", "clientId12", "clientId13"};
        Assert.assertThrows(NoAcommodationAvailableException.class, () ->
                theShippingLine.reserve(clientsC, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR,null,200 ));

        Assert.assertEquals(4, voyage1.getAvailableArmChairs());
        Assert.assertEquals(1, voyage1.getAvailableCabin2());
        Assert.assertEquals(12, voyage1.getAvailableCabin4());
        Assert.assertEquals(10, voyage1.getAvailableParkingSlots());

        String[] clientsD = {"clientId9" ,"clientId10", "clientId11", "clientId12"};
        theShippingLine.reserve(clientsD, "voyageId1", ShippingLine.AccommodationType.ARMCHAIR,null,200 );
        Assert.assertEquals(0, voyage1.getAvailableArmChairs());
        Assert.assertEquals(1, voyage1.getAvailableCabin2());
        Assert.assertEquals(12, voyage1.getAvailableCabin4());
        Assert.assertEquals(10, voyage1.getAvailableParkingSlots());

        String[] clientsE = {"clientId13", "clientId14"};
        theShippingLine.reserve(clientsE, "voyageId1", ShippingLine.AccommodationType.CABIN2,null,200 );
        Assert.assertEquals(0, voyage1.getAvailableArmChairs());
        Assert.assertEquals(0, voyage1.getAvailableCabin2());
        Assert.assertEquals(12, voyage1.getAvailableCabin4());
        Assert.assertEquals(10, voyage1.getAvailableParkingSlots());

        String[] clientsF = {"clientId15", "clientId16"};
        Assert.assertThrows(NoAcommodationAvailableException.class, () ->
            theShippingLine.reserve(clientsF, "voyageId1", ShippingLine.AccommodationType.CABIN2,null,200 ));

        String[] clientsG = {"clientId15", "clientId16", "clientId17"};
        theShippingLine.reserve(clientsG, "voyageId1", ShippingLine.AccommodationType.CABIN4,"8337GYT",200 );
        Assert.assertEquals(0, voyage1.getAvailableArmChairs());
        Assert.assertEquals(0, voyage1.getAvailableCabin2());
        Assert.assertEquals(11, voyage1.getAvailableCabin4());
        Assert.assertEquals(9, voyage1.getAvailableParkingSlots());

        Client client5 = theShippingLine.getClient("clientId15");
        Iterator<Reservation> it = client5.reservations();
        Assert.assertTrue(it.hasNext());
        Reservation reservation = it.next();
        Assert.assertEquals("voyageId1", reservation.getVoyage().getId());
        Assert.assertEquals(3, reservation.numClients());
        Assert.assertTrue(reservation.hasParkingLot());
        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void loadTest() throws DSException {

        Assert.assertThrows(ClientNotFoundException.class, () ->
                theShippingLine.load("clientIdXX", "voyageId1", DateUtils.createDate("30-07-2024 08:50:00")));

        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.load("clientId1", "voyageIdXX", DateUtils.createDate("30-07-2024 08:50:00")));

        Assert.assertThrows(ReservationNotFoundException.class, () ->
                theShippingLine.load("clientId18", "voyageId1", DateUtils.createDate("30-07-2024 08:50:00")));

        reserveTest();
        theShippingLine.load("clientId15", "voyageId1", DateUtils.createDate("30-07-2024 08:50:00"));

        Assert.assertThrows(LoadingAlreadyException.class, () ->
                theShippingLine.load("clientId15", "voyageId1", DateUtils.createDate("30-07-2024 08:55:00")));


        theShippingLine.load("clientId7", "voyageId1", DateUtils.createDate("30-07-2024 09:30:00"));
        theShippingLine.load("clientId1", "voyageId1", DateUtils.createDate("30-07-2024 22:05:00"));

        Voyage voyage = theShippingLine.getVoyage("voyageId1");
        Assert.assertEquals(3, voyage.numParkingLots());

    }

    @Test
    public void unLoadTest() throws DSException {
        loadTest();
        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.unload("voyageIdXXX"));

        Iterator<Reservation> it = theShippingLine.unload("voyageId1");

        ParkingReservation reservation = null;

        Assert.assertTrue(it.hasNext());
        reservation = (ParkingReservation)it.next();
        Assert.assertEquals("2251VV", reservation.getIdVehicle());
        Assert.assertEquals(7, reservation.getUnLoadTimeInMinutes());


        reservation = (ParkingReservation)it.next();
        Assert.assertEquals("B1923AT", reservation.getIdVehicle());
        Assert.assertEquals(14, reservation.getUnLoadTimeInMinutes());

        reservation = (ParkingReservation)it.next();
        Assert.assertEquals("8337GYT", reservation.getIdVehicle());
        Assert.assertEquals(21, reservation.getUnLoadTimeInMinutes());

        Assert.assertFalse(it.hasNext());

    }

    @Test
    public void unLoadTimeTest() throws DSException {
        Assert.assertThrows(VoyageNotFoundException.class, () ->
                theShippingLine.unloadTime("2251VV","voyageIdXXX"));

        Assert.assertThrows(LandingNotDoneException.class, () ->
                theShippingLine.unloadTime("2251VV","voyageId1"));

        unLoadTest();

        Assert.assertThrows(VehicleNotFoundException.class, () ->
                theShippingLine.unloadTime("999VV","voyageId1"));

        Assert.assertEquals(7, theShippingLine.unloadTime("2251VV","voyageId1"));
        Assert.assertEquals(14, theShippingLine.unloadTime("B1923AT","voyageId1"));
        Assert.assertEquals(21, theShippingLine.unloadTime("8337GYT","voyageId1"));


    }


    @Test
    public void getClientReservationsTest() throws DSException  {
        Assert.assertThrows(NoReservationException.class, () ->
                theShippingLine.getClientReservations("clientId1"));

        reserveTest();
        Iterator<Reservation> it = theShippingLine.getClientReservations("clientId1");
        Assert.assertTrue(it.hasNext());
        Reservation reservation = it.next();
        Assert.assertEquals("voyageId1", reservation.getVoyage().getId());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getVoyageReservationsTest() throws DSException  {
        Assert.assertThrows(NoReservationException.class, () ->
                theShippingLine.getVoyageReservations("voyageId1"));

        reserveTest();
        Reservation reservation;
        Iterator<Reservation> it = theShippingLine.getVoyageReservations("voyageId1");
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        Client client = null;
        Iterator<Client> clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId1", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId2", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId3", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId4", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId5", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId6", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.CABIN2, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId7", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId8", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId9", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId10", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId11", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId12", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.CABIN2, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId13", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId14", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.CABIN4, reservation.getAccommodationType());
        Assert.assertTrue(reservation.hasParkingLot());
        Assert.assertEquals("8337GYT", reservation.getIdVehicle());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId15", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId16", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId17", client.getId());
        Assert.assertFalse(clientsIt.hasNext());


        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getAccommodationReservationsTest() throws DSException {
        Assert.assertThrows(NoReservationException.class, () ->
                theShippingLine.getAccommodationReservations("voyageId1", ShippingLine.AccommodationType.ARMCHAIR));

        reserveTest();
        Reservation reservation;
        Client client=null;
        Iterator<Client> clientsIt;
        Iterator<Reservation> it = theShippingLine.getAccommodationReservations("voyageId1", ShippingLine.AccommodationType.ARMCHAIR);
        Assert.assertTrue(it.hasNext());
        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId1", client.getId());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId2", client.getId());
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId3", client.getId());
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId4", client.getId());
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId5", client.getId());
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId6", client.getId());
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId9", client.getId());
        Assert.assertTrue(it.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.ARMCHAIR, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId10", client.getId());
        Assert.assertTrue(it.hasNext());
        /// ...

        it = theShippingLine.getAccommodationReservations("voyageId1", ShippingLine.AccommodationType.CABIN2);
        Assert.assertTrue(it.hasNext());
        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.CABIN2, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId7", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId8", client.getId());
        Assert.assertFalse(clientsIt.hasNext());

        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.CABIN2, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId13", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId14", client.getId());
        Assert.assertFalse(clientsIt.hasNext());
        Assert.assertFalse(it.hasNext());


        it = theShippingLine.getAccommodationReservations("voyageId1", ShippingLine.AccommodationType.CABIN4);
        Assert.assertTrue(it.hasNext());
        reservation = it.next();
        Assert.assertEquals(ShippingLine.AccommodationType.CABIN4, reservation.getAccommodationType());
        client = null;
        clientsIt = reservation.clients();
        Assert.assertTrue(clientsIt.hasNext());
        client = clientsIt.next();
        Assert.assertEquals("clientId15", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId16", client.getId());
        client = clientsIt.next();
        Assert.assertEquals("clientId17", client.getId());
        Assert.assertFalse(clientsIt.hasNext());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void getMostTravelerClientTest() throws DSException {

        Assert.assertThrows(NoClientException.class, () ->
                theShippingLine.getMostTravelerClient());

        unLoadTest();
        Client client = theShippingLine.getMostTravelerClient();

        Assert.assertEquals("clientId15", client.getId());
    }

    @Test
    public void getMostTraveledRouteTest() throws DSException {
        addVoyageTest();

        Route route = theShippingLine.getMostTraveledRoute();
        Assert.assertEquals("routeId2", route.getId());
        Assert.assertEquals("Civitavecchia-Túnez", route.toString());
        Assert.assertEquals(2, route.numVoyages());

        theShippingLine.addVoyage("voyageId9000", DateUtils.createDate("30-08-2024 22:50:00"),
                DateUtils.createDate("31-09-2024 15:50:00"), "shipId3", "routeId1");

        theShippingLine.addVoyage("voyageId900q", DateUtils.createDate("30-09-2024 22:50:00"),
                DateUtils.createDate("31-09-2024 15:50:00"), "shipId3", "routeId1");

        route = theShippingLine.getMostTraveledRoute();
        Assert.assertEquals("routeId1", route.getId());
        Assert.assertEquals("Atenas-Civitavecchia", route.toString());
        Assert.assertEquals(4, route.numVoyages());

    }
}