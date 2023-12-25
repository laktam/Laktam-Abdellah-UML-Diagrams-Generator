package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class Class {
	private List<Field> fields;
	private List<Constructor> constructors;
	private List<Method> methods;

	public Class() {
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
}
