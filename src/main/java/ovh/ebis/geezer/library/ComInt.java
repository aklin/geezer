package ovh.ebis.geezer.library;

/**
 * Command Interface. Contains a single method, 'run', and I don't remember why
 * I had to make that into an interface. Probably wanted to do something
 * clojure-y.
 */
public interface ComInt {

	/**
	 * Run command.
	 * @return A string result which is command-dependant.
	 */
	public String run();

}
