package net.bramp.psyscript;

/**
 * A class to flag a section of code as unfinished
 * @author Andrew Brampton
 *
 */
public class NotImplementedException extends RuntimeException {

	public NotImplementedException(String string) {
		super( string );
	}
	
}
