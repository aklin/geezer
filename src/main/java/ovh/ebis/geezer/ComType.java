package ovh.ebis.geezer;

/**
 * Interaction of command to be executed on a WebElement.
 */
public enum ComType {

	/**
	 * Send keys as a user would type them.
	 * @TODO: Perhaps find a more distinctive name
	 */
	TEXT("type"),
	/**
	 * Choose an element from a drop-down menu.
	 */
	CHOOSE("choose"),
	/**
	 * Click a check-box or radio button.
	 */
	CLICK("click"),
	/**
	 * Assert before any other interaction.
	 */
	ASSERTBEFORE("assertbefore"),
	/**
	 * Assert at this time.
	 */
	ASSERT("assert"),
	/**
	 * A combination of CAPTCHASOLVE and TEXT.
	 */
	CAPTCHA("captcha"),
	/**
	 * Solve a CAPTCHA challenge. Used internally only.
	 */
	CAPTCHASOLVE("captchasolve"),
	/**
	 * Assert after all changes have been made.
	 */
	ASSERTAFTER("assertafter");

	private final String value;

	ComType(String v) {
		this.value = v;
	}

	/**
	 * Get the matching Interaction from a String. If null is passed, null will
	 * be returned.
	 * @param a Case-insensitive input.
	 * @return Associated type or null if none found.
	 */
	public static ComType fromString(final String a) {
		for (ComType t : ComType.values())
			if (t.value.equalsIgnoreCase(a))
				return t;
		return null;
	}
};
