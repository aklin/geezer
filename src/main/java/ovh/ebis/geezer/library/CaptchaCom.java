package ovh.ebis.geezer.library;

import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;
import com.DeathByCaptcha.SocketClient;
import com.mashape.unirest.http.HttpResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.InvalidTargetException;
import ovh.ebis.geezer.SourceParser;

/**
 * Submit a CAPTCHA image to a solving service and pass the result to a TEXT
 * command. Command layout is the same as TEXT, only the last command is the
 * CAPTCHA image element, instead of a string literal.
 */
public class CaptchaCom extends Command {

	private static final String USERNAME;
	private static final String PASSWORD;

	private final By challenge;
	private final By response;

	static {
		USERNAME = "hellocaptcha";
		PASSWORD = "hellocaptcha";
	}

	public CaptchaCom(List<String> args, ComInfo com) throws ArgumentNumberException,
															 InvalidTargetException {
		super(args, com);
		challenge = SourceParser.elementFinder(args.get(1));
		response = SourceParser.elementFinder(args.get(0));
	}

	/**
	 * Do it. Syntax: captcha [text elements]+ [captcha element]
	 * @return
	 */
	@Override
	public String run() {
		final String captcha;

		System.out.println("Captcha!");

		captcha = solve(challenge);

		System.out.println("\tSuccess?? Got: \"" + captcha + "\"");

		getDriver().findElement(response).sendKeys(captcha);
		return null;
	}

	/**
	 * Solve a CAPTCHA challenge. If the timeout is less than 0, a default of 0
	 * will be used instead. This method will poll the service at most 'tries'
	 * times. Each attempt will timeout after <code>timeout / tries</code>
	 * seconds. 10 seconds per attempt is a sane value, so you might want to set
	 * <code>timeout = tries * 10</code> .If tries is less than 1, 1 is used
	 * instead.
	 * @param e
	 * @param timeout How many seconds until timeout
	 * @param tries How many times to try within a given timeout
	 * @return
	 */
	private String solve(final By eid) {
		final BufferedImage sshot;
		final HttpResponse<String> resp;
		final WebElement e;

		e = getDriver().findElement(eid);

		sshot = getScreenshot(e);

		if (sshot == null) {
			System.err.println(
				"Could not grab screenshot of given element. Aborting CAPTCHA");
			return null;
		}

		//debug
		return fire(saveImage(sshot));

	}

	private String fire(final File challenge) {
		// Put your DBC username & password here:
		final Client client = new SocketClient(USERNAME, PASSWORD);
		client.isVerbose = true;

		try {
			try {
				System.out.
					println("Your balance is " + client.getBalance() + " US cents");
			} catch (IOException e) {
				System.out.println("Failed fetching balance: " + e.toString());
				return null;
			}

			Captcha captcha = null;
			try {
				// Upload a CAPTCHA and poll for its status with 120 seconds timeout.
				// Put you CAPTCHA image file name, file object, input stream, or
				// vector of bytes, and optional solving timeout (in seconds) here.
				captcha = client.decode(challenge, 120);
			} catch (IOException e) {
				System.out.println("Failed uploading CAPTCHA");
				return null;
			} catch (InterruptedException ex) {
				Logger.getLogger(CaptchaCom.class.getName()).
					log(Level.SEVERE, null, ex);
			}
			if (null != captcha) {
				System.out.
					println("CAPTCHA " + captcha.id + " solved: " + captcha.text);
				return captcha.text;
			} // Report incorrectly solved CAPTCHA if necessary.
			// Make sure you've checked if the CAPTCHA was in fact incorrectly
			// solved, or else you might get banned as abuser.
			/*try {
			 if (client.report(captcha)) {
			 System.out.println("Reported as incorrectly solved");
			 } else {
			 System.out.println("Failed reporting incorrectly solved CAPTCHA");
			 }
			 } catch (IOException e) {
			 System.out.println("Failed reporting incorrectly solved CAPTCHA: " + e.toString());
			 }*/ else
				System.out.println("Failed solving CAPTCHA");
		} catch (com.DeathByCaptcha.Exception e) {
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Dumps the image in a randomly-named file. This is useful for later
	 * diagnostics or wrong solutions.
	 * @param img
	 */
	private File saveImage(final BufferedImage img) {
		final File out;
		try {
			out = new File(RandomStringUtils.randomAlphanumeric(8) + ".png");
			ImageIO.write(img, "png", out);
			System.out.println("Img file: " + out.getCanonicalPath());
		} catch (IOException ex) {
			Logger.getLogger(
				CaptchaCom.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return out;
	}

	/**
	 * Take a screenshot of a given WebElement.
	 * @param e
	 * @return
	 */
	private BufferedImage getScreenshot(final WebElement e) {
		final BufferedImage img;
		final Point topleft;
		final Point bottomright;

		final byte[] screengrab;
		screengrab = ((TakesScreenshot) getDriver()).
			getScreenshotAs(OutputType.BYTES);

		try {
			img = ImageIO.read(new ByteArrayInputStream(screengrab));
		} catch (IOException ex) {
			return null;
		}

		//crop the image to focus on e
		//get dimensions (crop points)
		topleft = e.getLocation();
		bottomright = new Point(e.getSize().getWidth(),
								e.getSize().getHeight());

		return img.getSubimage(topleft.getX(),
							   topleft.getY(),
							   bottomright.getX(),
							   bottomright.getY());
	}

}
