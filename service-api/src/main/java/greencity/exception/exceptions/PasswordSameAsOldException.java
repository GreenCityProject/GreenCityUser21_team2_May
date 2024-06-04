package greencity.exception.exceptions;


/**
 * Exception that we get when user when passwords same as old Exception.
 *
 * @author Dmytro Dovhal
 * @version 1.0
 */
public class PasswordSameAsOldException extends RuntimeException {
    /**
     * Constructor for PasswordSameAsOldException.
     *
     * @param message - giving message.
     */
    public PasswordSameAsOldException(String message) {
        super(message);
    }
}
