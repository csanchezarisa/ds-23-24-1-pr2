package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;

import java.util.Comparator;

public class Client implements Comparable<Client> {

    public static final Comparator<Client> CMP = (c1, c2) -> c1.getId().compareTo(c2.getId());
    public static final Comparator<Client> CMP_V = (c1, c2)->Double.compare(c1.voyages.size(), c2.voyages.size());
    private String id;
    private String name;
    private String surname;

    private List<Reservation> reservations;
    private List<Voyage> voyages;


    public Client(String id, String name, String surname) {
        this(id);
        this.name = name;
        this.surname = surname;
        this.reservations = new LinkedList<>();
        this.voyages = new LinkedList<>();
    }

    public Client(String id) {
        this.id = id;
    }

    public void update(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean hasReservation(Voyage voyage) {
        Iterator<Reservation> it = reservations.values();
        boolean found = false;
        Reservation reserve  = null;
        while (!found && it.hasNext()) {
            reserve = it.next();
            found = reserve.getVoyage().equals(voyage);

        }
        return found;
    }

    public void addReservation(Reservation reserve) {
        reservations.insertEnd(reserve);
    }

    public Iterator<Reservation> reservations() {
        return reservations.values();
    }

    public Reservation findReservation(String idVoyage) {
        Iterator<Reservation> it = reservations();
        boolean found = false;
        Reservation reservation = null;
        while (!found && it.hasNext()) {
            reservation = it.next();
            found = reservation.getVoyage().getId().equals(idVoyage);
        }

        return (found?reservation:null);
    }

    public void addVoyage(Voyage voyage) {
        voyages.insertEnd(voyage);
    }

    @Override
    public int compareTo(Client o) {
        return this.id.compareTo(o.id);
    }
}
