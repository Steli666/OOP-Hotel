public class TooEarlyException extends Exception {
    public TooEarlyException() {
        super("You cannot checkout this early.");
    }
}