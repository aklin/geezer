package ovh.ebis.geezer.library;

import java.util.ArrayDeque;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.SourceParser;

/**
 * Clicks on an element. Intended to work with checkboxes and radio boxes.
 */
public class ClickCom extends Command {

	private final ArrayDeque<By> targets;

	public ClickCom(List<String> args, ComInfo com) throws ArgumentNumberException {
		super(args, com);
		targets = process();
	}

	/**
	 * Clicks through all the elements!.
	 * @return
	 */
	@Override
	public String run() {
		final WebDriver d = getDriver();
		targets.stream().
			forEach((b) -> {
//				if(d.g)
				System.out.println("\tClicking on " + b.toString());
				d.findElement(b).click();
				
//				d.findElement(b).sendKeys(" ");
			});
		return null;
	}

	private ArrayDeque<By> process() {
		final List<String> args = getArgs();
		final ArrayDeque<By> ret = new ArrayDeque<>(args.size());

		for (final String s : args)
			ret.addLast(SourceParser.elementFinder(s));

		return ret;
	}
}
