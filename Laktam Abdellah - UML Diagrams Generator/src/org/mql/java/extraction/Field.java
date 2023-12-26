package org.mql.java.extraction;

public class Field {
	private String modifiers;
	private String name;
	private String type;

	Field(String modifiers, String name, String type) {
		this.modifiers = modifiers;
		this.name = name;
		this.type = type;
	}

	public String getModifiers() {
		return modifiers;
	}

	public void setModifiers(String modifiers) {
		this.modifiers = modifiers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
