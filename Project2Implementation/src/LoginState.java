public class LoginState implements State {
    private final WarehouseContext context;

    public LoginState(WarehouseContext context) {
        this.context = context;
    }

    @Override
    public Event run() {
        System.out.println("\n=== Warehouse Login Menu ===");
        System.out.println("1. Login as Manager");
        System.out.println("2. Login as Clerk");
        System.out.println("3. Login as Client");
        System.out.println("0. Exit");

        String choice = context.prompt("Select an option: ");

        switch (choice) {
            case "1":
                return Event.TO_MANAGER;
            case "2":
                return Event.TO_CLERK;
            case "3":
                return handleClientLogin();
            case "0":
                return Event.EXIT;
            default:
                System.out.println("Invalid selection. Please try again.");
                return Event.STAY;
        }
    }

    private Event handleClientLogin() {
        String clientId = context.prompt("Enter client ID: ");
        if (context.setActiveClient(clientId)) {
            return Event.PUSH_CLIENT;
        }
        System.out.println("Client not found. Please check the client identifier.");
        return Event.STAY;
    }

    @Override
    public String getName() {
        return "LoginState";
    }
}
