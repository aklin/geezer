package ovh.ebis.geezer.library;

import java.util.ArrayDeque;
import java.util.List;
import org.openqa.selenium.By;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.InvalidTargetException;
import ovh.ebis.geezer.SourceParser;
import static ovh.ebis.geezer.SourceParser.elementFinder;

public class TextCom extends Command {

	private final ArrayDeque<By> targets;
	private String literal;

	/**
	 * Write text on a text field. This method accepts a variable number of
	 * arguments. The last one, enclosed in double quotes, is the string to
	 * write. All arguments before it are the element identifiers to write the
	 * string to (and of course, they all get the same string).
	 * @param args
	 * @param com
	 * @throws ArgumentNumberException
	 * @throws ovh.ebis.geezer.InvalidTargetException
	 */
	public TextCom(List<String> args, ComInfo com)
		throws ArgumentNumberException, InvalidTargetException {
		super(args, com);
		if (args.isEmpty())
			throw new ArgumentNumberException("Too few arguments");
		targets = new ArrayDeque<>(args.size());
		processArgs();
	}

	/**
	 * Write text to elements. Sends the same string to all input elements
	 * defined as targets.
	 * @return
	 */
	@Override
	public String run() {
		for (final By b : targets) {
			System.out.println("\tWriting to " + b.toString());
			getDriver().findElement(b).sendKeys(literal);
			System.out.println("\t---------");
		}
//		targets.stream().forEach((b) -> {
//		});
		return null;
	}

	private void processArgs() throws InvalidTargetException {
		final List<String> in = getArgs();
		By tmp;
		for (int i = 0; i < in.size(); i++) {
			if (SourceParser.surroundedByQuotes(in.get(i))) {
				//this is the last argument, therefore the string to type
				literal = SourceParser.stripQuotes(in.get(i));
				break;
			}
			//otherwise it's a bunch of targets

			tmp = elementFinder(in.get(i));

			if (tmp == null)
				throw new InvalidTargetException();

			targets.add(tmp);

		}
	}

}
