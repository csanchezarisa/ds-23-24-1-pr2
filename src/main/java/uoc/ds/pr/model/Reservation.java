package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;


public class Reservation {
    private List<Client> clients;

    private Voyage voyage;
    private ShippingLine.AccommodationType accommodationType;
    private String idVehicle;

    private boolean loaded;
    private int unLoadTimeInMinutes;

    public Reservation(Voyage voyage) {
        this.idVehicle = null;
        this.voyage = voyage;
        this.loaded = false;
    }
    public Reservation(Voyage voyage, List<Client> clientLinkedList, ShippingLine.AccommodationType accommodationType, double price) {
        this(voyage);
        clients = new LinkedList<>();
        addClients(clientLinkedList);
        this.accommodationType = accommodationType;
    }

    public Reservation(Voyage voyage, Client client, ShippingLine.AccommodationType accommodationType, double price ) {
        this(voyage);
        clients = new LinkedList<>();
        clients.insertEnd(client);
        this.accommodationType = accommodationType;
    }

    private void addClients(List<Client> clientLinkedList) {
        clients.insertAll(clientLinkedList);
    }

    public ShippingLine.AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public int numClients() {
        return clients.size();
    }

    public boolean hasParkingLot() {
        return idVehicle!=null;
    }


    public String getIdVehicle() {
        return this.idVehicle;
    }

    public void setIdVehicle(String idVehicle) {
        this.idVehicle = idVehicle;
    }

    public void addParkingReserve(String idVehicle) {
        this.idVehicle = idVehicle;
    }


    public void loaded() {
        this.loaded = true;
    }

    public void loadVehicle() {
        voyage.addParkingReservation(idVehicle);
    }

    public int getUnLoadTimeInMinutes() {
        return unLoadTimeInMinutes;
    }

    public void setUnLoadTimeInMinutes(int unLoadTimeInMinutes) {
        this.unLoadTimeInMinutes = unLoadTimeInMinutes;
    }

    public Iterator<Client> clients() {
        return clients.values();
    }

    public boolean isLoaded() {
        return loaded;
    }
}
