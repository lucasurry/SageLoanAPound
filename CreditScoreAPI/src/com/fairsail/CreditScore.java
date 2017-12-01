package com.fairsail;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreditScore {
	private int creditScore;
	private List<Note> notes = new ArrayList<Note>();
	
	public CreditScore() {}
	
	@XmlElement(name="userScore")
	public int getCreditScore() {
		return creditScore;
	}
	
	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}
	
	@XmlElementWrapper(name="notes")
	@XmlElements({ @XmlElement(name="note", type=Note.class) })
	public List<Note> getNotes(){
		return notes;
	}
	
	public void addNote(Note note) {
		notes.add(note);
	}
}
