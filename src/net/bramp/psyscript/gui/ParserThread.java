/**
 * 
 */
package net.bramp.psyscript.gui;

public class ParserThread extends Thread {

	private final MainGUI mainGUI;

	private static final long PARSE_WAITTIME = 1000;

	protected boolean hasUpdated = true;
	protected long wakeUpTime = 0;

	public ParserThread(MainGUI mainGUI) {
		if (mainGUI == null)
			throw new IllegalArgumentException("mainGUI can not be null");

		this.mainGUI = mainGUI;

		// Make a background thread
		setDaemon(true);
	}

	/**
	 * Prepare to make a change
	 */
	public synchronized void prepare() {
		hasUpdated = false;
		wakeUpTime = System.currentTimeMillis() + PARSE_WAITTIME;
		this.notify();
	}
	
	public long timeToWait() {
		return wakeUpTime - System.currentTimeMillis();
	}

	@Override
	public void run() {
		super.run();

		// Loop waiting to parse
		while ( true ) {
			synchronized (this) {
				try {
					// Sleep until we need updating
					while ( hasUpdated ) {
						wait();
					}

					// Now wait until the timeout has expired
					long timeToWait = timeToWait();
					while ( timeToWait > 0 ) {
						wait(timeToWait);
						timeToWait = timeToWait();
					}

					// we can Parse (inside a Swing GUI thread)
			        javax.swing.SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	mainGUI.parse();
			            }
			        });

					hasUpdated = true;

				} catch (InterruptedException e) {}
			}
		}
	}
}