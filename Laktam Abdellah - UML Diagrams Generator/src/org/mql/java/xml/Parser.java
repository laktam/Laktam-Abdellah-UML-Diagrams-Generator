package org.mql.java.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mql.java.extraction.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parser {
	private static Document doc;
	private static Element classDiagrams;
	public static void write(Project project, String output) {
		
		try {
			doc = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument();
			//add a schema
			classDiagrams = doc.createElement("classDiagrams");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		List<PackageType> packages = project.deleteEmptyPackages();
		parsePackages(packages);
		doc.appendChild(classDiagrams);
		writeXml(doc, output);
	}
	
	private static void parsePackages(List<PackageType> packages) {
		for (PackageType p : packages) {
			if(p.containTypes()) {
				Element classDiagram = doc.createElement("classDiagram");
				classDiagram.setAttribute("name", p.getName());
				
				List<ClassType> classes = p.getClasses();
				for (ClassType c : classes) {
					Element classElement = classToElement(c);
					classDiagram.appendChild(classElement);
				}
				classDiagrams.appendChild(classDiagram);
			}
			//parse subpackages
			parsePackages(p.getPackages());
		
		}
	}

	private static Element classToElement(ClassType c) {
		Element classElement = doc.createElement("class");
		classElement.setAttribute("name", c.getName());
		
		List<FieldType> fields = c.getFields();
		for (FieldType f : fields) {
			Element fieldElement = doc.createElement("field");
			fieldElement.setAttribute("modifiers", f.getModifiers());
			fieldElement.setAttribute("name", f.getName());
			fieldElement.setAttribute("type", f.getType().getSimpleName());
			classElement.appendChild(fieldElement);
		}
		List<ConstructorType> constructors = c.getConstructors();
		for (ConstructorType constructor : constructors) {
			Element constructorElement = doc.createElement("constructor");
//			constructorElement.setAttribute("name", constructor.getName());
			List<ParameterType> parameters = constructor.getParameters();
			for (ParameterType param : parameters) {
				Element paramElement = doc.createElement("parameter");
				paramElement.setAttribute("name", param.getName());
				paramElement.setAttribute("type", param.getType().getSimpleName());
				constructorElement.appendChild(paramElement);
			}
			classElement.appendChild(constructorElement);
		}

		List<MethodType> methods = c.getMethods();
		for (MethodType method : methods) {
			Element methodElement = doc.createElement("method");
			methodElement.setAttribute("modifiers", method.getModifiers());
			methodElement.setAttribute("name", method.getName());
			methodElement.setAttribute("returnType", method.getReturnType().getSimpleName());

			List<ParameterType> parameters = method.getParameters();
			for (ParameterType param : parameters) {
				Element paramElement = doc.createElement("parameter");
				paramElement.setAttribute("name", param.getName());
				paramElement.setAttribute("type", param.getType().getSimpleName());
				methodElement.appendChild(paramElement);
			}
			classElement.appendChild(methodElement);
		}
		return classElement;
	}

	private static void writeXml(Document doc, String outputFile) {

		try {
//			File out = new File(outputFile);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			FileWriter writer = new FileWriter(new File(outputFile));
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
