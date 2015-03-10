/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ovh.ebis.geezer;

/**
 *
 * @author
 */
public class InvalidTargetException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of <code>InvalidTarget</code> without detail
	 * message.
	 */
	public InvalidTargetException() {
	}

	/**
	 * Constructs an instance of <code>InvalidTarget</code> with the specified
	 * detail message.
	 * @param msg the detail message.
	 */
	public InvalidTargetException(String msg) {
		super(msg);
	}
}
