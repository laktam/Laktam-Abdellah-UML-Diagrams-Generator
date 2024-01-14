package org.mql.java.extraction.relationships;

import java.util.Objects;

import org.mql.java.extraction.SuperType;

public class Relationship {
	private String type;
	private RelationshipEnd from;
	private RelationshipEnd to;

	// maybe delete source because it is always the class that hold the relationship
	public Relationship(String type, RelationshipEnd from, RelationshipEnd to) {
		this.type = type;
		this.from = from;
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public RelationshipEnd getFrom() {
		return from;
	}

	public RelationshipEnd getTo() {
		return to;
	}

	@Override
	public boolean equals(Object obj) {
		Relationship r2 = (Relationship) obj;
		if (to.getFQName().equals(r2.to.getFQName())
				&& from.getFQName().equals(r2.from.getFQName()) 
				&& type.equals(r2.type)) {
			return true;
		}

		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(type, from.getFQName(), to.getFQName());
	}

}
