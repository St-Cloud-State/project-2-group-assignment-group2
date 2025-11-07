import java.util.List;

public class ManagerMenuState implements State {
    private final WarehouseContext context;

    public ManagerMenuState(WarehouseContext context) {
        this.context = context;
    }

    @Override
    public Event run() {
        // Manager mode:
        System.out.println("\n=== Manager Menu ===");
        System.out.println("1. Add a product");
        System.out.println("2. Display product waitlist");
        System.out.println("3. Receive a shipment");
        System.out.println("4. Become a clerk");
        System.out.println("5. Logout");

        String choice = context.prompt("Select an option: ");

        switch (choice) {
            case "1":
                addProduct();
                return Event.STAY;
            case "2":
                displayProductWaitlist();
                return Event.STAY;
            case "3":
                receiveShipment();
                return Event.STAY;
            case "4":
                return Event.TO_CLERK;
            case "5":
                return Event.LOGOUT;
            default:
                System.out.println("Invalid selection. Please try again.");
                return Event.STAY;
        }
    }

    private void addProduct() {
        String id = context.prompt("Enter product ID: ");
        String name = context.prompt("Enter product name: ");
        double price = context.promptDouble("Enter product price: ");
        int quantity = context.promptInt("Enter product quantity: ");

        Product product = new Product(id, name, price, quantity);
        context.getWarehouse().addProduct(product);
        System.out.println("Product added successfully.");
    }

    private void displayProductWaitlist() {
        String productId = context.prompt("Enter product ID: ");
        Product product = context.getWarehouse().findProduct(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        List<WaitlistItem> waitlist = product.getWaitlist();
        if (waitlist.isEmpty()) {
            System.out.println("No waitlist entries for this product.");
            return;
        }

        System.out.println("Waitlist for product " + product.getName() + ":");
        for (WaitlistItem item : waitlist) {
            System.out.println(item);
        }
    }

    private void receiveShipment() {
        String productId = context.prompt("Enter product ID: ");
        int quantity = context.promptInt("Enter quantity received: ");

        context.getWarehouse().addProductQuantity(productId, quantity);
        System.out.println("Shipment processed successfully.");
    }

    @Override
    public String getName() {
        return "ManagerMenuState";
    }
}
