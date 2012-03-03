package net.bramp.psyscript;

public class StringExpression extends ConstExpression {

	private final String s;
	
	public StringExpression(String s) {
		this.s = s;
	}

	@Override
	public Object get() {
		return s;
	}

	@Override
	public double getFloat() {
		return Double.parseDouble(s);
	}
	
	@Override
	public int getInteger() {
		return Integer.parseInt(s);
	}

	@Override
	public String getString() {
		return s;
	}

	@Override
	public String toString() {
		return "StringExpression (" + getString() + ")";
	}


}
