package org.openxdata.designer.client.cmd;


/**
 * Encapsulates an undoable or redoable command.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface ICommand {
	String getName();
	void execute();
}
