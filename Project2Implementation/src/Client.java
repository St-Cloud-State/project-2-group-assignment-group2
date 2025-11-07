import java.util.*;
import java.io.*;
// Could have probably done this better but don't want to change it

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientId;
    private String name;
    private String email;
    private String phone;
    private double balance;
    private List<WishlistItem> wishlist = new LinkedList<>();
    private List<WaitlistItem> waitlist = new LinkedList<>();

    public Client(String clientId, String name, String email, String phone) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.balance = 0.0;
    }

    public void addWishlistItem(WishlistItem item) {
        wishlist.add(item);
    }
    
    public void addWishlistItem(Product product, int quantity) {
        wishlist.add(new WishlistItem(product, quantity));
    }
    
    public void clearWishlist() {
        wishlist.clear();
    }

    public boolean removeWishlistItem(String productId) {
        Iterator<WishlistItem> items = wishlist.iterator();
        while (items.hasNext()) {
            WishlistItem item = items.next();
            if (item.getProduct().getId().equals(productId)) {
                items.remove();
                return true;
            }
        }
        return false;
    }

    public void updateBalance(double amount) {
        this.balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public List<WishlistItem> getWishlist() {
        return new ArrayList<>(wishlist);
    }

    public void addWaitlistItem(Product product, int quantity) {
        waitlist.add(new WaitlistItem(product, quantity));
    }

    public List<WaitlistItem> getWaitlist() {
        return new ArrayList<>(waitlist);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return clientId;
    }

    public String toString() {
        return "Client id=" + clientId + " name=" + name + " email=" + email + " phone=" + phone;
    }
}
