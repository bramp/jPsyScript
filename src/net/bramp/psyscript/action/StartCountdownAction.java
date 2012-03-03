package net.bramp.psyscript.action;
import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;
import net.bramp.psyscript.variables.Variable;


public class StartCountdownAction extends Action {

	final Variable countdownStart;
	final Variable countdownDuration;
	final Expression duration;

	public StartCountdownAction(final Program program, final int line, Variable countdownStart, Variable countdownDuration, Expression duration) {
		super(program, line);
		this.countdownStart = countdownStart;
		this.countdownDuration = countdownDuration;
		this.duration = duration;
	}

	@Override
	public void run() {
		countdownStart.set( System.nanoTime() );
		countdownDuration.set( duration.getFloat() );
	}

	@Override
	public void check() throws SemanticException {}

}
