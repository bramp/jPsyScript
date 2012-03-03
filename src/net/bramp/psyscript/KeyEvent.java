package net.bramp.psyscript;

import net.bramp.psyscript.variables.Variable;

public class KeyEvent extends Event {

	Expression filter;
	
	public KeyEvent (Variable lastKey, Variable lastKeyTime) {
		super(lastKey, lastKeyTime);
		
	}
	
	public void setFilter(Expression e) {
		filter = e;
	}

	@Override
	public boolean filter(String name) {
		if (filter != null) {
			final String filter = this.filter.getString();
	
			return filter.indexOf(name) != -1;
		} 
		
		return true;
	}

}
