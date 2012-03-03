package net.bramp.psyscript.action;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;
import net.bramp.psyscript.variables.Variable;


public class StartTimerAction extends Action {

	final Variable v;

	public StartTimerAction(final Program program, final int line, Variable v) {
		super(program, line);

		this.v = v;
	}

	@Override
	public void run() {
		v.set( System.nanoTime() );
	}

	@Override
	public void check() throws SemanticException {}

}
