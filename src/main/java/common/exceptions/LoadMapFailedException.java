package common.exceptions;

/**
 * An exception to be thrown when CodeCarnage fails to load the game map
 *
 * @author jacob.ekstrum
 */
public class LoadMapFailedException extends ResourceNotFoundException {

    public LoadMapFailedException() {super();}

    public LoadMapFailedException(String message) {super(message);}

}
