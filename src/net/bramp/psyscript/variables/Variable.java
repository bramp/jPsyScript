package net.bramp.psyscript.variables;

import net.bramp.psyscript.Expression;

/**
 * @author Andrew Brampton
 *
 */
public class Variable {
	
	/**
	 * The name of the variable
	 */
	final String name;
	
	/**
	 * The value of this variable
	 */
	Object value = null;

	public Variable(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public Variable(String name, Integer value) {
		this.name = name;
		this.value = value;
	}
	
	public Variable(String name, Double value) {
		this.name = name;
		this.value = value;
	}
	
	public Variable(String name, Long value) {
		this.name = name;
		this.value = value;
	}

	public Variable(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Create a empty variable
	 * @param name
	 */
	public Variable(String name) {
		this.name = name;
		this.value = null;
	}

	public void set(Expression o) {
		value = o.get();
	}

	public void set(Double f) {
		value = f;
	}

	public void set (Integer i) {
		value = i;
	}

	public void set (String s) {
		value = s;
	}
	
	public void set (Long l) {
		value = l;
	}

	protected void checkSet() throws RuntimeException {
		if (value == null)
			throw new RuntimeException("Variable " + name + " has not been set");		
	}
	
	public Object get() {
		checkSet();
		return value;
	}

	public double getFloat() {
		checkSet();
		
		if (value instanceof Long)
			return ((Long)value).doubleValue();

		if (value instanceof Double)
			return ((Double)value).doubleValue();

		if (value instanceof Integer)
			return ((Integer)value).doubleValue();

		if (value instanceof String)
			return Double.parseDouble( (String) value );

		throw new RuntimeException("Variable " + name + " is not a float it is a " + value.getClass());
	}

	public String getString() {
		checkSet();
		return value.toString();
	}

	public String getName() {
		return name;
	}

	public long getLong() {
		checkSet();

		if (value instanceof Long)
			return ((Long)value);

		if (value instanceof Double)
			return ((Double)value).longValue();

		if (value instanceof Integer)
			return ((Integer)value).longValue();

		if (value instanceof String)
			return Long.parseLong( (String) value );

		throw new RuntimeException("Variable " + name + " is not a integer it is a " + value.getClass());
	}

	public int getInteger() {
		checkSet();

		return (int) getLong();
	}

	@Override
	public String toString() {
		if ( value != null ) {
			return "Variable (" + name + "=" + getString() + ")";
		} else {
			return "Variable (" + name + "=null)";
		}
	}
}
