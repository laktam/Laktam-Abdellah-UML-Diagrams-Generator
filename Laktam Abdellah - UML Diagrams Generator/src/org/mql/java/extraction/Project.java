package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class Project {
	private String name;
	private List<PackageType> packages;
	
	Project(String name){
		this.name = name;
		this.packages = new Vector<PackageType>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addPackage(PackageType p) {
		packages.add(p);
	}
	
	public List<PackageType> getPackages(){
		return packages;
	}
}
