package ovh.ebis.geezer.library;

import java.util.List;
import org.openqa.selenium.By;
import ovh.ebis.geezer.ArgumentNumberException;

/**
 * Selects an item from a drop-down menu. Currently can only select a single
 * element, may expand it for multi-selection in the future.
 */
public class SelectCom extends Command {

	private By target;
	private By selection;

	public SelectCom(List<String> args, ComInfo com) throws ArgumentNumberException {
		super(args, com);
	}

	@Override
	public String run() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
