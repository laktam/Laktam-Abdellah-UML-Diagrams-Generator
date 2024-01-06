package org.mql.java.extraction;


public class FieldType {
	private String modifiers;
	private String name;
	private Class<?> type;

	FieldType(String modifiers, String name, Class<?> type) {
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

	public Class<?> getType() {
		return type;
	}


}
