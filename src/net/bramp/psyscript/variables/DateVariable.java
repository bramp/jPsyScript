package net.bramp.psyscript.variables;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Returns the current date
 * 
 * @author Andrew Brampton
 *
 */
public class DateVariable extends NotSettableVariable {

	public DateVariable(String name) {
		super(name, getDate());
	}

	public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
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
