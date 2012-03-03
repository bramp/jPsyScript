package net.bramp.psyscript;

public class ConditionIs extends Condition {

	final private Expression e1;
	final private Expression e2;

	public ConditionIs(Expression e1, Expression e2) {
		assert e1 != null;
		assert e2 != null;
		
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public boolean test() {
		return e1.getString().equals( e2.getString() );
	}

}
