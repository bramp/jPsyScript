package net.bramp.psyscript.action;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;
import net.bramp.psyscript.variables.ConstVariable;
import net.bramp.psyscript.variables.Variable;

public class AppendAction extends Action {

	private final Variable v;
	private final Expression e;
	
	public AppendAction(final Program program, final int line, Variable v, Expression e) {
		super(program, line);

		this.v = v;
		this.e = e;
		
		if ( v instanceof ConstVariable )
			throw new SemanticException(getLine(), "Can not append to the constant variable '" + v.getName() + "'");
	}

	@Override
	public void run() {
		v.set( v.getString() + e.getString() );
	}
	
	@Override
	public void check() throws SemanticException {}
}
