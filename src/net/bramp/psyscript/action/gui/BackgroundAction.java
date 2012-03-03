package net.bramp.psyscript.action.gui;

import java.awt.EventQueue;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.RGB;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.parser.SemanticException;

public class BackgroundAction extends Action {

	final RGB rgb;

	public BackgroundAction(final Program program, final int line, RGB rgb) {
		super(program, line);

		if ( rgb == null )
			throw new IllegalArgumentException();

		this.rgb = rgb;
	}

	@Override
	public void run() {

		EventQueue.invokeLater( new Runnable() {
			public void run() {
				program.getGUI().setBackground( rgb.getColour() );
			};
		} );
	}

	@Override
	public void check() throws SemanticException {}

}
