import java.util.ArrayDeque;//more FIFO suport
import java.util.Deque; //makes stacks easier to use, FIFO is for chumps
import java.util.EnumMap; //enum constants as keys
import java.util.List; //why do we even need to import this should be standard
import java.util.Map; //store as pairs of keys and values
import java.util.Scanner; //for primitive types and strings from input

//Ethan was here and i added these comments with love <3

public class WarehouseContext {
    private final Warehouse warehouse;
    private final Scanner scanner;
    private final Map<StateId, State> states = new EnumMap<>(StateId.class);
    private final Deque<StateId> stateHistory = new ArrayDeque<>();

    //Transition matrix, basically a 2D array of StateId objects
    //The first dimension is the current state, the second dimension is the event
    //The value is the next state
    //If the value is null, the state does not transition to any other state
    //If the value is not null, the state transitions to the state in the value
    //This is a very simple way to implement the state machine
    


    //Goodluck with part 2 GUI for this, the code is not very reasonable.


    //lord have mercy, yet it works, marvel of engineering.
    private final StateId[][] transitionMatrix = {
        // Events: STAY,       TO_MANAGER,    TO_CLERK,      PUSH_CLIENT,  LOGOUT,      RETURN,      EXIT
        {StateId.LOGIN, StateId.MANAGER, StateId.CLERK, StateId.CLIENT, StateId.LOGIN, StateId.LOGIN, StateId.TERMINAL}, // LOGIN
        {StateId.MANAGER, StateId.MANAGER, StateId.CLERK, null,          StateId.LOGIN, StateId.LOGIN, StateId.TERMINAL}, // MANAGER
        {StateId.CLERK, StateId.MANAGER, StateId.CLERK, StateId.CLIENT, StateId.LOGIN, StateId.LOGIN, StateId.TERMINAL}, // CLERK
        {StateId.CLIENT, null,          null,          null,          StateId.LOGIN, StateId.LOGIN, StateId.TERMINAL}, // CLIENT
        {StateId.TERMINAL, StateId.TERMINAL, StateId.TERMINAL, StateId.TERMINAL, StateId.TERMINAL, StateId.TERMINAL, StateId.TERMINAL} // TERMINAL
    };

    private StateId currentStateId;
    private String activeClientId;
    private boolean running = true;

    public WarehouseContext() {
        this.warehouse = Warehouse.instance("WH1", "Main Location");
        this.scanner = new Scanner(System.in);

        states.put(StateId.LOGIN, new LoginState(this));
        states.put(StateId.MANAGER, new ManagerMenuState(this));
        states.put(StateId.CLERK, new ClerkMenuState(this));
        states.put(StateId.CLIENT, new ClientMenuState(this));

        this.currentStateId = StateId.LOGIN;
    }
    //Where it all begins
    public static void main(String[] args) {
        new WarehouseContext().run();
    }

    public void run() {
        while (running && currentStateId != StateId.TERMINAL) {
            State currentState = states.get(currentStateId);
            if (currentState == null) {
                System.out.println("No state implementation for " + currentStateId + ". Exiting.");
                break;
            }

            Event event = currentState.run();
            handleEvent(event);
        }
        System.out.println("Warehouse application closed.");
    }
    //Can be probably be improved 
    private void handleEvent(Event event) {
        if (event == null) {
            event = Event.STAY;
        }

        switch (event) {
            case EXIT:
                currentStateId = StateId.TERMINAL;
                running = false;
                return;
            case PUSH_CLIENT:
                pushCurrentState();
                transitionTo(event);
                return;
            case RETURN:
                StateId previous = popPreviousState();
                if (previous != null) {
                    currentStateId = previous;
                } else {
                    transitionTo(event);
                }
                clearActiveClient();
                return;
            default:
                transitionTo(event);
        }
    }
    //part 2 matrixtrans
    private void transitionTo(Event event) {
        int eventIndex = event.ordinal();
        StateId next = transitionMatrix[currentStateId.ordinal()][eventIndex];
        if (next == null) {
            System.out.println("No transition defined for " + currentStateId + " with event " + event + ". Remaining in current state.");
            return;
        }
        currentStateId = next;
        if (currentStateId == StateId.LOGIN) {
            clearActiveClient();
        }
    }

    private void pushCurrentState() {
        stateHistory.push(currentStateId);
    }

    private StateId popPreviousState() {
        return stateHistory.isEmpty() ? null : stateHistory.pop();
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }
// not sure if this is needed
    public Scanner getScanner() {
        return scanner;
    }

    public StateId getCurrentStateId() {
        return currentStateId;
    }

    public String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public int promptInt(String message) {
        while (true) {
            String input = prompt(message);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer value.");
            }
        }
    }

    public double promptDouble(String message) {
        while (true) {
            String input = prompt(message);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric value.");
            }
        }
    }

    public Client findClient(String clientId) {
        List<Client> clients = warehouse.getClients();
        for (Client client : clients) {
            if (client.getId().equalsIgnoreCase(clientId)) {
                return client;
            }
        }
        return null;
    }

    public boolean setActiveClient(String clientId) {
        Client client = findClient(clientId);
        if (client == null) {
            return false;
        }
        this.activeClientId = client.getId();
        return true;
    }

    public Client getActiveClient() {
        if (activeClientId == null) {
            return null;
        }
        return findClient(activeClientId);
    }

    public void clearActiveClient() {
        this.activeClientId = null;
    }
}
