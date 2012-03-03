package net.bramp.psyscript.parser;

/**
 * A base exception to hold a line number
 * @author Andrew Brampton
 *
 */
public class RuntimeExceptionOnLine extends RuntimeException {

	public int line;

	public RuntimeExceptionOnLine() {
		line = -1;
	}

	public RuntimeExceptionOnLine(int line) {
		this.line = line;
	}

	public RuntimeExceptionOnLine(int line, String msg) {
		super(msg);
		this.line = line;
	}

	public RuntimeExceptionOnLine(int line, Exception e) {
		super(e);
		this.line = line;
	}
	
	public int getLine() {
		return line;
	}
	
	public String toString() {
		return "Error on line " + line + ": " + this.getCause();
	}
}
