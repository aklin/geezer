package ovh.ebis.geezer;

import ovh.ebis.geezer.library.Command;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import ovh.ebis.geezer.library.ComInfo;

public class SourceParser {

	/**
	 * Commend delimiter.
	 */
	private static final String COMMENT;

	private final static Pattern comPattern;

	static {
		COMMENT = "^\\s*//";
		comPattern = Pattern.compile("([^\\\"]\\S+[^\\\"])|(\\\".*\\\")",
			Pattern.UNICODE_CHARACTER_CLASS);
	}

	/**
	 * Parse a single line (statement) and make a Command object out of that.
	 *
	 * @param s
	 * @return
	 */
	public static Command parseLine(String s) {
		final Matcher m;
		final ArrayList<String> bag;
		final ComInfo cinfo;
		String group;

		bag = new ArrayList<>(8);

		/*
		 BUG: Comment is not escaped if enclosed in double quotes
		 */
		s = s.split(COMMENT, 2)[0].trim();
		m = comPattern.matcher(s);

		/*
		 Inputs consist of single groups [a-zA-Z0-9] and symbols, or same
		 surrounded by double quotes
		 */
		if (!m.find()) {
			System.err.println("None found, exiting ---FIXME---");
			return null;
		}

		cinfo = Quiver.getInfo(m.group().trim());
		if (cinfo == null) {
			System.err.
				println("Parse error: \"" + m.group() + "\" not recognized");
			return null;
		}

		while (m.find()) {
			group = m.group().trim();
			if (!"".equals(group))
				bag.add(group);
//			System.out.println("\tAdded " + group);
		}

		return Quiver.spawnCommand(bag, cinfo);
	}

	/**
	 * Checks if string is surrounded by double quotes.
	 *
	 * @param test
	 * @return
	 */
	public static boolean isDoubleQuoted(final String test) {
		return test.startsWith("\"") && test.endsWith("\"");
	}

	/**
	 * Strips surrounding quotes from string if present.
	 *
	 * @param s
	 * @return
	 */
	public static String stripQuotes(final String s) {
		if (isDoubleQuoted(s) && s.length() >= 2)
			return s.substring(1, s.length() - 1);
		else
			return s;
	}

	/**
	 * Parse locator string and return By element.
	 *
	 * @param e
	 * @return
	 */
	public static By elementFinder(final String e) {
		final char selector = e.charAt(0);
		final String loc = stripQuotes(e.substring(1));
		System.out.println("\tIdentifier: \"" + e + "\"");
		switch (selector) {
			case '#': //ID
				return new By.ById(loc);
			case '!': //name
				return new By.ByName(loc);
			case '\'': // xpath
				return new By.ByXPath(loc);
			case '[':
				return By.cssSelector(loc);
			default:
				return new By.ByXPath(e);
		}
	}
}
