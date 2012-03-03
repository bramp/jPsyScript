/**
 * 
 */
package net.bramp.psyscript.gui;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

/**
 * Simple action class to help abstract some of the funtionality
 * 
 * @author Andrew Brampton
 *
 */
abstract class MyAction extends AbstractAction {

    public MyAction(String name, Icon icon, KeyStroke keyStroke) {
        super(name, icon);
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    public MyAction(String name, Icon icon) {
        super(name, icon);
    }

    public MyAction(String name, KeyStroke keyStroke) {
        super(name);
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    public Action mapIt(InputMap im, ActionMap am) {
    	KeyStroke accKey = (KeyStroke) getValue(ACCELERATOR_KEY);
        if ( accKey != null )
        	im.put(accKey, getValue(NAME));

        am.put(getValue(NAME), this);
        
        return this;
    }
}