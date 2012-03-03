package net.bramp.psyscript;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

import net.bramp.psyscript.action.Action;
import net.bramp.psyscript.gui.ProgramGUI;
import net.bramp.psyscript.objects.CursorObject;
import net.bramp.psyscript.objects.SliderObject;
import net.bramp.psyscript.objects.TextFieldObject;
import net.bramp.psyscript.parser.BackupCharStream;
import net.bramp.psyscript.parser.CharStream;
import net.bramp.psyscript.parser.RuntimeExceptionOnLine;
import net.bramp.psyscript.parser.ParseException;
import net.bramp.psyscript.parser.PsyScriptParser;
import net.bramp.psyscript.variables.ConstVariable;
import net.bramp.psyscript.variables.CountdownVariable;
import net.bramp.psyscript.variables.DateVariable;
import net.bramp.psyscript.variables.TimeVariable;
import net.bramp.psyscript.variables.TimerVariable;
import net.bramp.psyscript.variables.Variable;

public final class Program implements LogListener {

	/**
	 * Base path of this program
	 */
	String path;
	
	/**
	 * The listener the log events are sent to
	 */
	LogListener logger;

	private final Map<String, Procedure> procedures = new TreeMap<String, Procedure>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, Table> tables = new TreeMap<String, Table>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, Variable> variables = new TreeMap<String, Variable>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * Is this program currently running
	 */
	private boolean isRunning;

	private ProgramGUI gui;

	/**
	 * The action currently being executed
	 */
	private Action activeAction;

	public Program() {
		// Setup some of the initial variables
		add(new ConstVariable("$return", System.getProperty("line.separator") ) );
		add(new ConstVariable("$space", " " ) );
		add(new ConstVariable("$blank", ""));

		add(new Variable("$timerStart", 0L ));
		add(new TimerVariable("$timer", getVariable("$timerStart") ));

		add(new Variable("$countdownStart", 0L ));
		add(new Variable("$countdownDuration", 0L ));
		add(new CountdownVariable("$countdown", getVariable("$countdownStart"), getVariable("$countdownDuration") ));

		add(new DateVariable("$dateStamp"));
		add(new TimeVariable("$timeStamp"));

		add(new Variable("$experiment", "$experiment"));
		add(new Variable("$subject", "$subject"));
		add(new Variable("$comments", "$comments"));
	}

	protected void add(Variable value) {
		if ( variables.containsKey(value.getName()) )
			throw new IllegalArgumentException("Variable " + value.getName() + " already exists");

		variables.put(value.getName(), value);
	}

	public void add(Procedure p) {
		if ( procedures.containsKey( p.getName() ))
			throw new RuntimeException("Duplicate procedure '" + p.getName() + "'" );

		procedures.put( p.getName() , p);
	}

	public void add(Table t) {
		if ( tables.containsKey( t.getName() ))
			throw new RuntimeException("Duplicate table '" + t.getName() + "'" );

		tables.put( t.getName() , t);
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append( "Program (" + procedures.size() + " procs, " + tables.size() + " tables)\n" );

		for (Procedure p : procedures.values() ) {
			s.append( p.toString() );
			s.append( "\n" );
		}
		return s.toString();
	}

	/**
	 * Run the procedure by calling the "main" procedure
	 * @throws Exception 
	 */
	public void run(ProgramGUI gui) throws Exception {
		this.gui = gui;

		try {
			isRunning = true;
			run( "main" );
		} catch ( Exception e ) {
			if ( activeAction != null )
				throw new RuntimeExceptionOnLine(activeAction.getLine(), e);
			throw e;
		} finally {
			isRunning = false;
			close(); // close the log file
		}
	}

	/**
	 * Calls a specific procedure
	 * @param procedure
	 * @throws Exception 
	 */
	public void run(String procedure) throws Exception {
		if ( procedure == null || procedure.length() <= 1 )
			throw new IllegalArgumentException("Invalid procedure name '" + procedure + "'");

		Procedure p = procedures.get( procedure );

		if ( p == null ) {
			throw new IllegalArgumentException("Program does not contain the '" + procedure + "' procedure" );
		}

		p.run();
	}

	public CursorObject getCursor() {
		// TODO Auto-generated method stub
		return null;
	}

	public SliderObject getSlider() {
		// TODO Auto-generated method stub
		return null;
	}

	public TextFieldObject getTextField() {
		// TODO make a singleton pattern
		return null;
	}

	public Variable getVariable(String name) {
		Variable v = variables.get(name);

		if ( v == null ) {
			v = new Variable(name);
			add(v);
		}

		return v;
	}

	public void setVariable(String variableName, String value) {
		Variable v = getVariable(variableName);
		v.set(value);
	}
	
	public Table getTable(String table) {
		return tables.get(table);
	}

	/**
	 * The main GUI for this program
	 * @return
	 */
	public ProgramGUI getGUI() {
		assert gui != null;
		return gui;
	}

	public static Program load(String filename) throws RuntimeExceptionOnLine, IOException {
		final Program p = load ( new FileReader( filename ) );
		p.setFilename( filename );
		return p;
	}

	public static Program load(Reader program) throws RuntimeExceptionOnLine {
		final CharStream c = new BackupCharStream( program );
		final PsyScriptParser parser = new PsyScriptParser(c);
		
		try {
			return parser.PsyScript();

		} catch ( RuntimeExceptionOnLine e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RuntimeExceptionOnLine(ParseException.getLine(parser.token), e);
		}
	}

	public void setFilename(String filename) {
		try {
			path = new File(filename).getCanonicalFile().getParent() + File.separatorChar;
		} catch (IOException e) {
			path = null;
		}
	}
	
	/**
	 * Returns the correct path for a file relative to the program's base path
	 * @param filename
	 * @return
	 */
	public String getAbsolutePath(String filename) {
		if ( path == null )
			return filename;

		return path + filename;
	}

	/**
	 * Checks if this filename exists
	 * @param filename
	 * @throws IOException
	 */
	public void checkFileExists(String filename) throws IOException {
		// Create a file reader to check we can open it
		Reader r = new FileReader( getAbsolutePath( filename ) );
		try {
			r.read();
		} finally {
			r.close();
		}
	}

	public void stop() {
		isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Does some basic checking
	 * ie looks if a main proc exists, etc
	 */
	public void check() {
		if ( ! containsProcedure("main") ) {
			throw new IllegalArgumentException("Script must have a 'main' procedure");
		}

		for ( Procedure p : procedures.values() ) {
			p.check();
		}
	}

	public boolean containsProcedure(String string) {
		return procedures.containsKey( string );
	}

	public void setLogListener(LogListener l) {
		logger = l;
	}

	public void log(final String data) {
		if (logger != null)
			logger.log(data);
	}

	public void close() {
		if (logger != null)
			logger.close();
	}
	
	public void runAction(Action a) throws Exception {
		activeAction = a;
		a.run();
	}

}
