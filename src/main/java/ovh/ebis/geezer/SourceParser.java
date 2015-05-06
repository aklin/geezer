package ovh.ebis.geezer;

import ovh.ebis.geezer.library.Command;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.lift.match.SelectionMatcher;
import org.openqa.selenium.support.ui.Select;
import ovh.ebis.geezer.library.ComInfo;

public class SourceParser {

	/**
	 * Commend delimiter.
	 */
	private static final String COMMENT;

	private final static Pattern comPattern;
	private final Properties inputs;
	private final Scanner source;

	static {
		COMMENT = "^\\s*//";
//		COMMENT = "[^\\\"].*//.*[^\\\"]";
		comPattern = Pattern.compile("([^\\\"]\\S+[^\\\"])|(\\\".*\\\")",
									 Pattern.UNICODE_CHARACTER_CLASS);
	}

	public SourceParser(final Properties in, final Scanner resource) {
		inputs = in;
		this.source = resource;
	}

	/**
	 * Parse a collection of lines (such as a file) and turn it into a queue of
	 * Commands.
	 * @return
	 */
	public ArrayDeque<Command> parse() {
		final ArrayDeque<Command> ret;
		String cur;
		Command temp;
		ret = new ArrayDeque<>();

		while (source.hasNext()) {
			cur = source.nextLine();
			temp = parseLine(cur);
			if (temp != null)
				ret.add(temp);
		}

		return ret;
	}

	/**
	 * Parse a single line (statement) and make a Command object out of that.
	 * @param s
	 * @return
	 */
	public static Command parseLine(String s) {
		final Matcher m = comPattern.matcher(s);
		final ArrayList<String> bag;
		final ComInfo cinfo;
		final Command ret;
		String group;

		bag = new ArrayList<>(8);

		/*
		 BUG: Comment is not escaped if enclosed in double quotes
		 */
		s = s.split(COMMENT, 2)[0].trim();

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
//		System.out.println("Input: " + s);

		while (m.find()) {
			group = m.group().trim();
			if (!"".equals(group))
				bag.add(group);
//			System.out.println("\tAdded " + group);
		}

		return Quiver.spawnCommand(bag, cinfo);
//		return new Command(bag, cinfo);
	}

	/**
	 * Checks if string is surrounded by double quotes.
	 * @param test
	 * @return
	 */
	public static boolean surroundedByQuotes(final String test) {
		return test.startsWith("\"") && test.endsWith("\"");
	}

	/**
	 * Strips surrounding quotes from string if present.
	 * @param s
	 * @return
	 */
	public static String stripQuotes(final String s) {
		if (surroundedByQuotes(s) && s.length() >= 2)
			return s.substring(1, s.length() - 1);
		else
			return s;
	}

	/**
	 * Parse locator string and return By element.
	 * @param e
	 * @return
	 */
	public static By elementFinder(final String e) {
		final char selector = e.charAt(0);
		final String loc = stripQuotes(e.substring(1));

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
				return null;
		}
	}
}
