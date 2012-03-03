import java.io.FileNotFoundException;

import net.bramp.psyscript.Program;
import net.bramp.psyscript.gui.ProgramGUI;
import net.bramp.psyscript.parser.ParseException;


public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		final String files[] = {
			///* 0 */ "test.PsyScript",
			/* 1 */ "PsyScript folder/sample scripts/one instruction timer.psyScript",
			/* 2 */ "PsyScript folder/sample scripts/test text field.psyScript",
			/* 3 */ "PsyScript folder/sample scripts/timer test.psyScript",
			/* 4 */ "PsyScript folder/sample scripts/moving image/moving.psyScript",
			/* 5 */ "PsyScript folder/sample scripts/right order/right order.psyScript",
			/* 6 */ "PsyScript folder/sample scripts/right order/three pictures.psyScript",
			/* 7 */ "PsyScript folder/sample scripts/timer calibration/timer calibration.psyScript",
			/* 8 */ "PsyScript folder/sample scripts/moving image/moving.psyScript",
			/* 9 */ "PsyScript folder/sample scripts/serial 7s/simulate starred text field.psyScript",
		};

		// Test each file parses correctly
		for (String file : files ) {
			
		    System.out.print( "\"" + file + "\"");

		    try {
		    	final Program p = Program.load(file);
				System.out.println(" OK");

				new ProgramGUI( null, p );

			} catch (Exception e) {
				System.out.println(" ERROR");
				e.printStackTrace( System.out );
				break;
			}

		}
	}

}
