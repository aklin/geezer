package ovh.ebis.geezer.library;

import com.DeathByCaptcha.Captcha;
import com.DeathByCaptcha.Client;
import com.DeathByCaptcha.SocketClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import ovh.ebis.geezer.ArgumentNumberException;
import ovh.ebis.geezer.InvalidTargetException;
import ovh.ebis.geezer.Quiver;
import ovh.ebis.geezer.SourceParser;

/**
 * Submit a CAPTCHA image to a solving service and pass the result to a TEXT
 * command. Command layout is the same as TEXT, only the last command is the
 * CAPTCHA image element, instead of a string literal.
 */
public class CaptchaCom extends Command {

	/**
	 * Max attempts to poll the server for result.
	 */
	private static final int TRIES;
	/**
	 * Initial delay before, and seconds between, each attempt.
	 */
	private static final int TIMEOUT;

	private static final String URL;

	private static final String USERNAME;

	private static final String PASSWORD;

	private final By challenge;
	private final By response;

	static {
		TRIES = 5;
		TIMEOUT = TRIES * 10;
		URL = "http://api.dbcapi.me/api/captcha";
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
//		final String b64img;
		final String cid; //captcha ID. Returned by the server after step 1.

		e = getDriver().findElement(eid);

		sshot = getScreenshot(e);

		if (sshot == null) {
			System.err.println(
				"Could not grab screenshot of given element. Aborting CAPTCHA");
			return null;
		}

		//debug
		System.out.println("\tE: " + eid.toString());
//		saveImage(sshot);
		return fire(saveImage(sshot));

//		System.exit(0);
		//Step 0 is done. Now to steop 1: Send screengrab to captcha solver
//		resp = submitChallenge(saveImage(sshot));
//
//		try {
//			cid = resp.getBody();//.getObject().getString("captcha");
//
//			System.out.println("\t***Got CID: " + cid);
//		} catch (JSONException ex) {
//			return null;
//		}
//		return pollLoop(cid);          //total attempts
	}

	private String fire(final File challenge) {
		// Put your DBC username & password here:
		//Client client = (Client)(new HttpClient(args[0], args[1]));
		Client client = (Client) (new SocketClient(USERNAME, PASSWORD));
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
	 * Submits a CAPTCHA challenge request to the server. This is Stage 1 of the
	 * solving process; we still need to poll the server for the result.
	 * @param img
	 * @return
	 */
//	private HttpResponse<String> submitChallenge(final File img) {
//		final HttpResponse<String> resp;
////		final String b64img;
//
////		b64img = "base64:" + DatatypeConverter.printBase64Binary(img);
//		try {
//			resp = Unirest.post(URL)
//				.header("accept", "application/json")
//				.queryString("username", USERNAME)
//				.field("password", PASSWORD)
//				.field("image", img)
//				.asString();
//		} catch (UnirestException ex) {
//			System.err.println("REST problem: " + ex);
//			return null;
//		}
//
//		return resp;
//	}
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
	 * Step 2: Poll captcha service for result.
	 * @param id Captcha ID (returned from step 1)
	 */
//	private String pollLoop(final String id) {
//		final GetRequest b;
//		ImmutablePair<Boolean, String> ret = null;
//
//		b = Unirest.get(
//			new StringBuilder()
//			.append(URL)
//			.append("/")
//			.append(id)
//			.toString());
//
//		snooze(TIMEOUT); //initial snooze
//
//		for (int i = 0; i < TRIES; i++) {
//			ret = pollSingle(b);
//
//			if (ret == null) {//some type of error occured
//				System.err.println("CAPTCHA Abort: Error solving " + id);
//				return null;
//			}
//
//			if (ret.getLeft() == true) {
//				// Return the solved string after some error checking
//				if (ret.getRight() == null)
//					throw new NullPointerException("Something very trippy happened");
//				return ret.getRight(); //We got the captcha
//			} else
//				snooze(TIMEOUT); //Poll failed. Wait for next iteration
//		}
//
//		return null; //We got nada.
//	}
	/**
	 * Does the dirty work of pollLoop. Specifically, it polls the captcha
	 * service for a given ID. A boolean-String pair is returned based on the
	 * server's response. A boolean 'true' signifies that we got an HTTP 200
	 * response, as well as the solved CAPTCHA string. If the boolean part is
	 * 'false', the String part will be 'null', which means that the solution is
	 * not yet available. This method will return null on a HTTP response other
	 * than 200. The appropriate error message will be written in stderr.
	 * @param id CAPTCHA ID to poll for
	 * @param rest Pre-prepared request to execute
	 * @return true+String on solve, false+null on not-available, null on error
	 */
//	private ImmutablePair<Boolean, String> pollSingle(final GetRequest rest) {
//		final HttpResponse<JsonNode> reply;
//		try {
//			reply = rest.asJson();
//		} catch (UnirestException ex) {
//			System.err.println("REST exception");
//			return null;
//		}
//
//		final JSONObject j = reply.getBody().getObject();
//
//		//check result
//		switch (reply.getStatus()) {
//			case 200: // HTTP 200: All is good
//				try {
//					if (j.getBoolean("is_correct"))
//						return new ImmutablePair<>(true, j.
//												   getString("text"));
//					else
//						return new ImmutablePair<>(false, null);
//				} catch (JSONException ex) {
//					System.err.
//						println("JSON Exception while reading CAPTCHA response:" + ex);
//				}
//				break;
//			case 404:
//				System.err.println("Error: Got 404");
//				return null;
//			default:
//				System.err.println("Error: Unrecognized HTTP code: "
//					+ reply.getStatus());
//				return null;
//		}
//		return null;
//	}
	/**
	 * Puts the calling thread to sleep for a given number of seconds.
	 * @param sec
	 */
//	private void snooze(final int sec) {
//		if (sec > 0)
//			try {
//				Thread.sleep(sec * 1000);
//			} catch (InterruptedException ex) {
//				Thread.currentThread().interrupt();
//			}
//	}
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

	/**
	 * Accepts a BufferedImage and returns a byte array that contains its data.
	 * @param img
	 * @return byte[] the image as a byte array
	 */
//	private byte[] getBytesFromImage(final BufferedImage img) {
//		final byte[] ret;
//		try (final ByteArrayOutputStream b = new ByteArrayOutputStream()) {
//			ImageIO.write(img, "jpg", b);
//			ret = b.toByteArray();
//		} catch (IOException ex) {
//			return null;
//		}
//		return ret;
//	}
}
