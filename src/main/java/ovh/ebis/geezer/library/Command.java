package ovh.ebis.geezer.library;

import java.util.List;
import org.openqa.selenium.WebDriver;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.GeeShell;

/**
 * Command class, trying to imitate the "Command" pattern.
 */
public abstract class Command extends ComInfo implements ComInt {

	private final List<String> arguments;
//	private static WebDriver driver;
	private GeeShell context;

	public Command(final List<String> args, final ComInfo com)
		throws ArgumentNumberException {
		super(com);
		arguments = args;
		if (!checkArgsSize()) {
			for (final String s : args)
				System.err.println(s);
			throw new ArgumentNumberException(
				"Arity problem: Min/max arguments: "
				+ getMinArgs() + "/" + getMaxArgs()
				+ ", got: " + args.size());
		}
	}

	/**
	 * Returns the driver used by the shell.
	 *
	 * @return
	 */
	public WebDriver getDriver() {
		return context.getDriver();
	}

	/**
	 * Checks the number of arguments according to ComInfo rules.
	 *
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
	 *
	 * @return
	 */
	protected List<String> getArgs() {
		return arguments;
	}

	@Override
	public GeeShell getContext() {
		return context;
	}

	@Override
	public void setContext(final GeeShell c) {
		context = c;
	}

}
