package org.mql.java.extraction;

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
			List<Class<?>> classFiles = Loader.loadClasses(projectBin, javaFiles);
			// get defined packages
			Package javaPackages[] = Loader.getDefinedPackages();
			// create tree
			List<PackageType> packages = createPackagesTree(projectBin.listFiles());
			fillPackages(packages, classFiles);
			packages.forEach(System.out::println);
			//
//			List<PackageType> packages = createPackages(javaPackages);
//			fillPackages(packages, classFiles);
//			nestPackages(packages);
//			project.addPackages(packages);

			// extract relationships
//			extractRelationships(packages);
			return project;
//			System.out.println(classList.get(0).getDeclaredFields()[0].getName());
//			Set<Package> javaPackages = new HashSet<>();
//			for (Class<?> c : classList) {
//				javaPackages.add(c.getPackage());
//			}
//			for (Package p : javaPackages) {
//				project.addPackage(new Package(p.getName()));
//			}
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
	public void extractRelationships(List<Package> packages) {
		for (Package p : packages) {
//			RelationshipDetector.detectRepationShips(p.getAllTypes());
		}
	}

	public static void main(String[] args) {
		Project project = new Extractor("D:\\MQL\\Java\\MqlWorkSpace").extract("testProject");
		System.out.println(project.getName());
		System.out.println(project.getPackages());
//		System.out.println(project.getPackages().get(2).getClasses());
//		project.getRelationships().forEach((r) -> {
//			System.out.println(" - type : " + r.getType());
//			System.out.println(" - from : " + r.getFrom());
//			System.out.println(" - to : " + r.getTo() + "\n");
//
//		});

	}
}
