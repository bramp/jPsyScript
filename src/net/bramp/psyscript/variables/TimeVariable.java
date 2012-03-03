package net.bramp.psyscript.variables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Returns the current time
 * 
 * @author Andrew Brampton
 *
 */
public class TimeVariable extends NotSettableVariable {

	public TimeVariable(String name) {
		super(name, getDate());
	}

	public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format( new Date() );
	}
	
	@Override
	public String getString() {
		return getDate();
	}
	
	@Override
	public Object get() {
		return getDate();
	}
}
