package org.openxdata.designer.client.vew.widget.images;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree.Resources;

/**
 * An aggragate image bundle that pulls together all the images for this
 * application into a single bundle.
 */
public interface FormDesignerImages extends Resources {

	ImageResource tasksgroup();
	ImageResource filtersgroup();
	
	ImageResource newform();
	ImageResource open();
	ImageResource save();
	ImageResource moveup();
	ImageResource movedown();
	ImageResource add();
	ImageResource addchild();
	ImageResource delete();
	ImageResource justifyleft();
	ImageResource justifyright();
	ImageResource cut();
	ImageResource copy();
	ImageResource paste();
	ImageResource alignTop();
	ImageResource alignBottom();
	ImageResource samewidth();
	ImageResource sameheight();
	ImageResource samesize();
	ImageResource undo();
	ImageResource redo();
	ImageResource refresh();
	
	ImageResource drafts();
	ImageResource markRead();
	ImageResource templates();
	ImageResource note();
	ImageResource lookup();
	
	ImageResource info();
	
	ImageResource error();
	ImageResource loading();
	
	ImageResource picture();
	
}
