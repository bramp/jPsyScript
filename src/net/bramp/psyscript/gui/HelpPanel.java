package net.bramp.psyscript.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

public class HelpPanel extends JPanel implements MouseListener {
	
	static final String stimulus[] = {
		// Stimulus
		"move cell ? to (?,?)",
		"move cell ? to (?,?) with size (?,?)",

		"load image ? into cell ?",
		"set cell ? to ?",
		"set cell ? font to ? size ?",
		
		"show cell ?",
		"hide cell ?",

		"play sound ?",
		"play sound ? without waiting",

		"play movie ? at (?,?)",
		"play movie ? at (?,?) without waiting",
		"play movie ? at (?,?) with size (?,?)",

		"background (?,?,?)",

		"pause for ? seconds",

		"display text from file ?",
		"display plain text from file ?",
		"display html text from file ?",
		"display rich text from file ?"
	};

	static final String responses[] = {
		"wait for a click in ?",
		"wait for a key in ?",
		"wait for ? second or a click in ?",
		"wait for ? second or a key in ?",
		//"flush key buffer",

		"move slider to (?,?) with size (?,?)",
		"set slider to ?",
		"set slider to ? of ? to ?",
		"show slider",
		"hide slider",
		
		"log ?",
		
		"start timer",
		"start countdown from ? seconds",

		"hide cursor",
		"show cursor",
	};
	
	static final String others[] = {
		"repeat while ? is [in] ?",
		"repeat until ? is [in] ?",
		"repeat using ? from [? rows of] ? ... end repeat",
		
		"table ? [in random order] ... end table",
		
		"set ? to ?",
		"set ? by appending ?",
		"set ? by removing ?",
		
		"if ? is [in] ? ... else ... end if",

		"call ?",
		"proc ? ... end proc",
		"abort"
	};

	static final String variables[] = {
		"$dateStamp",
		"$timeStamp",
		
		"$space",
		"$blank",
		
		"$lastClick",
		"$lastClickTime",
		"$lastKey",
		"$lastKeyTime",
		"$lastTime",
		
		"$timer",
		"$countdown",
		
		"$slider",
		"$textField"
	};
	
	private final MainGUI gui;
	
	public HelpPanel(MainGUI gui) {

		super ( new SpringLayout () );
		
		this.gui = gui;
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		
		add ( tabbedPane );

		final JList stimulusList = createListBox(stimulus);
		final JList responseList = createListBox(responses);
		final JList otherList    = createListBox(others);
		final JList variableList = createListBox(variables);

		final DoubleClickFilter filter = new DoubleClickFilter(this);
		stimulusList.addMouseListener( filter );
		responseList.addMouseListener( filter );
		otherList.addMouseListener( filter );
		variableList.addMouseListener( filter );

		final SpringLayout layout = (SpringLayout) getLayout();

        layout.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.EAST,  tabbedPane, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.WEST,  tabbedPane, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, this);

		tabbedPane.addTab( "Stimulus", stimulusList );
		tabbedPane.addTab( "Response", responseList );
		tabbedPane.addTab( "Other",    otherList );
		tabbedPane.addTab( "Variable", variableList );

        setPreferredSize( tabbedPane.getPreferredSize() );
        setMinimumSize  ( tabbedPane.getMinimumSize() );
	}
	
	
	private JList createListBox( final String entries[] ) {
		final JList list = new JList( entries );
		list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		list.setBorder( new EmptyBorder(0, 5, 0, 10) );
		return list;
	}

	public void mouseClicked(MouseEvent e) {
		if ( e.getClickCount() >= 2 && e.getSource() instanceof JList ) {
			JList list = (JList) e.getSource();
			
			String command = list.getSelectedValue().toString();
			command = command.replace( " ... ",  "\n\n\n" );
			gui.paste( command + "\n" );
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}
