package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Traversal;

import java.util.Comparator;

public class Route implements Comparable<Route> {
    public static final Comparator<Route> CMP_V = (r1, r2) -> Integer.compare(r1.voyages.size(), r2.voyages.size());
    private String id;
    private Port beginningPort;
    private Port arrivalPort;
    private double kms;
    List<Voyage> voyages;


    public Route(String id, Port beginningPort, Port arrivalPort, double kms) {
        this.setId(id);
        this.beginningPort = beginningPort;
        this.arrivalPort = arrivalPort;
        this.kms = kms;
        voyages = new LinkedList<>();
    }


    public void update(Port beginningPort, Port arrivalPort) {
        this.beginningPort = beginningPort;
        this.arrivalPort = arrivalPort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Route o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return this.getSrcPort()+"-"+this.getDstPort();
    }

    public void addVoyage(Voyage voyage) {
        voyages.insertEnd(voyage);
    }

    public int numVoyages() {
        return voyages.size();
    }

    public void remove(Voyage voyage) {
        Traversal<Voyage> traversal = voyages.positions();
        Position<Voyage> p = null;
        while (traversal.hasNext()) {
            p = traversal.next();
            if (p.getElem().getId().equals(voyage.getId())) {
                voyages.delete(p);
            }
        }
    }

    public double getKms() {
        return this.kms;
    }

    public Port getSrcPort() {
        return beginningPort;
    }

    public Port getDstPort() {
        return arrivalPort;
    }
}
