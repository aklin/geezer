package ovh.ebis.geezer;

import java.util.ArrayDeque;
import java.util.Scanner;
import org.openqa.selenium.firefox.FirefoxDriver;
import ovh.ebis.geezer.library.Command;

public class Geezer {

	public static void main(String[] args) {
		SourceParser p;
		String source = "nav \"http://www.captchacreator.com/v-examples.html\"\n"
			+ "text #name \"My name\"\n"
			+ "tab \"email@example.com\"\n"
			+ "tab \"Subject\"\n"
			+ "captcha !Turing #captcha";
//			+ "submit #lst-ib";
//		Command c = SourceParser.parseLineP(source);
		p = new SourceParser(null, new Scanner(source));

		ArrayDeque<Command> com = p.parse();
		Command.setDriver(new FirefoxDriver());
		com.stream().
			forEach((c) -> {
				System.out.println("Running " + c.getName());
				c.run();
				System.out.println("*");
			});
//		if (c == null)
//			System.exit(1);
//		Command.setDriver(new FirefoxDriver());
//		Command.getDriver().get("https://www.google.co.uk");
//		c.run();
	}
}
