package net.bramp.psyscript.variables;

/**
 * This counts down from a specific value

 * @author Andrew Brampton
 *
 */
public class CountdownVariable extends NotSettableVariable {

	final Variable countdownStart;
	final Variable countdownDuration;

	public CountdownVariable(String name, Variable countdownStart, Variable countdownDuration) {
		super(name, countdownStart);

		// Store the timerStart time (even if it is also stored in value) to aid in speed
		this.countdownStart = countdownStart;
		this.countdownDuration = countdownDuration;
	}

	@Override
	public double getFloat() {
		long nanoTime = System.nanoTime() - countdownStart.getLong() - countdownDuration.getLong();
		
		return (double)nanoTime / 1000000000D;
	}
	
	@Override
	public String getString() {
		double d = getFloat();
		if ( d > 0 )
			return Double.toString( d );
		return "expired";
	}
	
	@Override
	public Object get() {
		return getString();
	}
}
