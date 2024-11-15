package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.util.Utils;

import java.util.Objects;

public class Port {

    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private List<Route> originRoutes;

    public Port(String id, String imageUrl, String description, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.name = name;
        this.originRoutes = new LinkedList<>();
    }

    public void update(String imageUrl, String description, String name) {
        setImageUrl(imageUrl);
        setDescription(description);
        setName(name);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Port port)) return false;
        return Objects.equals(name, port.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public void addRoute(Route route) {
        originRoutes.insertEnd(route);
    }

    public void removeRoute(Route route) {
        Utils.find(originRoutes.positions(), r -> r.equals(route))
                .ifPresent(originRoutes::delete);
    }

    public Iterator<Route> routesOrigin() {
        return originRoutes.values();
    }
}
