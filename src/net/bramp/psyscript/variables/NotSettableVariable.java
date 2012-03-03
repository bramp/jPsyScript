package net.bramp.psyscript.variables;

import net.bramp.psyscript.Expression;

/**
 * A variable which can't be set
 * This includes the special variable that deal with timing etc
 * 
 * @author Andrew Brampton
 *
 */
public class NotSettableVariable extends Variable {
	
	public NotSettableVariable(String name, Double value) {
		super(name, value);
	}

	public NotSettableVariable(String name, String value) {
		super(name, value);
	}

	public NotSettableVariable(String name, Integer value) {
		super(name, value);
	}

	public NotSettableVariable(String name, Object value) {
		super(name, value);
	}

	public void set(Expression o) {
		throw new RuntimeException("Can't set this variable");
	}

	public void set(Double f) {
		throw new RuntimeException("Can't set this variable");
	}

	public void set (Integer i) {
		throw new RuntimeException("Can't set this variable");
	}

	public void set (String s) {
		throw new RuntimeException("Can't set this variable");
	}

	public void set (Long l) {
		throw new RuntimeException("Can't set this variable");
	}

}
