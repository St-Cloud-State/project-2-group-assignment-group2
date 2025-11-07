import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class WaitlistItemOnly implements Serializable {
    private static final long serialVersionUID = 1L;
    private Client client;
    private List<WaitlistItem> items = new LinkedList<>();

    //Constructor to create a waitlist for a specific client.
    public WaitlistItemOnly(Client client) {
        this.client = client;
    }

    //Adds a product to the client's waitlist with specified quantity
    public void addProduct(Product product, int quantity) {
        items.add(new WaitlistItem(product, quantity));
    }

    //Removes a waitlist item for a product using its ID.
    public boolean removeProduct(String productId) {
        Iterator<WaitlistItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            WaitlistItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    //Returns a list of waitlist items.
    public List<WaitlistItem> getWaitlist() {
        return new ArrayList<>(items);
    }

    //Gets all products in the waitlist.
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        for (WaitlistItem item : items) {
            products.add(item.getProduct());
        }
        return products;
    }

    
    //Gets the client associated with this waitlist.
    public Client getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "Waitlist for client: " + client.getName() + " with " + items.size() + " item(s).";
    }
}