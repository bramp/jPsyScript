package net.bramp.psyscript.variables;

/**
 * A variable whose value does not change
 * @author Andrew Brampton
 *
 */
public class ConstVariable extends NotSettableVariable {
	
	public ConstVariable(String name, Double value) {
		super(name, value);
	}

	public ConstVariable(String name, String value) {
		super(name, value);
	}

	public ConstVariable(String name, Integer value) {
		super(name, value);
	}

	public ConstVariable(String name, Object value) {
		super(name, value);
	}

}
