package com.fairsail;

import javax.xml.bind.annotation.XmlElement;

public class Note {
	private String note;
	
	public Note() {}
	
	public Note(String note) {
		this.note = note;
	}
	
	@XmlElement(name="content")
	public String getNote(){
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
}
