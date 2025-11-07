import java.util.*;
import java.io.*;

public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private int quantity;
    private Date dateAdded;

    public WishlistItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.dateAdded = new Date();
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public String toString() {
        return "WishlistItem [product=" + product.getName() + 
               ", quantity=" + quantity + ", dateAdded=" + dateAdded + "]";
    }
}
