package net.bramp.psyscript;

import net.bramp.psyscript.variables.Variable;

public abstract class Event {
	final Variable lastClick;
	final Variable lastClickTime;

	long startTime = -1;

	public Event (Variable lastClick, Variable lastClickTime) {
		this.lastClick = lastClick;
		this.lastClickTime = lastClickTime;
	}

	public void start() {
		startTime = System.nanoTime();
	}

	public void stop(String click) {
		long nanoTime = System.nanoTime() - startTime;

		lastClick.set( click );
		lastClickTime.set( (double)nanoTime / 1000000000D );
		startTime = -1;
	}

	public void expire() {
		if ( startTime == -1 )
			return;

		stop("timeout");
	}

	/**
	 * Called with the name of which object was fired
	 * Returns true if we are waiting for this object, otherwise false
	 * @param name
	 * @return
	 */
	public abstract boolean filter(String name);
}
