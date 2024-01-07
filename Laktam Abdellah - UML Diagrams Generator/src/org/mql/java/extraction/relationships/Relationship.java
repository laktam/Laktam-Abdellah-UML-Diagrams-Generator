package org.mql.java.extraction.relationships;

import org.mql.java.extraction.Type;

public class Relationship {
	private String type;
	private Type from;
	private Type to;
	
	//maybe delete source because it is always the class that hold the relationship
	public Relationship(String type, Type from, Type to) {
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

	public Type getFrom() {
		return from;
	}

	public void setFrom(Type from) {
		this.from = from;
	}

	public Type getTo() {
		return to;
	}

	public void setTo(Type to) {
		this.to = to;
	}
	
	
}
