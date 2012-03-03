package net.bramp.psyscript.action;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.objects.Cells;
import net.bramp.psyscript.parser.SemanticException;

public class SetCellAction extends Action {

	private final Cells c;
	private final Expression o;
	
	public SetCellAction(final Program program, final int line, Cells c, Expression o) {
		super(program, line);
		this.c = c;
		this.o = o;
	}

	@Override
	public void run() {
		c.setText(o.getString());
	}

	@Override
	public void check() throws SemanticException {}

	@Override
	public String toString() {
		return "SetCellAction ( " + c + "=" + o + " )";
	}
}
