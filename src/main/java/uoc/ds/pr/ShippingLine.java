package uoc.ds.pr;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.*;
import uoc.ds.pr.model.*;

import java.util.Date;


public interface ShippingLine {

    public static final int MAX_NUM_SHIPS = 25;
    public static final int MAX_NUM_ROUTES = 15 ;
    public static final int MAX_CLIENTS = 800;

    public static final int MAX_PEOPLE_CABIN2 = 2;
    public static final int MAX_PEOPLE_CABIN4 = 4;


    enum AccommodationType {
        ARMCHAIR,
        CABIN2,
        CABIN4
    }

    public void addShip(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingLots, int
            unLoadTimeinMinutes);

    public void addRoute(String id, String beginningPort, String arrivalPort, double kms) throws SrcPortNotFoundException, DstPortNotFoundException, RouteAlreadyExistException;

    public void addClient(String id, String name, String surname);

    public void addVoyage(String id, Date departureDt, Date arrivalDt, String idShip, String idRoute) throws ShipNotFoundException, RouteNotFoundException;

    public void reserve(String[] clients, String idVoyage, AccommodationType accommodationType, String idVehicle, double price)
            throws ClientNotFoundException, VoyageNotFoundException, ParkingFullException,
            NoAcommodationAvailableException, MaxExceededException, ReservationAlreadyExistsException;

    public void load(String idClient, String idVoyage, Date dt) throws
            LoadingAlreadyException, ClientNotFoundException, VoyageNotFoundException, ReservationNotFoundException;

    public Iterator<Reservation> unload(String idVoyage) throws VoyageNotFoundException;

    public int unloadTime(String idVehicle, String idVoyage) throws LandingNotDoneException,VoyageNotFoundException, VehicleNotFoundException;


    public Iterator<Reservation> getClientReservations(String idClient) throws NoReservationException;

    public Iterator<Reservation> getVoyageReservations(String idVoyage) throws NoReservationException;

    public Iterator<Reservation> getAccommodationReservations(String idVoyage, AccommodationType accommodationType) throws NoReservationException;

    public Client getMostTravelerClient() throws NoClientException;

    public Route getMostTraveledRoute() throws NoRouteException;
    
    /***********************************************************************************/
    /******************** AUX OPERATIONS  **********************************************/
    /***********************************************************************************/


    public Ship getShip(String id);

    public Port getPort(String id);

    public Route getRoute(String idRoute);

    public Client getClient(String id);

    public Voyage getVoyage(String id);

    public int numShips();

    public int numRoutes();

    public int numPorts();

    public int numClients();

    public int numVoyages();
}


