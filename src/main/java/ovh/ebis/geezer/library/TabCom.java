package ovh.ebis.geezer.library;

import java.util.List;
import org.openqa.selenium.Keys;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.SourceParser;

public class TabCom extends Command {

	private final String literal;

	public TabCom(List<String> args, ComInfo com) throws ArgumentNumberException {
		super(args, com);
		literal = SourceParser.stripQuotes(args.get(0));
	}

	/**
	 * Press "tab" and start writing. No idea if it's gonna work.
	 * @return
	 */
	@Override
	public String run() {
//		System.out.println("TabCom");
//		System.out.println("TADA:::  " + getDriver().switchTo().activeElement().
//			getText());
		getDriver().switchTo().activeElement().sendKeys(Keys.TAB);
		getDriver().switchTo().activeElement().sendKeys(literal);
		return null;
	}

}
