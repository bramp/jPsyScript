package net.bramp.psyscript.action;

import java.util.ArrayList;
import java.util.List;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;

public class LogAction extends Action {

	private final List<Expression> es;
	
	private final static String newline = System.getProperty("line.separator");
	
	public LogAction(final Program program, final int line, Expression e) {
		super(program, line);

		es = new ArrayList<Expression> (1);
		es.add(e);
	}

	public LogAction(final Program program, final int line, List<Expression> es) {
		super(program, line);

		this.es = es;
	}

	@Override
	public void run() {
		for ( Expression e : es) {
			String s = e.getString();

			if ( !s.equals(newline) )
				s += "\t";

			program.log( s );
		}
	}

	@Override
	public void check() throws SemanticException {}

}
