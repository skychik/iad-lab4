package shared;

import com.google.gwt.regexp.shared.RegExp;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client-side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 *
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 *
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static UserInputCheckStates isValidName(String name) {
		if(name.length() < 4)
			return UserInputCheckStates.CHECK_INVALID_LENGTH;

		RegExp regExp = RegExp.compile("^[A-Za-z][A-Za-z0-9]+$");
		if(regExp.exec(name) == null)
			return UserInputCheckStates.CHECK_INVALID_SYMBOLS;

		return UserInputCheckStates.CHECK_OK;
	}

	public static UserInputCheckStates isValidPassword(String password) {
		if(password.length() < 6)
			return UserInputCheckStates.CHECK_INVALID_LENGTH;

		RegExp regExp = RegExp.compile("^[A-Za-z0-9]+$");
		if(regExp.exec(password) == null)
			return UserInputCheckStates.CHECK_INVALID_SYMBOLS;

		return UserInputCheckStates.CHECK_OK;
	}

	public static YInputStates isValidY(String yField) {

		double y;

		if(yField.length() == 0)
			return YInputStates.EMPTY_STRING;

		try {
			y = Double.parseDouble(yField);
		} catch (NumberFormatException nfe) {
			return YInputStates.INVALID_SYMBOLS;
		}

		if(y < -3 || y > 5)
			return YInputStates.WRONG_RANGE;

		return YInputStates.OK;
	}
}