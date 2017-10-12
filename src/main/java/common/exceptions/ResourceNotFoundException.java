package common.exceptions;

/**
 * Exception class to be thrown or subclasses when a resource cannot be found in CodeCarnage.
 *
 * @author jacob.ekstrum
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
