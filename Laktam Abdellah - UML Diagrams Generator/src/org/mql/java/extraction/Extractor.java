package org.mql.java.extraction;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.mql.java.extraction.relationships.RelationshipDetector;
import org.mql.java.xml.Parser;

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
			// get class Files
			extractFiles(projectBin, javaFiles);//compilation separe=> this why i get the private inner class Item listener 
			// load them
			List<Class<?>> classFiles = Loader.loadClasses(projectBin, javaFiles);

			// create project tree
			//must edit ot get only used packages !!!!!
			List<PackageType> packages = createPackagesTree(projectBin.listFiles());
			fillPackages(packages, classFiles);
			project.addPackages(packages);

			// extract relationships
			extractRelationships(project);
			return project;
		}
		return null;
	}

	// pass bin.listFiles
	private List<PackageType> createPackagesTree(File files[]) {
		List<PackageType> packagesTree = new Vector<>();
		for (File f : files) {
			if (f.isDirectory()) {
				PackageType p = new PackageType(Loader.getFQName(f));
				// add subpackages
				if (f.listFiles().length > 0 && containsDirectory(f)) // is not empty and contains at least a directory
					p.addPackages(createPackagesTree(f.listFiles()));
				packagesTree.add(p);
			}
		}
		return packagesTree;
	}

	private boolean containsDirectory(File f) {
		for (File file : f.listFiles()) {
			if (file.isDirectory())
				return true;
		}
		return false;
	}

//	private List<PackageType> createPackages(Package[] javaPackages) {
//		List<PackageType> packages = new Vector<>();
//
//		for (Package p : javaPackages) {
//			packages.add(new PackageType(p.getName()));
//		}
//		return packages;
//	}

	private void fillPackages(List<PackageType> packages, List<Class<?>> classFiles) {

		Map<String, PackageType> packagesMap = new HashMap<>();// so i can reference them by fqname to fill them
		fillPackageMap(packages, packagesMap);

		for (Class<?> c : classFiles) {
			if (c.isAnnotation()) {
//				AnnotationType a = new AnnotationType(c);
//				packages.get(c.getPackage().getName()).addAnnotation(a);
			} else if (c.isInterface()) {
				InterfaceType i = new InterfaceType(c);
				packagesMap.get(c.getPackage().getName()).addInterface(i);
			} else if (c.isEnum()) {
//				EnumerationType e = new EnumerationType(c);
//				packages.get(c.getPackage().getName()).addEnumeration(e);
			} else {
				ClassType cl = new ClassType(c);
				packagesMap.get(c.getPackage().getName()).addClass(cl);
			}
		}
	}

	private void fillPackageMap(List<PackageType> packages, Map<String, PackageType> packagesMap) {
		for (PackageType p : packages) {
			packagesMap.put(p.getName(), p);
			fillPackageMap(p.getPackages(), packagesMap);
		}
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

	//
	public void extractRelationships(Project project) {
			RelationshipDetector.detectRepationShips(project.getTypes());
	}

	public static void main(String[] args) {
		Project project = new Extractor("D:\\MQL\\Java\\MqlWorkSpace").extract("p05-MultiThreading");
//		System.out.println(project);
		project.deleteEmptyPackages();
		System.out.println(project);
		
		Parser.write(project, "resources/classDiagrams.xml");
		project.getRelationships().forEach((r) -> {
			System.out.println(" - type : " + r.getType());
			System.out.println(" - from : " + r.getFrom());
			System.out.println(" - to : " + r.getTo() + "\n");

		});

	}
}
