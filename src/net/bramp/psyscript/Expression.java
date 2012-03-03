package net.bramp.psyscript;

public abstract class Expression {

	public abstract Object get();
	public abstract double getFloat();
	public abstract int getInteger();
	public abstract String getString();

	/**
	 * Returns the correct kind of expression depending on the string
	 * @param s
	 * @return
	 */
	static public Expression createExpression(final String s) throws NumberFormatException {
		try {
			return new IntExpression( Integer.parseInt(s) );

		} catch ( NumberFormatException e1 ) {
			try {
				return new FloatExpression( Double.parseDouble(s) );

			} catch ( NumberFormatException e2 ) {
				return new StringExpression(s);
			}
		}
	}

}
