package ovh.ebis.geezer.library;

public class ComInfo {

	private final String name;
	private final int minArgs;
	private final int maxArgs;
	private final boolean hasArgs;

	public ComInfo(final String name, final int min, final int max) {
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

	public String getName() {
		return name;
	}

	/**
	 * Get minimum number of arguments.
	 * @return Number of arguments (low bound)
	 */
	public int getMinArgs() {
		return minArgs;
	}

	/**
	 * Sets minimum number of arguments required by the command. This method
	 * does nothing if the parameter is less than 0.
	 * @param minArgs A positive integer, minimum number of arguments
	 */
//	public void setMinArgs(int minArgs) {
//		this.minArgs = minArgs >= 0 ? minArgs : this.minArgs;
//		requiresArgs();
//	}
	/**
	 * Get maximum number of arguments this command requires.
	 * @return Number of arguments (high bound)
	 */
	public int getMaxArgs() {
		return maxArgs;
	}

	/**
	 * Sets maximum number of arguments required by the command. This method
	 * does nothing if the parameter is a negative number.
	 * @param maxArgs A positive integer, maximum number of arguments
	 */
//	public void setMaxArgs(int maxArgs) {
//		this.maxArgs = maxArgs >= 0 ? maxArgs : this.maxArgs;
//		requiresArgs();
//	}
	/**
	 * Checks whether this command takes arguments at all. If this is set to
	 * false, then getMaxArgs and getMinArgs should both return 0 as well.
	 * Otherwise, getMaxArgs should be a non-zero value.
	 * @return
	 */
	public boolean hasArgs() {
		return hasArgs;
	}

	/**
	 * Calculates whether this Info takes args and updates hasArgs accordingly.
	 */
//	private void requiresArgs() {
//		hasArgs = (minArgs == 0 && maxArgs == 0);
//	}
}
