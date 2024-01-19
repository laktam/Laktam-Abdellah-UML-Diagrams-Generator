package org.mql.java.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
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
	private int marginLR = 25;
	private int marginTB = 25;
	private int textBottomPadding = 3;
	private int topAndBottomPadding = 6;
	private int sidePaddings = 5;
	private List<String> attributes;// visibility attribut: Type
	private List<String> methods;// visibility method(param1: Type, param2: Type): Type_de_retour
	private int h, w, row, column;
	private String widestString;
	private double fontHeight;
	private static Plan plan = new Plan(5, 6);
	private Position position;

	public TypeUI(SuperType type, int row, int column) {
		this.attributes = new Vector<String>();
		this.methods = new Vector<String>();
//		setBorder(BorderFactory.createLineBorder(Color.black));

		this.type = type;
		List<FieldType> fields = type.getFields();
		for (FieldType f : fields) {
			String s = transformModifiers(f.getModifiers());
			s += f.getFieldName() + " : " + f.getTypeSimpleName();
			attributes.add(s);
		}
		List<MethodType> ms = type.getMethods();
		for (MethodType m : ms) {
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
				s += "): " + ((Class<?>) m.getReturnType()).getSimpleName();
			} else if (rType instanceof ParameterizedType) {
				s += "): " + FieldType.createSimpleTypeName(((ParameterizedType) m.getReturnType()), "<", ">");
			}
			methods.add(s);
		}
		calculateSize();
		this.row = row;
		this.column = column;
		plan.setPosition(row, column, w, h, this);
		this.position = plan.getPosition(row, column);
		Point p = plan.getDrawingPosition(row, column);
		setBounds((int) p.getX(), (int) p.getY(), position.getDimension().width, position.getDimension().height);
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

	private void calculateSize() {
		Font font = new Font("Segoe UI", Font.BOLD, 12);
		setFont(font);
		FontMetrics fm = getFontMetrics(font);
		// need to calculate the the longest string to get width
		List<String> all = new Vector<String>(attributes);
		all.addAll(methods);
		widestString = "";
		for (String s : all) {
			if (fm.stringWidth(s) > fm.stringWidth(widestString)) {
				widestString = s;
			}
		}

		fontHeight = font.createGlyphVector(fm.getFontRenderContext(), widestString).getVisualBounds().getHeight();
		double height = (fontHeight + textBottomPadding) * (all.size() + 1) + topAndBottomPadding * 6;

		w = fm.stringWidth(widestString) + (sidePaddings * 2) + marginLR * 2;
		h = (int) height + marginTB * 2;
		setSize(w, h); 
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		g.setFont(new Font("Segoe UI", Font.BOLD, 13));
		FontMetrics fm = g.getFontMetrics();
//		Font font = g.getFont();

		// List<String> all = new Vector<String>(attributes);
//		all.addAll(methods);
//		String widest = "";
//		for (String s : all) {
//			if (fm.stringWidth(s) > fm.stringWidth(widest)) {
//				widest = s;
//			}
//		}
		// change width to the actual display width of the biggest string

		//
//		double fontHeight = font.createGlyphVector(fm.getFontRenderContext(), widest).getVisualBounds().getHeight();
//		double height = (fontHeight + textBottomPadding) * (all.size() + 1) + topAndBottomPadding * 6;
//
//		w = fm.stringWidth(widest) + (sidePaddings * 2) + margin * 2;
//		h = (int) height + margin * 2;
//		setSize(w, h);
		//

		int simpleNameWidth = 0;
		int stringHeight = 0;
		int xName = 0;
		int yName = 0;

		simpleNameWidth = fm.stringWidth(type.getSimpleName());
		stringHeight = (int) Math.ceil(fontHeight);

		xName = getWidth() / 2 - simpleNameWidth / 2;
		yName = topAndBottomPadding + stringHeight + marginTB;

		// draw simpleName
		g.drawString(type.getSimpleName(), xName, yName);
		int yLine = yName + topAndBottomPadding + textBottomPadding;
		g.drawLine(0 + marginLR, yLine, getWidth() - marginLR, yLine);

		// draw attributes
		int x = sidePaddings + marginLR;
		int y = yLine + topAndBottomPadding + stringHeight;
		for (String attribute : attributes) {
			if (attribute.startsWith("S")) {// static
				g.drawString(attribute.substring(1), x, y);
				g.drawLine(x, y + 2, fm.stringWidth(attribute.substring(1)) + sidePaddings + marginLR, y + 2);// fm.stringWidth(attribute)
			} else {
				g.drawString(attribute, x, y);
			}
			y += stringHeight + textBottomPadding;
		}

		// draw methods
		y = y - stringHeight - textBottomPadding;
		y += topAndBottomPadding;
		g.drawLine(0 + marginLR, y, getWidth() - marginLR, y);
		y += topAndBottomPadding + stringHeight;// + textBottomPadding
		for (String method : methods) {
			if (method.startsWith("S")) {// static
				g.drawString(method.substring(1), x, y);
				g.drawLine(x, y + 2, fm.stringWidth(method.substring(1)) + sidePaddings + marginLR, y + 2);
			} else {
				g.drawString(method, x, y);
			}
			y += stringHeight + textBottomPadding;
		}
		g.drawRect(marginLR, marginTB, getWidth() - marginLR * 2, getHeight() - marginTB * 2);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public void setWidth(int width) {
		Point p = plan.getDrawingPosition(row, column);
		marginLR += (width - w) / 2;
		repaint();
//		plan.setPosition(row, column, width, h, this);
		setBounds((int) p.getX(), (int) p.getY(), width, h);//position.getDimension().height
	}

}
