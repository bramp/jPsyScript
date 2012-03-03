package net.bramp.psyscript.gui;

import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class MainToolbar extends JToolBar {

	public MainToolbar(final MainGUI gui) {

    	setFloatable(false);

    	final ActionMap actionMap = getActionMap();
    	actionMap.setParent( gui.getActionMap() );

    	add( createToolbarButton(actionMap.get("New"), KeyEvent.VK_N ) );
        add( createToolbarButton(actionMap.get("Open"), KeyEvent.VK_O ) );
        add( createToolbarButton(actionMap.get("Save"), KeyEvent.VK_S) );
        add( createToolbarButton(actionMap.get("Save As"), KeyEvent.VK_A) );
        add( createToolbarButton(actionMap.get("Test"), KeyEvent.VK_T) );
        add( createToolbarButton(actionMap.get("Run"), KeyEvent.VK_R) );
        add( createToolbarButton(actionMap.get("Options"), KeyEvent.VK_O) );
        
        // Put some glue to move the last two button to the far right
        add( Box.createHorizontalGlue() );
        
        add( createToolbarButton(actionMap.get("Help"), KeyEvent.VK_H) );
        add( createToolbarButton(actionMap.get("Exit"), KeyEvent.VK_X) );

	}
	
	protected JButton createToolbarButton(final Action action, final int mnemonic ) {
		
		assert action != null;
		
        final JButton btn = new JButton();

        btn.setAction(action);
        
       	btn.setMnemonic(mnemonic);
        btn.setFocusable( false );
        btn.setMargin( new Insets(10,10,10,10) );
        //btn.setVerticalTextPosition( AbstractButton.BOTTOM );
        //btn.setHorizontalTextPosition( AbstractButton.CENTER );
        

        btn.addMouseListener( new ToolButtonMouseListener(btn) );

        return btn;
	}
}
