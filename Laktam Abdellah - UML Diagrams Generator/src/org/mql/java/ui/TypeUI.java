package org.mql.java.ui;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.mql.java.extraction.FieldType;
import org.mql.java.extraction.MethodType;
import org.mql.java.extraction.ParameterType;
import org.mql.java.extraction.SuperType;

public class TypeUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private SuperType type;
	private int centerX;
	private int centerY;
	private int width;
	private int height;
	private List<String> attributes;// visibility attribut: Type
	private List<String> methods;// visibility method(param1: Type, param2: Type): Type_de_retour

	public TypeUI(SuperType type) {
		this.attributes = new Vector<String>();
		this.methods = new Vector<String>();
		this.type = type;
		List<FieldType> fields = type.getFields();
		for (FieldType f : fields) {
			String s = transformModifiers(f.getModifiers());
			s += f.getFieldName() + " : " + f.getTypeSimpleName();
			attributes.add(s);
		}
		List<MethodType> methods = type.getMethods();
		for (MethodType m : methods) {
			String s = transformModifiers(m.getModifiers());
			s += m.getName() + "(";
			List<ParameterType> params = m.getParameters();
			for (int i = 0; i < params.size(); i++) {
				s += params.get(i).getName() + " : " + params.get(i).getTypeSimpleName();
				if (i < params.size() - 2) {
					s += ", ";
				}
			}

			Type rType = m.getReturnType();
			if (rType instanceof Class<?>) {
				s += "): " + ((Class<?>) m.getReturnType()).getTypeName();
			} else if (rType instanceof ParameterizedType) {
				s += "): " + FieldType.createSimpleTypeName(((ParameterizedType) m.getReturnType()), "<", ">");
				System.out.println(FieldType.createSimpleTypeName(((ParameterizedType) m.getReturnType()), "<", ">"));

			}
		}

	}

	private String transformModifiers(String modifiers) {
		String s = "";
		if (modifiers.contains("static")) {
			s += "S";// to underline
		}
		if (modifiers.contains("private")) {
			s += "-";
		} else if (modifiers.contains("public")) {
			s += "+";
		} else if (modifiers.contains("protected")) {
			s += "#";
		} else {
			s += "~";
		}
		return s;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// need to calculate the the longest string to get width
		List<String> all = new Vector<String>(attributes);
		all.addAll(methods);
		String widest = all.get(0);
		for (String s : all) {
			if (s.length() > widest.length()) {
				widest = s;
			}
		}
		System.out.println(widest);
		// change width to the actual display width of the string
		FontMetrics fm = g.getFontMetrics();
		width = fm.stringWidth(widest);
		setSize(width, getHeight());
		//

		int simpleNameWidth = 0;
		int stringAccent = 0;
		int xName = 0;
		int yName = 0;

		simpleNameWidth = fm.stringWidth(type.getSimpleName());
		stringAccent = fm.getAscent();

//		setSize(simpleNameWidth, stringAccent + 5);

		xName = getWidth() / 2 - simpleNameWidth / 2;
		yName = getHeight() / 2 + stringAccent / 2;

		// draw simpleName
		g.drawString(type.getSimpleName(), xName, yName);
		g.drawLine(0, getHeight(), getWidth(), getHeight());
	}

}
