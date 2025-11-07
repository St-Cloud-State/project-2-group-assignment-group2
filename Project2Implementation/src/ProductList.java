import java.util.*;
import java.io.*;
//java updated mid code and crashed somehow making file different
public class ProductList implements Serializable {
    private String id;
    private String name;
    private double price;
    private String description;
    private boolean available;

    public ProductList(String id, String name, double price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String toString() {
        return "ProductList [id=" + id + ", name=" + name + ", price=" + price + 
               ", description=" + description + ", available=" + available + "]";
    }
}
