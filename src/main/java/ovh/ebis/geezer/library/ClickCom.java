package ovh.ebis.geezer.library;

import java.util.ArrayDeque;
import java.util.List;
import org.openqa.selenium.By;
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
		targets.stream().
			forEach((b) -> {
				getDriver().findElement(b).click();
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
