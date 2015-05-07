package ovh.ebis.geezer.library;

import java.util.List;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.SourceParser;

/**
 * Navigates to the given URL. Accepts a single parameter, the URL to go to. The
 * URL can be surrounded by double-quotes. If it's not prefixed with a protocol,
 * <code>https://</code> will be used.
 * @author
 */
public class NavCom extends Command {

	private final String url;

	private static final String[] PROTOCOLS;

	static {
		PROTOCOLS = new String[]{
			"https://",
			"http://"
		};
	}

	public NavCom(List<String> args, ComInfo com) throws ArgumentNumberException {
		super(args, com);
		url = process(getArgs().get(0));
	}

	/**
	 * Goes to the specified URL.
	 * @return
	 */
	@Override
	public String run() {
		getDriver().get(url);
		return null;
	}

	/**
	 * Removes possible quotes and prefixes with protocol, if none was
	 * specified. This method does NOT check if the result is a valid URL.
	 * @param in
	 * @return
	 */
	private String process(String in) {
		boolean hasProto = false;
		in = SourceParser.stripQuotes(in);

		for (final String proto : PROTOCOLS)
			if (in.startsWith(proto)) {
				hasProto = true;
				break;
			}
		System.out.println("IN is " + in);

		if (!hasProto)
			return PROTOCOLS[0] + in;

		return in;
	}

}
