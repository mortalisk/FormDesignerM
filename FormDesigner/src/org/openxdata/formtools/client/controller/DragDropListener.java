package org.openxdata.formtools.client.controller;

import org.openxdata.formtools.client.widget.DesignWidgetWrapper;

import com.google.gwt.user.client.ui.Widget;


/**
 * Interface implemented by classes that want to listen to drag and drop events.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface DragDropListener {
	
	/**
	 * Called when a widget has been dropped.
	 * 
	 * @param widget the dropped widget.
	 */
	public void onDragStart(Widget widget);
	
	/**
	 * Called when the drag operation has ended.
	 * 
	 * @param widget the widget whose drag operation has ended.
	 */
	public void onDragEnd(Widget widget);
	
	/**
	 * Called when a widget has been dropped. This event is mostly used for those
	 * listening to drop operations for widgets from the palette.
	 * 
	 * @param widget the dropped widget.
	 * @param x the x coordinate where the widget has been dropped.
	 * @param y the y coordinate where the widget has been dropped.
	 * @return the design widget which has been created after the drop operation.
	 */
	public DesignWidgetWrapper onDrop(Widget widget,int x, int y);
}
