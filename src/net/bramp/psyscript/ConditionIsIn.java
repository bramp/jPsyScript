package net.bramp.psyscript;

public class ConditionIsIn extends Condition {

	final private Expression e1;
	final private Expression e2;

	public ConditionIsIn(Expression e1, Expression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public boolean test() {
		return e2.getString().contains( e1.getString() );
	}

}
