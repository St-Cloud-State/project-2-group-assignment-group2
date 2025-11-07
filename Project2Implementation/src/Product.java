import java.io.*;
import java.util.*;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private double price;
    private int quantity;
    private List<WaitlistItem> waitlist = new ArrayList<>();

    public Product(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public boolean removeQuantity(int amount) {
        if (amount <= quantity) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    public void addToWaitlist(WaitlistItem item) {
        waitlist.add(item);
    }

    public void removeFromWaitlist(WaitlistItem item) {
        waitlist.remove(item);
    }

    public void clearWaitlist() {
        waitlist.clear();
    }

    public List<WaitlistItem> getWaitlist() {
        return new ArrayList<>(waitlist);
    }

    public String toString() {
        return "Product id=" + id + " name=" + name + " price=" + price + " quantity=" + quantity;
    }
}
