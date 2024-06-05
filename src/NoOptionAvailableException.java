public class NoOptionAvailableException extends Exception {
    public NoOptionAvailableException() {
        super("There's nothing that can be done");
    }
}