package ovh.ebis.geezer;

import java.util.ArrayDeque;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.lang3.NotImplementedException;
import ovh.ebis.geezer.library.ComInt;
import ovh.ebis.geezer.library.Command;

/**
 * The Shell provides session and REPL functionality.
 */
public class GeeShell implements ComInt {

	private final GeeShell parentContext;
	private final Scanner sc;
	private final ArrayDeque<Command> comQueue;

	/**
	 * Run-time variables storage.
	 */
	private final Properties svar;

	public GeeShell(final Scanner source) {
		this(source, null);
	}

	public GeeShell(final Scanner source, final GeeShell context) {
		comQueue = new ArrayDeque<>();
		sc = source;

		this.parentContext = context;

		svar = context == null
			   ? new Properties()
			   : new Properties(context.svar);
	}

	/**
	 * Evaluate and run each command as they come.
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

	@Override
	public GeeShell getContext() {
		if (parentContext == null)
			return this;
		return parentContext;
	}

	/**
	 * Pass the context via the constructor instead.
	 * @deprecated
	 * @param c
	 */
	@Override
	public void setContext(GeeShell c) {
		throw new NotImplementedException("");
	}
}
