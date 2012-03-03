package net.bramp.psyscript.action;

import java.util.ArrayList;
import java.util.List;

import net.bramp.psyscript.Condition;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;


public class IfAction extends Action {

	private final Condition c;
	private final List<Action> ifActions;
	private List<Action> elseActions = null;

	public IfAction(final Program program, final int line, Condition c, List<Action> ifActions) {
		super(program, line);
		assert c != null;
		assert ifActions != null;

		this.c = c;
		this.ifActions = ifActions;
	}

	@Override
	public void run() throws Exception {
		if ( c.test() ) {
			if ( ifActions != null )
				runActions( ifActions );
		} else {
			if ( elseActions != null )
				runActions( elseActions );
		}
	}

	public void addElse(Action action) {
		elseActions = new ArrayList<Action>(1);
		elseActions.add( action );
	}

	public void addElse(List<Action> elseActions) {
		this.elseActions = elseActions;
	}

	@Override
	public void check() throws SemanticException {
		if ( ifActions != null )
			checkActions( ifActions );

		if ( elseActions != null )
			checkActions( elseActions );
	}
	
	public String toString() {
		String s = "IfAction ( " + c + " ) {\n";
		boolean needCloseBracket = true;

		s += printActions(ifActions);

		if (elseActions != null) {
			if ( elseActions.size() == 1 && elseActions.get(0) instanceof IfAction ) {
				s += getPrintIndent() + "} else " + elseActions.get(0);
				needCloseBracket = false;
			} else {
				s += getPrintIndent() + "} else {\n";
				s += printActions(elseActions);
			}
		}
		if ( needCloseBracket )
			s += getPrintIndent() + "}";

		return s;
	}
}
