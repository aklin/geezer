package ovh.ebis.geezer.library;

import java.util.List;
import org.openqa.selenium.WebDriver;
import ovh.ebis.geezer.ArgumentNumberException;

/**
 * Command class, trying to imitate the "Command" pattern.
 */
public abstract class Command extends ComInfo implements ComInt {

	private final List<String> arguments;
	private static WebDriver driver;

	public Command(final List<String> args, final ComInfo com)
		throws ArgumentNumberException {
		super(com);
		arguments = args;
		if (!checkArgsSize()) {
			for (String s : args)
				System.err.println(s);
			throw new ArgumentNumberException(
				"Arity problem: Min/max arguments: "
				+ getMinArgs() + "/" + getMaxArgs()
				+ ", got: " + args.size());
		}
	}

	/**
	 * Sets GLOBAL WebDriver.
	 * @param d
	 */
	public static final void setDriver(final WebDriver d) {
		driver = d;
	}

	/**
	 * Get the static driver used by all Command instances.
	 * @return WebDriver in use
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/**
	 * Checks the number of arguments according to ComInfo rules.
	 * @param args
	 * @return
	 */
	private boolean checkArgsSize() {
		final int s = arguments.size();
		final int l = this.getMinArgs();
		final int h = this.getMaxArgs();

		return (l <= s && (h < l ? true : s <= h));
	}

	/**
	 * Get the argument list given to this command instance.
	 * @return
	 */
	protected List<String> getArgs() {
		return arguments;
	}

}
