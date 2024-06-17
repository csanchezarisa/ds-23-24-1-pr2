package uoc.ds.pr.model;

import edu.uoc.ds.adt.nonlinear.Dictionary;
import edu.uoc.ds.adt.nonlinear.DictionaryAVLImpl;
import edu.uoc.ds.traversal.Iterator;

import java.util.Objects;

public class Category {

    private String id;
    private String name;
    private final Dictionary<String, Product> products;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
        products = new DictionaryAVLImpl<>();
    }

    public Category(String id) {
        this.id = id;
        this.name = null;
        products = new DictionaryAVLImpl<>();
    }

    public void update(String name) {
        this.name = name;
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

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void deleteProduct(String productId) {
        products.delete(productId);
    }

    public int numProducts() {
        return products.size();
    }

    public Iterator<Product> products() {
        return products.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
