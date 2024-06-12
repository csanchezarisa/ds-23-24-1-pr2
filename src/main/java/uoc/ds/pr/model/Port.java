package uoc.ds.pr.model;

public class Port {

    private String id;
    private String name;
    private String imageUrl;
    private String description;

    public Port(String id, String imageUrl, String description, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
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
}
