package org.openxdata.formtools.server.util;

import java.io.File;


/**
 * Utility class for listing names of files in a directory.
 * Used when creating the HTML 5 off-line cache manifest to have names of the GWT
 * generated files especially because these names keep changing often on compilation.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FileListUtil {

	public static void listFiles(String prefix, String folderName){

		File folder = new File(folderName);
		if(folder == null)
			return;

		for(File file : folder.listFiles())
			System.out.println(prefix + file.getName());
	}
}
