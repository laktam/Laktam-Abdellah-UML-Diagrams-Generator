package org.mql.java.extraction;

import java.util.List;

public class Package {
	private String name;
	private List<Class> classes;
	private List<Interface> interfaces;
	
	public void addClass(Class c) {
		classes.add(c);
	}
	
	public void addInterface(Interface i) {
		interfaces.add(i);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
