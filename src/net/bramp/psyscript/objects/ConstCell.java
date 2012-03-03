package net.bramp.psyscript.objects;

import net.bramp.psyscript.Program;


/**
 * A wrapper around a Cell object
 * @author Andrew Brampton
 */
public class ConstCell extends Cell {

	private final String name;

	public ConstCell(Program p, String name) throws IllegalArgumentException {
		super(p);

		checkValidName( name );
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
