package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class Project {
	private String name;
	private List<Package> packages;
	
	Project(String name){
		this.name = name;
		this.packages = new Vector<Package>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addPackage(Package p) {
		packages.add(p);
	}
}
