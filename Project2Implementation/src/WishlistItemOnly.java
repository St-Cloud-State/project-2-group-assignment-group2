import java.util.*;
import java.io.*;

public class WishlistItemOnly implements Serializable {
    private Client client;
    private List<ProductList> products;

    public WishlistItemOnly(Client client) {
        this.client = client;
        this.products = new LinkedList<ProductList>();
    }

    public void addProduct(ProductList product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
        }
    }

    public boolean removeProduct(String productId) {
        Iterator<ProductList> iterator = products.iterator();
        while (iterator.hasNext()) {
            ProductList product = iterator.next();
            if (product.getId().equals(productId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Iterator<ProductList> getWishlist() {
        return products.iterator();
    }

    public Client getClient() {
        return client;
    }

    public int getWishlistSize() {
        return products.size();
    }

    public String toString() {
        return "WishlistItemOnly [client=" + client.getName() + ", products=" + products.size() + " items]";
    }
}
