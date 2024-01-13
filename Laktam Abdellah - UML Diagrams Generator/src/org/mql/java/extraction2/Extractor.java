package org.mql.java.extraction2;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class Extractor {
	private String workspace;

	public Extractor(String workspace) {
		this.workspace = workspace;
	}

	public Project extract(String projectName) {
		Project project = new Project(projectName);
		File workspaceFile = new File(workspace);
		if (workspaceFile.isDirectory()) {
			File projectFile = new File(workspace + File.separator + projectName);
			File projectBin = new File(projectFile + File.separator + "bin");
			// extract all files
			List<File> javaFiles = new Vector<File>();
			// get Files
			extractFiles(projectBin, javaFiles);
			// load them
			List<java.lang.Class<?>> classFiles = Loader.loadClasses(projectBin, javaFiles);
			//
			List<Package> packages = extractPackages(classFiles);
			project.addPackages(packages);
			// extract relationships
			extractRelationships(packages);
			return project;
//			System.out.println(classList.get(0).getDeclaredFields()[0].getName());
//			Set<java.lang.Package> javaPackages = new HashSet<>();
//			for (java.lang.Class<?> c : classList) {
//				javaPackages.add(c.getPackage());
//			}
//			for (java.lang.Package p : javaPackages) {
//				project.addPackage(new Package(p.getName()));
//			}
		}
		return null;
	}

	// extract all .class files
	private void extractFiles(File root, List<File> fileList) {// takes project bin
		File files[] = root.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				fileList.add(f);
			} else {
				extractFiles(f, fileList);
			}
		}
	}

	public List<Package> extractPackages(List<java.lang.Class<?>> classFiles) {
		Set<java.lang.Package> javaPackages = new HashSet<>();
		// fill javaPackages set
		for (java.lang.Class<?> c : classFiles) {
			javaPackages.add(c.getPackage());
			c.getPackage();
		}
		
		Map<String, Package> packages = new HashMap<>();
		for (java.lang.Package p : javaPackages) {
			packages.put(p.getName(), new Package(p.getName()));// so i can reference them by name to fill them
		}

		for (java.lang.Class<?> c : classFiles) {
			if (c.isAnnotation()) {
				Annotation a = new Annotation(c);
				packages.get(c.getPackage().getName()).addAnnotation(a);
			} else if (c.isInterface()) {
				Interface i = new Interface(c);
				packages.get(c.getPackage().getName()).addInterface(i);
			} else if (c.isEnum()) {
				Enumeration e = new Enumeration(c);
				packages.get(c.getPackage().getName()).addEnumeration(e);
			} else {
				Class cl = new Class(c);
				packages.get(c.getPackage().getName()).addClass(cl);
			}
		}

		// get packages from map
		List<Package> ps = new Vector<Package>();
		for (Entry<String, Package> entry : packages.entrySet()) {
			ps.add(entry.getValue());
		}
		return ps;
	}

	//
	public void extractRelationships(List<Package> packages) {
		for (Package p : packages) {
			RelationshipDetector.detectRepationShips(p.getAllTypes());
		}
	}

	public static void main(String[] args) {
		Project project = new Extractor("D:\\MQL\\Java\\MqlWorkSpace").extract("testProject");
		System.out.println(project.getName());
		System.out.println(project.getPackages());
		System.out.println(project.getPackages().get(2).getClasses());
		project.getRelationships().forEach((r) -> {
			System.out.println(" - type : " + r.getType());
			System.out.println(" - from : " + r.getFrom());
			System.out.println(" - to : " + r.getTo() + "\n");

		});

	}
}
