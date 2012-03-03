package net.bramp.psyscript.action;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;

public class FlushAction extends Action {
	
	public FlushAction(final Program program, final int line) {
		super(program, line);
	}

	@Override
	public void run() {
		// Does nothing, because we are not needed
	}

	@Override
	public void check() throws SemanticException {}

}
