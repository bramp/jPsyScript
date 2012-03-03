package net.bramp.psyscript.action.gui;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.objects.ScreenObject;
import net.bramp.psyscript.parser.SemanticException;

public class ShowAction extends Action {

	final ScreenObject o;
	
	public ShowAction(final Program program, final int line, ScreenObject o) {
		super(program, line);
		this.o = o;
	}

	@Override
	public void run() {
		o.show();
	}

	@Override
	public void check() throws SemanticException {}

}
