package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.PriorityQueue;
import edu.uoc.ds.adt.sequential.*;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.util.FiniteLinkedList;
import uoc.ds.pr.util.LoyaltyLevel;
import uoc.ds.pr.util.Utils;

import java.util.Comparator;
import java.util.Date;

public class Voyage {
    public static final Comparator<Voyage> CMP = (o1, o2) -> o1.getId().compareTo(o2.getId());
    public static final Comparator<Order> CMP_LEVEL = Comparator.comparingInt((Order o) -> LoyaltyLevel.getLevelValue(o.getClient().getLevel()))
            .thenComparing(Order::getCreated);

    private String id;
    private Date departureDt;
    private Date arrivalDt;
    private Ship ship;
    private Route route;

    private List<Reservation> reservations;
    private FiniteLinkedList<Reservation> armChairs;
    private FiniteLinkedList<Reservation> cabin2;
    private FiniteLinkedList<Reservation> cabin4;
    private Stack<Reservation> parking;
    private List<Order> servedOrders;
    private Queue<Order> pendingOrders;

    private int availableParkingSlots;

    public Voyage(String id) {
        this.setId(id);
    }


    public Voyage(String id, Date departureDt, Date arrivalDt, Ship ship, Route route) {
        this(id);
        this.setDepartureDt(departureDt);
        this.setArrivalDt(arrivalDt);
        this.setShip(ship);
        this.setRoute(route);
        armChairs = new FiniteLinkedList<>(ship.getnArmChairs());
        cabin2 = new FiniteLinkedList<>(ship.getnCabins2());
        cabin4 = new FiniteLinkedList<>(ship.getnCabins4());
        parking = new StackArrayImpl<>(ship.getnParkingSlots());
        this.availableParkingSlots = ship.getnParkingSlots();
        reservations = new LinkedList<>();
        this.servedOrders = new LinkedList<>();
        this.pendingOrders = new PriorityQueue<>(CMP_LEVEL);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getDepartureDt() {
        return departureDt;
    }

    public void setDepartureDt(Date departureDt) {
        this.departureDt = departureDt;
    }

    public Date getArrivalDt() {
        return arrivalDt;
    }

    public void setArrivalDt(Date arrivalDt) {
        this.arrivalDt = arrivalDt;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public boolean parkingFull() {
        return availableParkingSlots <=0; //((FiniteContainer) parking).isFull();
    }

    public boolean isAcommodationAvailable(ShippingLine.AccommodationType accommodationType, int num) {
        boolean isAcoomodationAvailable = true;

        switch (accommodationType) {
            case ARMCHAIR:
                isAcoomodationAvailable = isAvailable(armChairs, num);
                break;
            case CABIN2:
                isAcoomodationAvailable = isAvailable(cabin2, 1);
                break;
            case CABIN4:
                isAcoomodationAvailable = isAvailable(cabin4, 1);
                break;
        }
        return isAcoomodationAvailable;

    }

    private boolean isAvailable(FiniteLinkedList<Reservation> acommodation, int num) {
        return (acommodation.size()+num<=acommodation.length());
    }

    public void addReservation(Reservation reservation) {
        switch (reservation.getAccommodationType()) {
            case ARMCHAIR:
                armChairs.insertEnd(reservation);
                break;
            case CABIN2:
                cabin2.insertEnd(reservation);
                break;
            case CABIN4:
                cabin4.insertEnd(reservation);
                break;
        }
        reservations.insertEnd(reservation);
    }

    public void update(Date departureDt, Date arrivalDt, Ship ship, Route route) {
        this.departureDt = departureDt;
        this.arrivalDt = arrivalDt;
        this.ship = ship;
        this.route = route;
    }

    public int getAvailableArmChairs() {
        return ship.getnArmChairs() - armChairs.size();
    }

    public int getAvailableCabin2() {
        return ship.getnCabins2() - cabin2.size();
    }

    public int getAvailableCabin4() {
        return ship.getnCabins4() - cabin4.size();
    }

    public int getAvailableParkingSlots() {
        return availableParkingSlots; //ship.getnParkingSlots() - parking.size();
    }

    public void addReservation(List<Client> clientLinkedList, ShippingLine.AccommodationType accommodationType,
                               double price, String idVehicle) {
        Client client = null;
        Reservation reservation = null;
        if (accommodationType == ShippingLine.AccommodationType.ARMCHAIR ) {
            Iterator<Client> it = clientLinkedList.values();
            while (it.hasNext()) {
                client = it.next();
                reservation = new Reservation(this, client, accommodationType, price);
                addReservation(reservation);
                client.addReservation(reservation);
                if (idVehicle!=null) {
                    reservation.addParkingReserve(idVehicle);
                }
            }
        }
        else {
            reservation = new Reservation(this, clientLinkedList, accommodationType, price);
            addReservation(reservation);
            if (idVehicle!=null) {
                reservation.addParkingReserve(idVehicle);
            }
            Iterator<Client> it = clientLinkedList.values();
            while (it.hasNext()) {
                client = it.next();
                client.addReservation(reservation);
            }
        }
        if (idVehicle!=null) {
            this.availableParkingSlots--;
        }
    }

    public boolean equals(Voyage voyage) {
        return this.id.equals(voyage.id);
    }

    public void addParkingReservation(String idVehicle) {
        parking.push(new ParkingReservation(this, idVehicle));
    }

    public Iterator<Reservation> parking() {
        return parking.values();
    }

    public Iterator<Reservation> reservations() {
        return reservations.values();
    }

    public Iterator<Reservation> reservations(ShippingLine.AccommodationType accommodationType) {
        FiniteLinkedList<Reservation> reservations = null;
        switch (accommodationType) {
            case ARMCHAIR:
                reservations = armChairs;
                break;
            case CABIN2:
                reservations = cabin2;
                break;
            case CABIN4:
                reservations = cabin4;
                break;
        }
        return reservations.values();
    }

    public int numParkingLots() {
        return this.parking.size();
    }

    public void addOrder(Order order) {
        pendingOrders.add(order);
    }

    public boolean arePendingOrders() {
        return !pendingOrders.isEmpty();
    }

    public Order serveOrder() {
        Order order = pendingOrders.poll();
        servedOrders.insertEnd(order);
        return order;
    }

    public boolean isProductAvailable(Product product) {
        return Utils.anyMatch(ship.products(), p -> p.equals(product));
    }
}
