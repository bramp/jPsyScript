package net.bramp.psyscript.action.gui;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.objects.ScreenObject;
import net.bramp.psyscript.parser.SemanticException;


public class HideAction extends Action {

	final ScreenObject o;
	
	public HideAction(final Program program, final int line, ScreenObject o) {
		super(program, line);
		this.o = o;
	}

	@Override
	public void run() {
		o.hide();
	}

	@Override
	public void check() throws SemanticException {}

}
