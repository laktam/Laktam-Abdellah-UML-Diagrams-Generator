package org.mql.java.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mql.java.extraction.*;
import org.mql.java.extraction.relationships.Relationship;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parser {
	private static Document doc;
	private static Element classDiagrams;

	public static void write(Project project, String output) {

		try {
			doc = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument();
			// add a schema
			classDiagrams = doc.createElement("classDiagrams");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

//		List<PackageType> packages = project.deleteEmptyPackages();
		List<PackageType> packages = project.getPackages();
		parsePackages(packages);
		parseRelationships(project.getRelationshipsSet());
		doc.appendChild(classDiagrams);
		writeXml(doc, output);
	}

	private static void parsePackages(List<PackageType> packages) {
		for (PackageType p : packages) {
			if (p.containTypes()) {
				Element classDiagram = doc.createElement("classDiagram");
				classDiagram.setAttribute("name", p.getFQName());

				List<ClassType> classes = p.getClasses();
				for (ClassType c : classes) {
					Element classElement = classToElement(c);
					classDiagram.appendChild(classElement);
				}
				classDiagrams.appendChild(classDiagram);
			}
			// parse subpackages
			parsePackages(p.getPackages());

		}
	}

	private static Element classToElement(ClassType c) {
		Element classElement = doc.createElement("class");
		classElement.setAttribute("name", c.getSimpleName());

		List<FieldType> fields = c.getFields();
		for (FieldType f : fields) {
			Element fieldElement = doc.createElement("field");
			fieldElement.setAttribute("modifiers", f.getModifiers());
			fieldElement.setAttribute("name", f.getFieldName());
			fieldElement.setAttribute("type", f.getTypeSimpleName());
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
				paramElement.setAttribute("type", param.getTypeSimpleName());
				constructorElement.appendChild(paramElement);
			}
			classElement.appendChild(constructorElement);
		}

		List<MethodType> methods = c.getMethods();
		for (MethodType method : methods) {
			Element methodElement = doc.createElement("method");
			methodElement.setAttribute("modifiers", method.getModifiers());
			methodElement.setAttribute("name", method.getName());
			Type rT = method.getReturnType();
			if (rT instanceof Class<?>) {
				methodElement.setAttribute("returnType", ((Class<?>) method.getReturnType()).getSimpleName());
			} else if (rT instanceof ParameterizedType) {
				String nameWithParameters = FieldType.createSimpleTypeName((ParameterizedType) rT, "(", ")");
				methodElement.setAttribute("returnType", nameWithParameters);
			}

			List<ParameterType> parameters = method.getParameters();
			for (ParameterType param : parameters) {
				Element paramElement = doc.createElement("parameter");
				paramElement.setAttribute("name", param.getName());
				paramElement.setAttribute("type", param.getTypeSimpleName());
				methodElement.appendChild(paramElement);
			}
			classElement.appendChild(methodElement);
		}
		return classElement;
	}

	private static void parseRelationships(Set<Relationship> relationships) {
		Element relationshipsE = doc.createElement("relationships");
		for (Relationship r : relationships) {
			Element rE = doc.createElement("relationship");
			rE.setAttribute("type", r.getType());
			rE.setAttribute("from", r.getFrom().getSimpleName());
			rE.setAttribute("to", r.getTo().getSimpleName());
			relationshipsE.appendChild(rE);
		}
		classDiagrams.appendChild(relationshipsE);
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
