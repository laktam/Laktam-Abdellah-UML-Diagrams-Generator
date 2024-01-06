package org.mql.java.extraction;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Vector;

public class Loader {
	public Loader() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<java.lang.Class<?>> loadClasses(File bin, List<File> javaFiles){
		List<java.lang.Class<?>> classList = new Vector<java.lang.Class<?>>();
		try (URLClassLoader loader = new URLClassLoader(new URL[] {bin.toURI().toURL()})) {
			for (File file : javaFiles) {
				classList.add(loader.loadClass(getFQName(file)));
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classList;
	}
	
	private static String getFQName(File f) {
		return f.getPath().split("bin" + "\\" + File.separator)[1].replace(File.separator, ".").replace(".class", "");
	}
}
