package net.bramp.psyscript.action;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;

public class AbortAction extends Action {

	public AbortAction(final Program program, final int line) {
		super(program, line);
	}

	@Override
	public void run() {
		program.stop();
	}

	@Override
	public void check() throws SemanticException {}

}
