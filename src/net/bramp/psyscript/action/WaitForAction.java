package net.bramp.psyscript.action;

import net.bramp.psyscript.Event;
import net.bramp.psyscript.Expression;
import net.bramp.psyscript.Program;
import net.bramp.psyscript.parser.SemanticException;

public class WaitForAction extends Action {

	Expression waitDuration; // How long to wait for
	Event event;

	public WaitForAction(final Program program, final int line) {
		super(program, line);
	}

	public WaitForAction(final Program program, final int line, Expression duration) {
		super(program, line);

		setDuration( duration );
	}

	@Override
	public void run() {
		
		if ( waitDuration != null ) {
			long time = (long) (waitDuration.getFloat() * 1000);

			if ( event == null )
				program.getGUI().waitFor(time);
			else
				program.getGUI().waitFor(time, event);
		} else {
			program.getGUI().waitFor(event);
		}
	}

	/**
	 * Add a specific amount of time to wait for (in seconds)
	 * @param e1
	 */
	public void setDuration(Expression duration) {
		assert waitDuration == null;

		waitDuration = duration;
	}

	/**
	 * Add an event to wait on
	 * @param object
	 */
	public void setEvent(Event event) {
		assert this.event == null;
		
		this.event = event;
	}

	@Override
	public void check() throws SemanticException {}

}
