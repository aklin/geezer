package ovh.ebis.geezer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Geezer {

	public static void main(String[] args) {
		final GeeShell shell;
		final Scanner source;

		try {
			if (args.length == 0)
				source = new Scanner(System.in);
			else
				source = new Scanner(new FileInputStream(args[0]));
		} catch (FileNotFoundException ex) {
			System.err.println("Fatal: Resource cannot be loaded " + ex);
			return;
		}

		shell = new GeeShell(source);
		//don't forget to set a driver
		shell.run();

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
