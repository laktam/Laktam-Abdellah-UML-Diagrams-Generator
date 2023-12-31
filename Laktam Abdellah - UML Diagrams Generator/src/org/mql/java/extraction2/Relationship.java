package org.mql.java.extraction2;

public class Relationship {
	private String type;
	private Class from;
	private Class to;
	
	public Relationship(String type, Class from, Class to) {
		this.type = type;
		this.from = from;
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class getFrom() {
		return from;
	}

	public void setFrom(Class from) {
		this.from = from;
	}

	public Class getTo() {
		return to;
	}

	public void setTo(Class to) {
		this.to = to;
	}
	
	
}
