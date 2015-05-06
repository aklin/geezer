package ovh.ebis.geezer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.openqa.selenium.firefox.FirefoxDriver;
import ovh.ebis.geezer.library.Command;

public class Geezer {

	private static RunningMode mode;

	public static void main(String[] args) throws FileNotFoundException {
		final SourceParser p;
		final ArrayDeque<Command> comQueue;
		final Scanner source;

		source = new Scanner(getSourceStream(args));

		Command.setDriver(new FirefoxDriver());

		System.out.println("Welcome to Geezer " + mode);

		if (mode == RunningMode.REPL) {

			comQueue = new ArrayDeque<>();

			while (source.hasNext()) {
				String line = source.nextLine();
				Command com;

				if ("exit".equals(line.toLowerCase()))
					break;

				com = SourceParser.parseLine(line);
				if (com != null) {
					comQueue.add(com);
					com.run();
				}
			}
			return;
		}

		p = new SourceParser(null, source);
		comQueue = p.parse();

		try {
			comQueue.stream().
				forEach((c) -> {
					System.out.println("Running " + c.getName());
					c.run();
					System.out.println("*");
				});
		} catch (NoSuchElementException ex) {
			System.err.println("No such element! " + ex);
		}

	}

	private static InputStream getSourceStream(final String[] args) throws FileNotFoundException {
		if (args.length == 0)
			return System.in;

		//change running mode to FILE
		mode = RunningMode.FILE;
		return new FileInputStream(args[0]);
	}

	private static String demoSource() {
		return "nav \"http://www.captchacreator.com/v-examples.html\"\n"
			+ "text #name \"My name\"\n"
			+ "tab \"email@example.com\"\n"
			+ "tab \"Subject\"\n"
			+ "tab \"Blyat cyka\"\n"
			+ "captcha !Turing #captcha\n";
//			+ "submit #send";
	}
}
