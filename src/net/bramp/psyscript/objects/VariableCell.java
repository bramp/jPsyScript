package net.bramp.psyscript.objects;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.variables.Variable;


/**
 * A wrapper around a Cell object
 * @author Andrew Brampton
 */
public class VariableCell extends Cell {

	private final Variable v;

	public VariableCell(Program p, Variable var) {
		super(p);
		v = var;
	}

	public String getName() throws IllegalArgumentException {
		final String cellName = v.getString();
		checkValidName( cellName );
		return cellName;
	}

}
