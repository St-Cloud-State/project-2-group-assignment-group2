import java.util.*;
import java.io.*;

public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String warehouseId;
    private String location;
    private List<Product> products = new LinkedList<>();
    private List<Client> clients = new LinkedList<>();
    private Map<String, List<String>> invoices = new HashMap<>(); // clientId -> list of invoices
    private static Warehouse warehouse;

    private Warehouse(String warehouseId, String location) {
        this.warehouseId = warehouseId;
        this.location = location;
    }

    public static Warehouse instance(String warehouseId, String location) {
        if (warehouse == null) {
            warehouse = new Warehouse(warehouseId, location);
        }
        return warehouse;
    }

    public boolean processOrder(Client client) {
        if (client == null) return false;
        
        List<WishlistItem> wishlist = client.getWishlist();
        boolean orderProcessed = false;
        
        // Keep track of items to remove from wishlist after processing
        List<WishlistItem> itemsToRemove = new ArrayList<>();
        
        for (WishlistItem item : wishlist) {
            Product product = item.getProduct();
            int quantityWanted = item.getQuantity();
            int available = product.getQuantity();
            
            if (available >= quantityWanted) {
                // Fulfill order completely
                if (product.removeQuantity(quantityWanted)) {
                    double cost = quantityWanted * product.getPrice();
                    client.updateBalance(cost);
                    itemsToRemove.add(item);
                    orderProcessed = true;
                    
                    // Generate invoice
                    String invoice = String.format("Invoice for %s: %d x %s at $%.2f each. Total: $%.2f",
                        client.getName(), quantityWanted, product.getName(),
                        product.getPrice(), cost);
                    
                    List<String> clientInvoices = invoices.computeIfAbsent(client.getId(), k -> new ArrayList<>());
                    clientInvoices.add(invoice);
                }
            } else if (available > 0) {
                // Partially fulfill order
                product.removeQuantity(available);
                double cost = available * product.getPrice();
                client.updateBalance(cost);
                orderProcessed = true;
                
                // Generate invoice for partial fulfillment
                String invoice = String.format("Invoice for %s: %d x %s at $%.2f each. Total: $%.2f",
                    client.getName(), available, product.getName(),
                    product.getPrice(), cost);
                
                List<String> clientInvoices = invoices.computeIfAbsent(client.getId(), k -> new ArrayList<>());
                clientInvoices.add(invoice);
                
                // Add unfulfilled quantity to waitlist
                product.addToWaitlist(new WaitlistItem(product, quantityWanted - available, client));
                itemsToRemove.add(item);
            } else {
                // Cannot fulfill any part of this item - add to waitlist
                product.addToWaitlist(new WaitlistItem(product, quantityWanted, client));
                itemsToRemove.add(item);
            }
        }
        
        // Remove processed and waitlisted items from wishlist
        wishlist.removeAll(itemsToRemove);
        
        return orderProcessed;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProduct(String productId) {
        Iterator<Product> iter = products.iterator();
        while (iter.hasNext()) {
            Product p = iter.next();
            if (p.getId().equals(productId)) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public List<Client> getClients() {
        return new ArrayList<>(clients);
    }

    public Product findProduct(String productId) {
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                return p;
            }
        }
        return null;
    }

    public void addProductQuantity(String productId, int quantity) {
        Product product = findProduct(productId);
        if (product != null) {
            // Process waitlist first
            List<WaitlistItem> waitlist = product.getWaitlist();
            List<WaitlistItem> remainingItems = new ArrayList<>();
            int remainingQuantity = quantity;

            for (WaitlistItem item : waitlist) {
                if (remainingQuantity <= 0) {
                    remainingItems.add(item);
                    continue;
                }

                int itemQuantity = item.getQuantity();
                if (remainingQuantity >= itemQuantity) {
                    // Can fulfill this waitlist item completely
                    remainingQuantity -= itemQuantity;
                    
                    // Charge the client and generate invoice
                    Client client = item.getClient();
                    double cost = itemQuantity * product.getPrice();
                    client.updateBalance(cost);
                    
                    String invoice = String.format("Invoice for %s: %d x %s at $%.2f each. Total: $%.2f (Waitlist fulfillment)",
                        client.getName(), itemQuantity, product.getName(),
                        product.getPrice(), cost);
                    
                    List<String> clientInvoices = invoices.computeIfAbsent(client.getId(), k -> new ArrayList<>());
                    clientInvoices.add(invoice);
                } else if (remainingQuantity > 0) {
                    // Can only fulfill part of this waitlist item
                    double cost = remainingQuantity * product.getPrice();
                    Client client = item.getClient();
                    client.updateBalance(cost);
                    
                    String invoice = String.format("Invoice for %s: %d x %s at $%.2f each. Total: $%.2f (Partial waitlist fulfillment)",
                        client.getName(), remainingQuantity, product.getName(),
                        product.getPrice(), cost);
                    
                    List<String> clientInvoices = invoices.computeIfAbsent(client.getId(), k -> new ArrayList<>());
                    clientInvoices.add(invoice);
                    
                    // Add remaining quantity back to waitlist
                    remainingItems.add(new WaitlistItem(product, itemQuantity - remainingQuantity, client));
                    remainingQuantity = 0;
                } else {
                    remainingItems.add(item);
                }
            }

            // Update waitlist with remaining items
            product.clearWaitlist();
            for (WaitlistItem item : remainingItems) {
                product.addToWaitlist(item);
            }

            // Add any remaining quantity to stock
            if (remainingQuantity > 0) {
                product.addQuantity(remainingQuantity);
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (warehouse != null) {
                return;
            } else {
                input.defaultReadObject();
                if (warehouse == null) {
                    warehouse = (Warehouse) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch (IOException ioe) {
            System.out.println("in Warehouse readObject \n" + ioe);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public List<String> getInvoices(String clientId) {
        List<String> clientInvoices = invoices.get(clientId);
        return clientInvoices != null ? new ArrayList<>(clientInvoices) : Collections.emptyList();
    }

    public String toString() {
        return "Warehouse id=" + warehouseId + " location=" + location;
    }
}
