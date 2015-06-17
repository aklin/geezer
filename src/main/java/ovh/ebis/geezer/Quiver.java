package ovh.ebis.geezer;

import java.util.List;
import ovh.ebis.geezer.library.ComInfo;
import java.util.HashMap;
import ovh.ebis.geezer.library.*;

/**
 * ComInfo lookup table.
 *
 * @author
 */
public class Quiver {

	/**
	 * Dictionary that contains a "canonical model" of each command. This includes number of
	 * arguments and keyword.
	 */
	private static final HashMap<String, ComInfo> commands;

	static {
		commands = new HashMap<>(8);
		commands.put("text", new ComInfo("text", 2, 0));
		commands.put("option", new ComInfo("option", 2, 0));
		commands.put("check", new ComInfo("check", 2, 0));
		commands.put("nav", new ComInfo("nav", 1, 1));
		commands.put("submit", new ComInfo("submit", 1, 1));
		commands.put("select", new ComInfo("select", 2, 2));
		commands.put("click", new ComInfo("click", 1, 0));
		commands.put("tab", new ComInfo("tab", 1, 0));
		commands.put("captcha", new ComInfo("captcha", 2, 0));
	}

	/**
	 * Command look-up.
	 *
	 * @param name
	 * @return
	 */
	public static ComInfo getInfo(final String name) {
		if ("".equals(name))
			return null;
		return commands.get(name);
	}

	/**
	 * @param args
	 * @param cinfo
	 * @return
	 */
	public static Command spawnCommand(final List<String> args,
		final ComInfo cinfo) {
		try {
			switch (cinfo.getName()) {
				case "nav":
					return new NavCom(args, cinfo);
				case "text":
					return new TextCom(args, cinfo);
				case "submit":
					return new SubmitCom(args, cinfo);
				case "tab":
					return new TabCom(args, cinfo);
				case "captcha":
					return new CaptchaCom(args, cinfo);
				case "click":
					return new ClickCom(args, cinfo);
				case "select":
					return new SelectCom(args, cinfo);

				default:
					return null;
			}
		} catch (ArgumentNumberException ex) {
			System.err.println("Arg num exception " + ex);
		} catch (InvalidTargetException ex) {
			System.err.println("Invalid target exception " + ex);
		}
		return null;
	}

}
