package org.mql.java.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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
import org.w3c.dom.NodeList;

public class Parser {
	private static Document doc;
	private static Element classDiagrams;

	public static Project read(String source) {
		try {
			doc = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().parse(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		classDiagrams = doc.getDocumentElement();
		Project project = new Project(classDiagrams.getAttribute("name"));
		// get packages
		List<PackageType> packages = new Vector<PackageType>();
		NodeList packagesElements = classDiagrams.getElementsByTagName("classDiagram");
		for (int i = 0; i < packagesElements.getLength(); i++) {
			Element pckgE = (Element) packagesElements.item(i);
			PackageType pckgType = new PackageType(pckgE.getAttribute("name"));
			// classes
			List<ClassType> classes = new Vector<ClassType>();
			NodeList classesElements = pckgE.getElementsByTagName("class");
			for (int j = 0; j < classesElements.getLength(); j++) {
				Element classE = (Element) classesElements.item(j);
				String classFQName = pckgE.getAttribute("name") + "." + classE.getAttribute("name");
				ClassType c = new ClassType(classFQName, classE.getAttribute("name"));
				// fields
				List<FieldType> fieldsTypes = new Vector<FieldType>();
				NodeList fieldsElements = classE.getElementsByTagName("field");
				for (int f = 0; f < fieldsElements.getLength(); f++) {
					Element fieldE = (Element) fieldsElements.item(f);
					FieldType fType = new FieldType(fieldE.getAttribute("name"), fieldE.getAttribute("modifiers"),
							fieldE.getAttribute("type"));
					fieldsTypes.add(fType);
				}
				c.setFields(fieldsTypes);
				// methodes
				List<MethodType> methods = new Vector<MethodType>();
				NodeList methodsElements = classE.getElementsByTagName("method");
				for (int m = 0; m < methodsElements.getLength(); m++) {
					Element methodE = (Element) methodsElements.item(m);
					MethodType mType = new MethodType(methodE.getAttribute("name"), methodE.getAttribute("modifiers"),
							methodE.getAttribute("returnType"));
					// parameters
					List<ParameterType> params = new Vector<ParameterType>();
					NodeList paramsElements = methodE.getElementsByTagName("parameter");
					for (int p = 0; p < paramsElements.getLength(); p++) {
						Element paramE = (Element) paramsElements.item(p);
						ParameterType param = new ParameterType(paramE.getAttribute("name"),
								paramE.getAttribute("type"));
						params.add(param);
					}
					mType.setParameters(params);
					methods.add(mType);
				}
				c.setMethods(methods);
				// relationships
				List<Relationship> relationships = new Vector<Relationship>();
				Element relationsRoot = (Element) classE.getElementsByTagName("relationships").item(0);
				NodeList relationsElements = relationsRoot.getElementsByTagName("relationship");
				for (int r = 0; r < relationsElements.getLength(); r++) {
					Element relationE = (Element) relationsElements.item(r);
					Relationship relationship = new Relationship(relationE.getAttribute("type"),
							relationE.getAttribute("from"), relationE.getAttribute("to"));
					c.addRelationship(relationship);
				}
				classes.add(c);

			}
			// interfaces
			List<InterfaceType> interfaces = new Vector<InterfaceType>();
			NodeList intefacesElements = pckgE.getElementsByTagName("interface");
			for (int in = 0; in < intefacesElements.getLength(); in++) {
				Element interfaceE = (Element) intefacesElements.item(in);
				InterfaceType interfaceType = readInterface(interfaceE, pckgE.getAttribute("name"));
				interfaces.add(interfaceType);
			}

			pckgType.setClasses(classes);
			pckgType.setInterfaces(interfaces);
			packages.add(pckgType);
		}
		project.setPackages(packages);

		return project;
	}

	private static InterfaceType readInterface(Element interfaceE, String pckgName) {
		String fqName = pckgName + "." + interfaceE.getAttribute("name");
		InterfaceType interfaceType = new InterfaceType(interfaceE.getAttribute("name"), fqName);
		//methods
		List<MethodType> methods = new Vector<MethodType>();
		NodeList methodsElements = interfaceE.getElementsByTagName("method");
		for (int m = 0; m < methodsElements.getLength(); m++) {
			Element methodE = (Element) methodsElements.item(m);
			MethodType mType = new MethodType(methodE.getAttribute("name"), methodE.getAttribute("modifiers"),
					methodE.getAttribute("returnType"));
			// parameters
			List<ParameterType> params = new Vector<ParameterType>();
			NodeList paramsElements = methodE.getElementsByTagName("parameter");
			for (int p = 0; p < paramsElements.getLength(); p++) {
				Element paramE = (Element) paramsElements.item(p);
				ParameterType param = new ParameterType(paramE.getAttribute("name"), paramE.getAttribute("type"));
				params.add(param);
			}
			mType.setParameters(params);
			methods.add(mType);
		}
		interfaceType.setMethods(methods);
		// relationships
		List<Relationship> relationships = new Vector<Relationship>();
		Element relationsRoot = (Element) interfaceE.getElementsByTagName("relationships").item(0);
		NodeList relationsElements = relationsRoot.getElementsByTagName("relationship");
		for (int r = 0; r < relationsElements.getLength(); r++) {
			Element relationE = (Element) relationsElements.item(r);
			Relationship relationship = new Relationship(relationE.getAttribute("type"),
					relationE.getAttribute("from"), relationE.getAttribute("to"));
			interfaceType.addRelationship(relationship);
		}
		return interfaceType;
		
	}

	public static void write(Project project, String output) {

		try {
			doc = DocumentBuilderFactory.newDefaultInstance().newDocumentBuilder().newDocument();
			// add a schema
			classDiagrams = doc.createElement("classDiagrams");
			classDiagrams.setAttribute("name", project.getName());
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
				// interfaces
				List<InterfaceType> interfaces = p.getInterfaces();
				for (InterfaceType i : interfaces) {
					Element iElement = interfaceToElement(i);
					classDiagram.appendChild(iElement);
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
		// add relationships to each element
		Set<Relationship> relationships = c.getRelationshipsSet();
		Element relationshipsElement = parseRelationships(relationships);
		classElement.appendChild(relationshipsElement);
		return classElement;
	}

	// intefaces
	private static Element interfaceToElement(InterfaceType i) {
		Element interfaceElement = doc.createElement("interface");
		interfaceElement.setAttribute("name", i.getSimpleName());

		List<MethodType> methods = i.getMethods();
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
			interfaceElement.appendChild(methodElement);
		}
		// add relationships to each element
		Set<Relationship> relationships = i.getRelationshipsSet();
		Element relationshipsElement = parseRelationships(relationships);
		interfaceElement.appendChild(relationshipsElement);
		return interfaceElement;
	}

	private static Element parseRelationships(Set<Relationship> relationships) {
		Element relationshipsE = doc.createElement("relationships");
		for (Relationship r : relationships) {
			Element rE = doc.createElement("relationship");
			rE.setAttribute("type", r.getType());
			rE.setAttribute("from", r.getFrom().getFQName());
			rE.setAttribute("to", r.getTo().getFQName());
			relationshipsE.appendChild(rE);
		}
		return relationshipsE;
//		classDiagrams.appendChild(relationshipsE);
	}
//	private static void parseRelationships(Set<Relationship> relationships) {
//		Element relationshipsE = doc.createElement("relationships");
//		for (Relationship r : relationships) {
//			Element rE = doc.createElement("relationship");
//			rE.setAttribute("type", r.getType());
//			rE.setAttribute("from", r.getFrom().getSimpleName());
//			rE.setAttribute("to", r.getTo().getSimpleName());
//			relationshipsE.appendChild(rE);
//		}
//		classDiagrams.appendChild(relationshipsE);
//	}

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
