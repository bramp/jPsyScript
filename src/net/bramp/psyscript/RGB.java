package net.bramp.psyscript;

import java.awt.Color;

public final class RGB {
	final Expression colour;
	Expression red, green, blue;
	
	public RGB(Expression r, Expression g, Expression b) {
		colour = null;
		setColour(r, g, b);
	}

	public RGB(Expression s) {
		colour = s;
		red = null;
		green = null;
		blue = null;
	}
	
	public RGB(String s) {
		colour = null;
		setColour(s);
	}

	private void setColour(String s) {
		if ( s == null )
			throw new IllegalArgumentException("s can not be null");

		s = s.toLowerCase();

		if ( s.equals("black") ) {
			setColour( 0,0,0 );
		} else if ( s.equals("dark gray") || s.equals("dark grey") ) {
			setColour( 16383, 16383, 16383 );
		} else if ( s.equals("mid gray") || s.equals("mid grey") ) {
			setColour( 32767, 32767, 32767 );
		} else if ( s.equals("light gray") || s.equals("light grey") ) {
			setColour( 49151, 49151, 49151 );
		} else if ( s.equals("white") ) {
			setColour( 65535, 65535, 65535 );
		}
	}
	
	private void setColour(Expression r, Expression g, Expression b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	/**
	 * Check the colours are within the correct boundaries
	 * @param r
	 * @param g
	 * @param b
	 */
	private void checkColour(int r, int g, int b) {
		if ( r < 0 || r > 65535 )
			throw new IllegalArgumentException("Red must between 0 and 65535 not " + r);

		if ( g < 0 || g > 65535 )
			throw new IllegalArgumentException("Green must between 0 and 65535 not " + g);

		if ( b < 0 || b > 65535 )
			throw new IllegalArgumentException("Blue must between 0 and 65535 not " + b);
	}

	private void setColour(int r, int g, int b) {
		checkColour(r,g,b);

		this.red = new IntExpression(r);
		this.green = new IntExpression(g);
		this.blue = new IntExpression(b);
	}

	public Color getColour() {
		// If this is a string expression figure out the new colour
		if ( colour != null )
			setColour(colour.getString());

		int r = (int) (red.getFloat() / 256);
		int g = (int) (green.getFloat() / 256);
		int b = (int) (blue.getFloat() / 256);

		return new Color(r, g, b);
	}
}
