package ovh.ebis.geezer.library;

public class ComInfo {

	private final String name;
	private final String className;
	private final int minArgs;
	private final int maxArgs;
	private final boolean hasArgs;

	public ComInfo(final String name, final int min, final int max) {
		this.className = this.getClass().getSimpleName();
		this.name = name;
		minArgs = min;
		maxArgs = max;
		hasArgs = (minArgs == 0 && maxArgs == 0);
	}

	public ComInfo(final ComInfo model) {
		this(model.name, model.minArgs, model.maxArgs);
	}

	public ComInfo(final String name, final ComInfo model) {
		this(name, model.minArgs, model.maxArgs);
	}

	public final String getName() {
		return name;
	}

	/**
	 * Get minimum number of arguments.
	 * @return Number of arguments (low bound)
	 */
	public final int getMinArgs() {
		return minArgs;
	}

	/**
	 * Return the class name of the command. Used to reflexively create new
	 * commands at run-time.
	 * @return
	 */
	protected String getClassName() {
		return className;
	}

	/**
	 * Get maximum number of arguments this command requires.
	 * @return Number of arguments (high bound)
	 */
	public final int getMaxArgs() {
		return maxArgs;
	}

	/**
	 * Checks whether this command takes arguments at all. If this is set to
	 * false, then getMaxArgs and getMinArgs should both return 0 as well.
	 * Otherwise, getMaxArgs should be a non-zero value.
	 * @return
	 */
	public final boolean hasArgs() {
		return hasArgs;
	}
}
