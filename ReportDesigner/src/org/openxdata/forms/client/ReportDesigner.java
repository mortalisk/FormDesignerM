package org.openxdata.forms.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ReportDesigner implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
    Button button = new Button("Click me");
    
    // We can add style names
    button.addStyleName("pc-template-btn");
    // or we can set an id on a specific element for styling
    img.getElement().setId("pc-template-img");
    
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.setWidth("100%");
    vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    vPanel.add(img);
    vPanel.add(button);

    // Add image and button to the RootPanel
    RootPanel.get().add(vPanel);

    // Create the dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Welcome to GWT!");
    dialogBox.setAnimationEnabled(true);
    Button closeButton = new Button("close");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.setWidth("100%");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    dialogVPanel.add(closeButton);

    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.hide();
      }
    });

    // Set the contents of the Widget
    dialogBox.setWidget(dialogVPanel);
    
    button.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.center();
        dialogBox.show();
      }
    });
  }
}
