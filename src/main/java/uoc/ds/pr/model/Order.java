package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.List;

public class Order {

    private Client client;
    private Voyage voyage;
    private double price;
    private List<Product> products;
    private long creation = System.nanoTime();

    public Order(Client client, Voyage voyage, double price, List<Product> products) {
        this.client = client;
        this.voyage = voyage;
        this.price = price;
        this.products = products;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public long getCreation() {
        return creation;
    }

    @Override
    public String toString() {
        return "Order{" +
                "products=" + products.size() +
                ", price=" + price +
                ", voyage=" + voyage.getId() +
                ", client=" + client.getId() +
                '}';
    }
}
