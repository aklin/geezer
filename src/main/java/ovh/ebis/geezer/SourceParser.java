package ovh.ebis.geezer;

import ovh.ebis.geezer.library.Command;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import ovh.ebis.geezer.library.ComInfo;

public class SourceParser {

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

	public SourceParser(final Properties in, final File source)
		throws FileNotFoundException {
		this(in, new Scanner(source));
	}

	public SourceParser(final Properties in, final Scanner resource) {
		inputs = in;
		this.source = resource;
	}

	public ArrayDeque<Command> parse() {
		final ArrayDeque<Command> ret;
		String cur;
		Command temp;
		ret = new ArrayDeque<>();

		/*
		 BUG: Comment is not escaped if enclosed in double quotes
		 */
		while (source.hasNext()) {
			cur = source.nextLine().split(COMMENT, 2)[0].trim();
			temp = parseLine(cur);
			if (temp != null)
				ret.add(temp);
		}

		return ret;
	}

//	public static Command parseLineP(final String s) {
//		return parseLine(s);
//	}
	private static Command parseLine(final String s) {
		final Matcher m = comPattern.matcher(s);
		final ArrayList<String> bag;
		final ComInfo cinfo;
		final Command ret;
		String group;

		bag = new ArrayList<>(8);

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
		System.out.println("Input: " + s);

		while (m.find()) {
			group = m.group().trim();
			if (!"".equals(group))
				bag.add(group);
			System.out.println("\tAdded " + group);
		}

		return Quiver.spawnCommand(bag, cinfo);
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
	 * Prase locator string and return By element.
	 * @param e
	 * @return
	 */
	public static By elementFinder(final String e) {
		if (e.startsWith("#"))
			return new By.ById(e.substring(1));
		else if (e.startsWith("!"))
			return new By.ByName(e.substring(1));
		return null;
	}
}
