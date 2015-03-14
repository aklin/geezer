package ovh.ebis.geezer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.openqa.selenium.firefox.FirefoxDriver;
import ovh.ebis.geezer.library.Command;

public class Geezer {

	public static void main(String[] args) throws FileNotFoundException {
		final SourceParser p;
		final ArrayDeque<Command> com;

		if (args.length < 1) {
			System.err.println("No input file. Exiting");
			return;
		}

		p = new SourceParser(null, new File(args[0]));

		com = p.parse();
		System.exit(0);
		Command.setDriver(new FirefoxDriver());

		try {
			com.stream().
				forEach((c) -> {
					System.out.println("Running " + c.getName());
					c.run();
					System.out.println("*");
				});
		} catch (NoSuchElementException ex) {
			System.err.println("No such element! " + ex);
		}

	}

	private static String demoSource() {
		return "nav \"http://www.captchacreator.com/v-examples.html\"\n"
			+ "text #name \"My name\"\n"
			+ "tab \"email@example.com\"\n"
			+ "tab \"Subject\"\n"
			+ "tab \"Blyat cyka\"\n"
			+ "captcha !Turing #captcha\n"
			+ "submit #send";
	}
}
