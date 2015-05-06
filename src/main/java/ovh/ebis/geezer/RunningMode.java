package ovh.ebis.geezer;

public enum RunningMode {

	/**
	 * This mode indicates that commands are being read from a file.
	 */
	FILE,
	/**
	 * Geezer is running in repl mode, commands are typed by the user.
	 */
	REPL;
}
