public class FalseIdException extends Exception {
    public FalseIdException() {
        super("There are no reservations with this id");
    }
}
