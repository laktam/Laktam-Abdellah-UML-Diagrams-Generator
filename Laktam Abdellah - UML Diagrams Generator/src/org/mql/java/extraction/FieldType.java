package org.mql.java.extraction;

import java.lang.reflect.Type;

public class FieldType {
	private String modifiers;
	private String name;
	private Type type;
	
	FieldType(String modifiers, String name, Type type) {
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
	}



	public String getModifiers() {
		return modifiers;
	}

//	public void setModifiers(String modifiers) {
//		this.modifiers = modifiers;
//	}

	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}

	public Type getType() {
		return type;
	}


}
