package net.bramp.psyscript.action;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;
import net.bramp.psyscript.variables.ConstVariable;
import net.bramp.psyscript.variables.Variable;

public class RemoveAction extends Action {

	private final Variable v;
	private final Expression e;	

	public RemoveAction(final Program program, final int line, Variable v, Expression e) {
		super(program, line);
		this.v = v;
		this.e = e;
		
		if ( v instanceof ConstVariable )
			throw new SemanticException(getLine(), "Can not remove from the constant variable '" + v.getName() + "'");
	}

	@Override
	public void run() {
		String s = v.getString();
		v.set( s.replace(e.getString(), "") );
	}

	@Override
	public void check() throws SemanticException {}

}