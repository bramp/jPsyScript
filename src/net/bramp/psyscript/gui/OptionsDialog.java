package net.bramp.psyscript.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * Allows setting of all the options
 * @author Andrew Brampton
 *
 */
public class OptionsDialog extends JDialog {

	/**
		options.put( "use_fullscreen", "true" );
		options.put( "look_and_feel", "" );
	 */

	final Options options;

	public OptionsDialog(Frame owner, Options options ) {
		super(owner, "Options", true);

		this.options = options;

		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        //setResizable(false);

		Container content = getContentPane();

        final SpringLayout layout = new SpringLayout();
        content.setLayout( layout );

        final JButton ok = new JButton("OK");
        final JButton cancel = new JButton("Cancel");

        final JPanel optPanel = new JPanel();
        
        final JComboBox look_and_feel = new JComboBox();
        final JCheckBox fullscreen = new JCheckBox();
        
        content.add( ok );
        content.add( cancel );
        content.add( optPanel );

        optPanel.setLayout(new FlowLayout());
        optPanel.add( look_and_feel );
        optPanel.add( fullscreen );

        layout.putConstraint(SpringLayout.SOUTH, cancel, -10, SpringLayout.SOUTH, content);
        layout.putConstraint(SpringLayout.EAST, cancel, -10, SpringLayout.EAST, content);

        layout.putConstraint(SpringLayout.SOUTH, ok, -10, SpringLayout.SOUTH, content);
        layout.putConstraint(SpringLayout.EAST,  ok, -10, SpringLayout.WEST, cancel);

        layout.putConstraint(SpringLayout.NORTH, optPanel, 0, SpringLayout.NORTH, content);
        layout.putConstraint(SpringLayout.EAST, optPanel, 0, SpringLayout.EAST, content);
        layout.putConstraint(SpringLayout.WEST, optPanel, 0, SpringLayout.WEST, content);
        layout.putConstraint(SpringLayout.SOUTH, optPanel, -10, SpringLayout.NORTH, ok);

        pack();
        
        setMinimumSize( getPreferredSize() );
        System.out.println(getPreferredSize());
        System.out.println(getMinimumSize());
	}
	

	/*
	try {
		LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
		Random r = new Random();
		LookAndFeelInfo look = looks [ r.nextInt( looks.length - 1) ];
		//look = UIManager.getSystemLookAndFeelClassName();
		
		UIManager.setLookAndFeel( look.getClassName() );
		SwingUtilities.updateComponentTreeUI(gui);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	*/	

}
