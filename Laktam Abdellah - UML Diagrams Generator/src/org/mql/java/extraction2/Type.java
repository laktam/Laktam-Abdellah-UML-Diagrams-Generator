package org.mql.java.extraction2;

import java.util.List;
import java.util.Vector;

public class Type {
	protected java.lang.Class<?> c;
	protected List<Relationship> relationships;
	
	public Type(java.lang.Class<?> c) {
		this.relationships = new Vector<Relationship>();
		this.c = c;
	}
	
	public java.lang.Class<?> getObjectClass() {
		return c;
	}
	
	public void addRelationship(Relationship r) {
		relationships.add(r);
	}
	
	public  List<Relationship> getRelationships(){
		return relationships;
	}
}
