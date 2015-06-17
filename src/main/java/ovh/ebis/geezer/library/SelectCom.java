package ovh.ebis.geezer.library;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.SourceParser;

/**
 * Selects an item from a drop-down menu. Currently can only select a single element, may
 * expand it for multi-selection in the future. Syntax: select [target] [element-value]
 */
public class SelectCom extends Command {

	private final By target;
	private final String selection;

	public SelectCom(List<String> args, ComInfo com) throws ArgumentNumberException {
		super(args, com);
		target = SourceParser.elementFinder(args.get(0));
		selection = args.get(args.size() - 1);
	}

	/**
	 * @TODO Figure out a way to select stuff based on value and ID.
	 * @return
	 */
	@Override
	public String run() {
		final Select s;
		s = new Select(getDriver().findElement(target));

//		s.deselectAll();
//		s.selectByVisibleText(selection);
		s.selectByValue(selection);
//		s.se

		return null;
	}

}
