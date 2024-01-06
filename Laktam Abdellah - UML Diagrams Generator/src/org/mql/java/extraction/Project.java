package org.mql.java.extraction;

import java.util.List;
import java.util.Vector;

public class Project {
	private String name;
	private List<PackageType> packages;

	Project(String name) {
		this.name = name;
		this.packages = new Vector<PackageType>();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "Project : " + name + "\n";
		for (PackageType p : packages) {
			s += p;
		}
		return s;
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

	public void addPackages(List<PackageType> packages) {
		this.packages.addAll(packages);
	}

	public List<PackageType> getPackages() {
		return packages;
	}
	
	public List<PackageType> deleteEmptyPackages() {
		List<PackageType> toDelete = new Vector<PackageType>();
		for (PackageType p : packages) {
			if(p.isEmpty()) {
				toDelete.add(p);
			}else {
				p.deleteEmptyPackages();
			}
		}
		packages.removeAll(toDelete);
		return packages;
	}
}
