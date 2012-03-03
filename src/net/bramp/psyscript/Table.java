package net.bramp.psyscript;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.bramp.psyscript.variables.Variable;

public class Table {

	private final String name;
	private final List<String> rows;
	private final int columns;
	private boolean randomise;

	private final String sep;

	private int currentRow = 0;

	public Table(String name, List<String> rows, boolean randomise) {
		this.name = name;
		this.rows = rows;
		this.randomise = randomise;
		
		// Work out the separator on each row
		boolean validTabs = true;
		boolean validCommas = true;

		// Count the first row
		if ( !rows.isEmpty() ) {

			Iterator<String> i = rows.iterator();
			String row = i.next();

			int tabCount = countChars(row, '\t');
			int commaCount = countChars(row, ',');

			if ( tabCount == 0 && commaCount == 0 ) {
				// No separator
				sep = "\0";
				columns = 1;
	
			} else {
				while ( i.hasNext() && (validTabs || validCommas) ) {
					row = i.next();
	
					if ( validTabs )
						validTabs = tabCount == countChars(row, '\t');
				
					if ( validCommas )
						validCommas = commaCount == countChars(row, ',');
				}
	
				// If both are valid pick the highest count
				if ( validTabs && validCommas ) {
					if ( tabCount > commaCount )
						validCommas = false;
					else
						validTabs = false;
				}
				
				if ( validTabs ) {
					sep = "\t";
					columns = tabCount + 1;
				} else if ( validCommas ) {
					sep = ",";
					columns = commaCount + 1;
				} else {
					throw new IllegalArgumentException("Each row must have the same number of items seperated by tabs or commas");
				}
			}
	
			shuffleRows();

		} else {
			columns = 0;
			sep = "";
		}
	}
	
	private static int countChars(final String s, final char c) {
		int index = 0;
		int count = 0;
		
		index = s.indexOf(c);
		while ( index != -1) {
			count++;
			index = s.indexOf(c, index + 1);
		}
		return count;
	}

	public String getName() {
		return name;
	}

	public int getColumns() {
		return columns;
	}
	
	/**
	 * Populates the list of variables with the values from the row
	 * @param vs
	 */
	public void fill(String row, List<Variable> vs) {
		String items[] = row.split( sep );
		
		assert vs.size() == items.length;

		int i = 0;
		for ( Variable v : vs ) {
			v.set( items [ i ] );
			i++;
		}
			
	}
	
	private void shuffleRows() {
		if ( randomise )
			Collections.shuffle( rows );
	}

	public int getRows() {
		return rows.size();
	}
	
	public String nextRow() {
		String row = rows.get(currentRow);

		currentRow++;
		if ( currentRow >= rows.size() ) {
			currentRow = 0;
			shuffleRows();
		}
		
		return row;
	}
}
