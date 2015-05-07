package ovh.ebis.geezer.library;

import ovh.ebis.geezer.GeeShell;

/**
 * Command Interface. Contains the executor, 'run' and the session getter.
 */
public interface ComInt {

	/**
	 * Run command.
	 * @return A string result which is command-dependant.
	 */
	public String run();

	/**
	 * Get the session (context).
	 * @return Session under which this ComInt belongs.
	 */
	public GeeShell getContext();

	public void setContext(final GeeShell c);

}
