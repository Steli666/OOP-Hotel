public class TooManyGuestsException extends Exception {
    public TooManyGuestsException() {
        super("You can't check in more guests than there are beds.");
    }
}