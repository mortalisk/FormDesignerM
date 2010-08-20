package org.openxdata.formtools.client.cmd;

import java.util.Stack;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class CommandHistory {

	private Stack<ICommand> undoCmds = new Stack<ICommand>();
	private Stack<ICommand> redoCmds = new Stack<ICommand>();
	
	public void undo(){
		
	}
	
	public void redo(){
		
	}
	
	public boolean canUndo(){
		return undoCmds.size() > 0;
	}
	
	public boolean canRedo(){
		return redoCmds.size() > 0;
	}
	
	public String getUndoCommandName(){
		if(undoCmds.size() == 0)
			return null;
		else
			return undoCmds.lastElement().getName();
	}
	
	public String getRedoCommandName(){
		if(redoCmds.size() == 0)
			return null;
		else
			return redoCmds.lastElement().getName();
	}
}
