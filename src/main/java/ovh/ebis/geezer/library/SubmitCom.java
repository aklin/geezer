package ovh.ebis.geezer.library;

import java.util.List;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.SourceParser;

/**
 * Submits a form. Accepts a single element, and submits the form that it
 * belongs to.
 */
public class SubmitCom extends Command {

	public SubmitCom(List<String> args, ComInfo com) throws ArgumentNumberException {
		super(args, com);
	}

	@Override
	public String run() {
		getDriver().findElement(
			SourceParser.elementFinder(getArgs().get(0)))
			.submit();
		return null;
	}

}
