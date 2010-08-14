package org.openxdata.forms.client.model;

import java.util.Date;


/**
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormDataHeader {

	private String id;
	private String description;
	private Date dateCreated;
	private Date dateLastChanged;
	
	
	public FormDataHeader(){
		
	}
	
	public FormDataHeader(String id, String description, Date dateCreated,
			Date dateLastChanged) {
		super();
		this.id = id;
		this.description = description;
		this.dateCreated = dateCreated;
		this.dateLastChanged = dateLastChanged;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getDateCreated() {
		return dateCreated;
	}


	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	public Date getDateLastChanged() {
		return dateLastChanged;
	}


	public void setDateLastChanged(Date dateLastChanged) {
		this.dateLastChanged = dateLastChanged;
	}
}
