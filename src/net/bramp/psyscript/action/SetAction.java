package net.bramp.psyscript.action;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;
import net.bramp.psyscript.variables.ConstVariable;
import net.bramp.psyscript.variables.Variable;

public class SetAction extends Action {

	private final Variable v;
	private final Expression o;
	
	public SetAction(final Program program, final int line, Variable v, Expression o) {
		super(program, line);
		this.v = v;
		this.o = o;
		
		if ( v instanceof ConstVariable )
			throw new SemanticException(getLine(), "Can not set the constant variable '" + v.getName() + "'");
	}

	@Override
	public void run() {
		v.set ( o );
	}

	@Override
	public void check() throws SemanticException {}

}
