package net.bramp.psyscript;

/**
 * Some helper methods
 * @author Andrew Brampton
 *
 */
public final class Helper {

	public static  boolean endsWithIgnoreCase(String string, String suffix) {
		if ( string.length() < suffix.length() )
			return false;
		
		return string.substring(string.length() - suffix.length()).equalsIgnoreCase(suffix);
	}
}
