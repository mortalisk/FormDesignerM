package org.openxdata.formtools.client.controller;



/**
 * This interface is implemented by those interested in listening to
 * events during the opening of a file from the file system.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface OpenFileDialogEventListener {
	
	/**
	 * Called when the file contents have been retrieved.
	 * 
	 * @param contents the file contents.
	 */
	public void onSetFileContents(String contents);
}
