package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class Class {
	private String name;
	private String modifiers;
	private List<Field> fields;
	private List<Constructor> constructors;
	private List<Method> methods;

	public Class(String name, String modifiers) {
		this.name = name;
		this.modifiers = modifiers;
		this.fields = new Vector<Field>();
		this.constructors = new Vector<Constructor>();
		this.methods = new Vector<Method>();
	}

	public void addField(String modifiers, String name, String type) {
		fields.add(new Field(modifiers, name, type));
	}
	
	public void addConstructor(Constructor constructor) {
		constructors.add(constructor);
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}
  
	
}
