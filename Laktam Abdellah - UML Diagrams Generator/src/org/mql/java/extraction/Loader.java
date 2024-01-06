package org.mql.java.extraction;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Vector;

public class Loader {
	private static Package definedPackages[];
	public Loader() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<Class<?>> loadClasses(File bin, List<File> javaFiles){
		List<java.lang.Class<?>> classList = new Vector<Class<?>>();
		try (URLClassLoader loader = new URLClassLoader(new URL[] {bin.toURI().toURL()})) {
			for (File file : javaFiles) {
				classList.add(loader.loadClass(getFQName(file)));
			}
			definedPackages =loader.getDefinedPackages();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classList;
	}
	
	public static Package[] getDefinedPackages() {
		return definedPackages;
	}
	
	public static String getFQName(File f) {
		return f.getPath().split("bin" + "\\" + File.separator)[1].replace(File.separator, ".").replace(".class", "");
	}
}
