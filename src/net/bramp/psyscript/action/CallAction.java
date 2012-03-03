package net.bramp.psyscript.action;

import net.bramp.psyscript.ConstExpression;
import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;

public class CallAction extends Action {

	private final Expression name;

	public CallAction(final Program program, final int line, final Expression name) {
		super(program, line);

		this.name = name;
	}

	@Override
	public void run() throws Exception {
		assert program != null;
		assert name != null;

		if (!program.isRunning())
			return;

		program.run( name.getString() );
	}

	@Override
	public void check() throws SemanticException {
		if ( name instanceof ConstExpression && !program.containsProcedure(name.getString()) ) {
			throw new SemanticException(getLine(), "Procedure '" + name.getString() + "' does not exist");
		}
	}

}
