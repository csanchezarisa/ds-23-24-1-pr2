package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.ProductNotInMenuException;
import uoc.ds.pr.util.Utils;

public class Ship {

    private String id;
    private String name;
    private int nArmChairs;
    private int nCabins2;
    private int nCabins4;
    private int nParkingLots;
    private final List<Product> products;


    private int unLoadTimeinMinutes;


    public Ship(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingLots, int unLoadTimeinMinutes) {
        setId(id);
        update(name, nArmChairs, nCabins2, nCabins4, nParkingLots, unLoadTimeinMinutes);
        products = new LinkedList<>();
    }


    public void update(String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingLots, int
            unLoadTimeinMinutes) {
        this.setName(name);
        this.setnArmChairs(nArmChairs);
        this.setnCabins2(nCabins2);
        this.setnCabins4(nCabins4);
        this.setnParkingLots(nParkingLots);
        this.setUnLoadTime(unLoadTimeinMinutes);
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnLoadTime(int unLoadTime) {
        this.unLoadTimeinMinutes = unLoadTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUnLoadTimeInMinutes() {
        return unLoadTimeinMinutes;
    }
    public int getnArmChairs() {
        return nArmChairs;
    }

    public void setnArmChairs(int nArmChairs) {
        this.nArmChairs = nArmChairs;
    }

    public int getnCabins2() {
        return nCabins2;
    }

    public void setnCabins2(int nCabins2) {
        this.nCabins2 = nCabins2;
    }

    public int getnCabins4() {
        return nCabins4;
    }

    public void setnCabins4(int nCabins4) {
        this.nCabins4 = nCabins4;
    }

    public int getnParkingSlots() {
        return nParkingLots;
    }

    public void setnParkingLots(int nParkingLots) {
        this.nParkingLots = nParkingLots;
    }

    public void addProduct(Product product) {
        products.insertEnd(product);
    }

    public void deleteProduct(Product product) throws ProductNotInMenuException {
        Position<Product> pos = Utils.find(products.positions(), p -> p.equals(product))
                        .orElseThrow(ProductNotInMenuException::new);
        products.delete(pos);
    }

    public int numProducts() {
        return products.size();
    }

    public Iterator<Product> products() {
        return products.values();
    }


}