package ovh.ebis.geezer.library;

import java.util.List;
import org.openqa.selenium.WebDriver;
import ovh.ebis.geezer.ArgumentNumberException;

/**
 * Command class, trying to imitate the "Command" pattern.
 * @author
 */
public abstract class Command extends ComInfo implements ComInt {

	private final List<String> arguments;
	private static WebDriver driver;

	public Command(final List<String> args, final ComInfo com)
		throws ArgumentNumberException {
		super(com);
		if (checkArgsSize(args)) {
			for (String s : args)
				System.err.println(s);
			throw new ArgumentNumberException(
				"Arity problem: Min/max arguments: "
				+ getMinArgs() + "/" + getMaxArgs()
				+ ", got: " + args.size());
		}
		arguments = args;
	}

	/**
	 * Sets GLOBAL WebDriver.
	 * @param d
	 */
	public static final void setDriver(final WebDriver d) {
		driver = d;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	/**
	 * Checks the number of arguments according to ComInfo rules.
	 * @param args
	 * @return
	 */
	private boolean checkArgsSize(final List<String> args) {
		return args.size() < this.getMaxArgs()
			|| args.size() > this.getMinArgs();
	}

	/**
	 * Get the argument list given to this command instance.
	 * @return
	 */
	protected List<String> getArgs() {
		return arguments;
	}

}
