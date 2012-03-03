package net.bramp.psyscript;

import java.util.List;

import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.parser.SemanticException;

public class Procedure extends Action {

	private final String name;
	private final List<Action> actions;

	public Procedure(Program program, int line, String name, List<Action> actions) {
		super(program, line);

		assert name != null;
		assert actions != null;
		assert !actions.contains(null);
		
		this.name = name;
		this.actions = actions;
	}

	public String getName() {
		return name;
	}

	public void run() throws Exception {
		runActions(actions);
	}

	@Override
	public void check() throws SemanticException {
		checkActions(actions);
	}

	public String toString() {
		String s = "Procedure " + name + " {\n";
		s += printActions(actions);
		s += "}\n";
		return s;
	}
}
