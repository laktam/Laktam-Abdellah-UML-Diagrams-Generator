package org.mql.java.extraction;

import java.util.List;

public class Interface {
	private String name;
	private List<Method> methods;
	private List<Field> fields;
	
	Interface(String name){
		this.name = name;
	}
	
	public void addField(String name, String type) {
		fields.add(new Field("public static final", name, type));
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
	
	
}
