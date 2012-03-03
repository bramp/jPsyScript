package net.bramp.psyscript.parser;

/**
 * An example with the logic of a command
 * @author Andrew Brampton
 *
 */
public class SemanticException extends RuntimeExceptionOnLine {

	public SemanticException(int line, Exception e) {
		super(line, e);
	}

	public SemanticException(int line, String string) {
		super(line, string);
	}

}
