package net.bramp.psyscript;

public final class Coordinate {
	Expression x, y;

	public Coordinate(Expression x, Expression y) {
		this.x = x;
		this.y = y;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public int getX() {
		return x.getInteger();
	}

	public int getY() {
		return y.getInteger();
	}
}
