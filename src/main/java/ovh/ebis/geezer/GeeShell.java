package ovh.ebis.geezer;

import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import ovh.ebis.geezer.library.ComInt;
import ovh.ebis.geezer.library.Command;

/**
 * The Shell provides session and REPL functionality.
 */
public class GeeShell implements ComInt {

	private final GeeShell parentContext;
	private final Scanner sc;
	private final ArrayDeque<Command> comQueue;
	private WebDriver driver;

	/**
	 * Run-time variables storage.
	 */
	private final Properties svar;

	public GeeShell(final Scanner source) {
		this(source, null);
	}

	public GeeShell(final Scanner source, final GeeShell context) {
		this(source, context, null);
	}

	/**
	 * Create a new Gee shell. The shell can be run interactively or as a string parser.
	 * That depends on the source argument. If the driver is null, the shell will attempt to
	 * inherit the parent driver instead.
	 *
	 * @param source
	 * @param context
	 * @param driver
	 */
	public GeeShell(final Scanner source, final GeeShell context, final WebDriver driver) {
		comQueue = new ArrayDeque<>();
		sc = source;

		this.parentContext = context;

		svar = context == null
			? new Properties()
			: new Properties(context.svar);

		if (driver == null)
			this.driver = context == null
				? null
				: context.getDriver();
	}

	/**
	 * Evaluate and run each command as they come.
	 *
	 * @return
	 */
	@Override
	public String run() {
		Command com;

		while (sc.hasNext()) {
			com = SourceParser.parseLine(sc.nextLine());

			if (com == null)
				continue;
			com.setContext(this);
			comQueue.add(com);
			com.run();
		}

		return null;
	}

	/**
	 * Get the driver used by this shell.
	 *
	 * @return
	 */
	public final WebDriver getDriver() {
		return driver;
	}

	/**
	 * Set the driver for this shell.
	 *
	 * @param driver
	 * @return
	 */
	public final WebDriver setDriver(final WebDriver driver) {
		this.driver = driver;
		return this.driver;
	}

	@Override
	public GeeShell getContext() {
		if (parentContext == null)
			return this;
		return parentContext;
	}

	/**
	 * Pass the context via the constructor instead.
	 *
	 * @deprecated
	 * @param c
	 */
	@Override
	public void setContext(GeeShell c) {
		throw new NotImplementedException("");
	}
}
