package net.bramp.psyscript;

public class IntExpression extends ConstExpression {

	private final int i;
	
	public IntExpression(int i) {
		this.i = i;
	}
	
	public IntExpression(String s) throws NumberFormatException {
		i = Integer.parseInt(s);
	}

	@Override
	public Object get() {
		return Integer.valueOf(i);
	}

	@Override
	public double getFloat() {
		return (double)i;
	}

	@Override
	public String getString() {
		return Integer.toString(i);
	}

	@Override
	public String toString() {
		return "IntExpression (" + getString() + ")";
	}

	@Override
	public int getInteger() {
		return i;
	}
}
