package org.mql.java.extraction;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Extractor {
	private String workspace;

	public Extractor(String workspace) {
		this.workspace = workspace;
	}

	public List<java.lang.Class<?>> extractJavaFiles(String projectName) {
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
			List<java.lang.Class<?>> classList = Loader.loadClasses(projectBin, javaFiles);
			return classList;
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

	public void extractPackages(List<java.lang.Class<?>> classFiles) {
		Set<java.lang.Package> javaPackages = new HashSet<>();
		
		//fill javaPackages set
		for (java.lang.Class<?> c : classFiles) {
			javaPackages.add(c.getPackage());
		}
		Map<String, PackageType> packages = new HashMap<>();
		for (java.lang.Package p : javaPackages) {
			packages.put(p.getName(), new PackageType(p.getName()));//so i can reference them by name to fill them 
		}
		
		for (java.lang.Class<?> c : classFiles) {
			if (c.isAnnotation()) {
				
			} else if (c.isInterface()) {
				InterfaceType i = new InterfaceType(c.getName());
				
			} else if (c.isEnum()) {

			} else {

			}
		}
		
	}
	 

	public static void main(String[] args) {
		System.out.println(new Extractor("D:\\MQL\\Java\\MqlWorkSpace").extractJavaFiles("testProject"));
	}
}
