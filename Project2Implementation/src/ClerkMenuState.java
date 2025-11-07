import java.util.List;

public class ClerkMenuState implements State {
    private final WarehouseContext context;

    public ClerkMenuState(WarehouseContext context) {
        this.context = context;
    }

    @Override
    public Event run() {
        // Clerks should rise up on their corporate overlords <-- Ethan was here
        System.out.println("\n=== Clerk Menu ===");
        System.out.println("1. Add a client");
        System.out.println("2. Show product catalog");
        System.out.println("3. Show all clients");
        System.out.println("4. Show clients with outstanding balance");
        System.out.println("5. Record payment from a client");
        System.out.println("6. Become a client");
        System.out.println("7. Logout");

        String choice = context.prompt("Select an option: ");

        switch (choice) {
            case "1":
                addClient();
                return Event.STAY;
            case "2":
                displayAllProducts();
                return Event.STAY;
            case "3":
                displayAllClients();
                return Event.STAY;
            case "4":
                displayClientsWithOutstandingBalance();
                return Event.STAY;
            case "5":
                recordPayment();
                return Event.STAY;
            case "6":
                return becomeClient(); // pretend we're the customer for a moment
            case "7":
                return Event.LOGOUT;
            default:
                System.out.println("Invalid selection. Please try again.");
                return Event.STAY;
        }
    }

    private void addClient() {
        String id = context.prompt("Enter client ID: ");
        String name = context.prompt("Enter client name: ");
        String email = context.prompt("Enter client email: ");
        String phone = context.prompt("Enter client phone: ");

        Client client = new Client(id, name, email, phone);
        context.getWarehouse().addClient(client);
        System.out.println("Client added successfully.");
    }

    private void displayAllClients() {
        List<Client> clients = context.getWarehouse().getClients();
        if (clients.isEmpty()) {
            System.out.println("There are no clients registered.");
            return;
        }

        System.out.println("Registered clients:");
        for (Client client : clients) {
            System.out.println(client + " Balance: $" + String.format("%.2f", client.getBalance()));
        }
    }

    private void displayAllProducts() {
        List<Product> products = context.getWarehouse().getProducts();
        if (products.isEmpty()) {
            System.out.println("There are no products in the catalog.");
            return;
        }

        System.out.println("Products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void displayClientsWithOutstandingBalance() {
        List<Client> clients = context.getWarehouse().getClients();
        boolean found = false;
        for (Client client : clients) {
            if (client.getBalance() > 0.0) {
                if (!found) {
                    System.out.println("Clients with outstanding balance:");
                    found = true;
                }
                System.out.println(client + " Balance: $" + String.format("%.2f", client.getBalance()));
            }
        }
        if (!found) {
            System.out.println("No clients have outstanding balances.");
        }
    }

    private void recordPayment() {
        String clientId = context.prompt("Enter client ID: ");
        Client client = context.findClient(clientId);
        if (client == null) {
            System.out.println("Client not found.");
            return;
        }

        double amount = context.promptDouble("Enter payment amount: ");
        client.updateBalance(-amount);
        System.out.println("Payment recorded. New balance: $" + String.format("%.2f", client.getBalance()));
    }

    private Event becomeClient() {
        String clientId = context.prompt("Enter client ID: ");
        if (context.setActiveClient(clientId)) {
            return Event.PUSH_CLIENT;
        }
        System.out.println("Client not found.");
        return Event.STAY;
    }

    @Override
    public String getName() {
        return "ClerkMenuState";
    }
}
