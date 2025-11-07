import java.util.List;

public class ClientMenuState implements State {
    private final WarehouseContext context;

    public ClientMenuState(WarehouseContext context) {
        this.context = context;
    }

    @Override
    public Event run() {
        Client client = context.getActiveClient();
        if (client == null) {
            System.out.println("No active client selected. Returning to previous menu.");
            return Event.RETURN;
        }

        System.out.println("\n=== Client Menu (" + client.getName() + ") ===");
        System.out.println("1. Show client details");
        System.out.println("2. Show product catalog");
        System.out.println("3. Show client transactions");
        System.out.println("4. Add item to wishlist");
        System.out.println("5. Display wishlist");
        System.out.println("6. Place an order");
        System.out.println("7. Logout");

        String choice = context.prompt("Select an option: ");

        switch (choice) {
            case "1":
                showClientDetails(client);
                return Event.STAY;
            case "2":
                showProductCatalog();
                return Event.STAY;
            case "3":
                showClientTransactions(client);
                return Event.STAY;
            case "4":
                addItemToWishlist(client);
                return Event.STAY;
            case "5":
                displayWishlist(client);
                return Event.STAY;
            case "6":
                placeOrder(client);
                return Event.STAY;
            case "7":
                return Event.RETURN;
            default:
                System.out.println("Invalid selection. Please try again.");
                return Event.STAY;
        }
    }

    private void showClientDetails(Client client) {
        System.out.println("Client details:");
        System.out.println(client);
        System.out.println("Balance: $" + String.format("%.2f", client.getBalance()));
    }

    private void showProductCatalog() {
        List<Product> products = context.getWarehouse().getProducts();
        if (products.isEmpty()) {
            System.out.println("No products are currently available.");
            return;
        }

        System.out.println("Product catalog:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void showClientTransactions(Client client) {
        List<String> invoices = context.getWarehouse().getInvoices(client.getId());
        if (invoices.isEmpty()) {
            System.out.println("No transactions found for this client.");
            return;
        }

        System.out.println("Transactions:");
        for (String invoice : invoices) {
            System.out.println(invoice);
        }
    }

    private void addItemToWishlist(Client client) {
        String productId = context.prompt("Enter product ID: ");
        Product product = context.getWarehouse().findProduct(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        int quantity = context.promptInt("Enter quantity: ");
        client.addWishlistItem(product, quantity);
        System.out.println("Item added to wishlist.");
    }

    private void displayWishlist(Client client) {
        List<WishlistItem> wishlist = client.getWishlist();
        if (wishlist.isEmpty()) {
            System.out.println("Wishlist is empty.");
            return;
        }

        System.out.println("Wishlist:");
        for (WishlistItem item : wishlist) {
            System.out.println(item);
        }
    }

    private void placeOrder(Client client) {
        boolean fullyProcessed = context.getWarehouse().processOrder(client);
        if (fullyProcessed) {
            System.out.println("Order processed successfully.");
        } else {
            System.out.println("Order partially processed. Some items may remain on the waitlist.");
        }
    }

    @Override
    public String getName() {
        return "ClientMenuState";
    }
}
