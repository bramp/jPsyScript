package net.bramp.psyscript.action;

import java.util.List;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;

public abstract class Action {

	protected final Program program;
	protected final int line;

	static int printDepth = 0;
	
	public Action(final Program program, final int line) {
		if ( program == null )
			throw new IllegalArgumentException("Program can not be null");

		this.program = program;
		this.line = line;
	}

	protected void runActions(List<Action> actions) throws Exception {
		assert actions != null;

		for ( Action a : actions ) {
			if ( !program.isRunning() )
				return;

			program.runAction(a);
		}
	}

	protected void checkActions(List<Action> actions) throws SemanticException {
		assert actions != null;

		for ( Action a : actions ) {
			a.check();
		}
	}
	
	protected String getPrintIndent() {
		String indent = "";
		for (int i = 0; i < printDepth; i++)
			indent += "\t";
		return indent;
	}

	protected String printActions(List<Action> actions) {
		assert actions != null;
		StringBuffer s = new StringBuffer();
		
		printDepth++;
		
		String indent = getPrintIndent();

		for ( Action a : actions ) {
			s.append(indent);
			s.append(a.toString());
			s.append("\n");
		}

		printDepth--;

		return s.toString();
	}

	/**
	 * Runs this action
	 * @throws Exception
	 */
	public abstract void run() throws Exception;

	/**
	 * Does some basic checks to see if this action is invalid
	 * @throws Exception
	 */
	public abstract void check() throws SemanticException;

	/**
	 * Get the line number of this action
	 * @return
	 */
	public int getLine() {
		return line;
	}
}
