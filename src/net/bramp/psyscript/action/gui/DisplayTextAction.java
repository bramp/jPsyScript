package net.bramp.psyscript.action.gui;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.bramp.psyscript.ConstExpression;
import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Helper;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.gui.TextPanel;
import net.bramp.psyscript.parser.SemanticException;

public class DisplayTextAction extends Action {

	private final Expression filename;
	private final String type;

	public DisplayTextAction(final Program program, final int line, Expression filename, String textType) {
		super(program, line);

		if ( filename == null )
			throw new IllegalArgumentException("Filename can not be null");

		this.filename = filename;
		this.type = textType;
	}

	@Override
	public void run() throws FileNotFoundException, IOException {

		assert program != null;
		assert filename != null;

		final TextPanel text = program.getGUI().getTextPanel();
		final String file = filename.getString();
		String textType = type;

		// If no text type was entered, try and guess it
		if ( textType == null ) {
			if ( Helper.endsWithIgnoreCase(file, ".rtf") ) {
				textType = "text/rtf";
			} else if ( Helper.endsWithIgnoreCase(file, ".html") 
					 || Helper.endsWithIgnoreCase(file, ".htm") ) {
				textType = "text/html";
			} else {
				textType = "text/plain";
			}
		}

		text.loadText(file, textType);
		text.display();
	}

	@Override
	public void check() throws SemanticException {
		if ( filename instanceof ConstExpression ) {
			try {
				program.checkFileExists( filename.getString() );
			} catch (IOException e) {
				throw new SemanticException(getLine(), e);
			}
		}
	}

}
