package net.bramp.psyscript;

/**
 * Const Expression
 * 
 * @author Andrew Brampton
 *
 */
public class FloatExpression extends ConstExpression {

	private final double f;

	public FloatExpression(double f) {
		this.f = f;
	}
	
	public FloatExpression(String s) throws NumberFormatException {
		f = Double.parseDouble(s);
	}

	@Override
	public Object get() {
		return new Double(f);
	}

	@Override
	public double getFloat() {
		return f;
	}

	@Override
	public String getString() {
		return Double.toString(f);
	}

	@Override
	public String toString() {
		return "FloatExpression (" + getString() + ")";
	}

	@Override
	public int getInteger() {
		return (int)getFloat();
	}
}
