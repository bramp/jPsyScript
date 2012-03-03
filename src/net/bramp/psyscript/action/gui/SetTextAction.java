package net.bramp.psyscript.action.gui;

import java.awt.EventQueue;

import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.objects.TextFieldObject;
import net.bramp.psyscript.parser.SemanticException;

public class SetTextAction extends Action {

	final TextFieldObject text;
	final Expression e;
	
	public SetTextAction(final Program program, final int line, final TextFieldObject text, final Expression e) {
		super(program, line);

		assert text != null;
		assert e != null;

		this.text = text;
		this.e = e;
	}

	@Override
	public void run() {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				text.setText(e.getString());
			};
		} );
	}

	@Override
	public void check() throws SemanticException {}

}
