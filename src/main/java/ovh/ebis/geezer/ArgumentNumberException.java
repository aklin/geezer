package ovh.ebis.geezer;

/**
 * Thrown when the number of arguments given to a command exceed the required
 * bounds. This means: Too few or too many arguments.
 * @author
 */
public class ArgumentNumberException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @TODO: Make it work with a Line object
	 * @param message
	 */
	public ArgumentNumberException(final String message) {
		super(message);
	}

}
