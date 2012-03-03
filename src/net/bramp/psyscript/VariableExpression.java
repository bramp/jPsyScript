package net.bramp.psyscript;

import net.bramp.psyscript.variables.Variable;

public class VariableExpression extends Expression {

	private final Variable v;

	public VariableExpression(Variable v) {
		assert v != null;

		this.v = v;
	}

	@Override
	public Object get() {
		return v.get();
	}

	@Override
	public double getFloat() {
		return v.getFloat();
	}

	@Override
	public String getString() {
		return v.getString();
	}

	@Override
	public String toString() {
		return "VariableExpression (" + v + ")";
	}

	@Override
	public int getInteger() {
		return v.getInteger();
	}
}
