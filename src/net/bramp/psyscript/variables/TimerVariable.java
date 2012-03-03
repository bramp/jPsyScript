package net.bramp.psyscript.variables;

/**
 * This variable counts up from a specific start time
 * 
 * @author Andrew Brampton
 *
 */
public class TimerVariable extends NotSettableVariable {

	final Variable timerStart;
	
	public TimerVariable(String name, Variable timerStart) {
		super(name, timerStart);
		
		// Store the timerStart time (even if it is also stored in value) to aid in speed
		this.timerStart = timerStart;
	}

	@Override
	public double getFloat() {
		long nanoTime = System.nanoTime() - timerStart.getLong();
		return (double)nanoTime / 1000000000D;
	}
	
	@Override
	public String getString() {
		return Double.toString( getFloat() );
	}
	
	@Override
	public Object get() {
		return new Double( getFloat() );
	}
}
