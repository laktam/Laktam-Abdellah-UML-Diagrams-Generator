package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

import org.mql.java.extraction.relationships.Relationship;

public class Type {
	protected String name;
	protected String modifiers;
	protected List<FieldType> fields;
	protected List<ConstructorType> constructors;
	protected List<MethodType> methods;
	
	protected List<Relationship> relationships;

	public Type() {
		this.relationships = new Vector<Relationship>();
		this.fields = new Vector<FieldType>();
		this.constructors = new Vector<ConstructorType>();
		this.methods = new Vector<MethodType>();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	public void addRelationship(Relationship r) {
		relationships.add(r);
	}
	//default getters need to return an empty list !!
	public List<Relationship> getRelationships(){
		return relationships;
	}
	
	public String getName() {
		return name;
	}
	
	public String getModifiers() {
		return modifiers;
	}
	
	public List<FieldType> getFields() {
		return fields;
	}
	
	public List<ConstructorType> getConstructors() {
		return constructors;
	}
	
	public List<MethodType> getMethods() {
		return methods;
	} 
	
	
}
