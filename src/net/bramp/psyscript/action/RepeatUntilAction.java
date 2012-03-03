package net.bramp.psyscript.action;

import java.util.List;

import net.bramp.psyscript.Condition;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;


public class RepeatUntilAction extends Action {

	private final Condition c;
	private final List<Action> cs;
	
	public RepeatUntilAction(final Program program, final int line, Condition c, List<Action> cs) {
		super(program, line);

		assert c != null;
		assert cs != null;
		assert !cs.isEmpty();

		this.c = c;
		this.cs = cs;
	}

	@Override
	public void run() throws Exception {
		while ( !c.test() ) {
			if ( !program.isRunning() )
				return;

			runActions(cs);
		}
	}

	@Override
	public void check() throws SemanticException {
		checkActions( cs );
	}

}
