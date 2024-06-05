public class NotMeetingRequirmentException extends Exception {
    public NotMeetingRequirmentException() {
        super("There is no room that meets the requirement");
    }
}