public interface State {
    Event run();
    String getName();
}

