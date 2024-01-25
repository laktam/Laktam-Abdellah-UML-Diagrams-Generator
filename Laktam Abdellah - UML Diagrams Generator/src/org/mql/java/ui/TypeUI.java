package org.mql.java.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.sound.sampled.LineEvent;
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
	private int marginLR = 40;
	private int marginTB = 40;
	private int textBottomPadding = 3;
	private int topAndBottomPadding = 6;
	private int sidePaddings = 6;
	private List<String> attributes;// visibility attribut: Type
	private List<String> methods;// visibility method(param1: Type, param2: Type): Type_de_retour
	private int h, w, row, column;
	private String widestString;
	private double fontHeight;
	private static Plan plan;
	private Position position;
	private int cornerCounter = 25;

	public TypeUI(SuperType type, int row, int column) {
		this.attributes = new Vector<String>();
		this.methods = new Vector<String>();
//		setOpaque(true);
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
			if(rType != null) {
				if (rType instanceof Class<?>) {
					s += "): " + ((Class<?>) m.getReturnType()).getSimpleName();
				} else if (rType instanceof ParameterizedType) {
					s += "): " + FieldType.createSimpleTypeName(((ParameterizedType) m.getReturnType()), "<", ">");
				}
			}else {
				s+= "): " + m.getReturnTypeString();
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
		all.add(type.getSimpleName());
		widestString = "";
		for (String s : all) {
			if (fm.stringWidth(s) > fm.stringWidth(widestString)) {
				widestString = s;
			}
		}

		fontHeight = font.createGlyphVector(fm.getFontRenderContext(), widestString).getVisualBounds().getHeight();
		double height = (fontHeight + textBottomPadding) * (all.size()) + topAndBottomPadding * 6;

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
		w = width;
//		repaint();
//		plan.setPosition(row, column, width, h, this);
		setBounds((int) p.getX(), (int) p.getY(), width, h);// position.getDimension().height
		plan.update(column);
	}

	public static void setPlan(Plan plan) {
		TypeUI.plan = plan;
	}
	//top center of the rectangle 
	public Point getTopPoint() {
		int x = 0, y = 0;
		Point ref = getLocation();
		x = (int) ref.getX() + w / 2;
		y = (int) ref.getY() + marginTB;
		return new Point(x, y);
	}

	public Point getBottomPoint() {
		int x = 0, y = 0;
		Point ref = getLocation();
		x = (int) ref.getX() + w / 2;
		y = (int) ref.getY() + h - marginTB;
		return new Point(x, y);
	}

	public Point getLeftPoint() {
		int x = 0, y = 0;
		Point ref = getLocation();
		x = (int) ref.getX() + marginLR;
		y = (int) ref.getY() + h / 2;
		return new Point(x, y);
	}

	public Point getRightPoint() {
		int x = 0, y = 0;
		Point ref = getLocation();
		x = (int) ref.getX() + w - marginLR;
		y = (int) ref.getY() + h / 2;
		return new Point(x, y);
	}

	// 0 : top is closest => draw from bottom of this component to top of the other
	// one
	// 1 : left
	// 2 : right
	// 3 : bottom
	public int getClosestSideTo(TypeUI to) {
//		TypeUI to = plan.getPosition(row, column).getTypeUi();
		double distance0 = this.getBottomPoint().distance(to.getTopPoint());
		double distance1 = this.getRightPoint().distance(to.getLeftPoint());
		double distance2 = this.getLeftPoint().distance(to.getRightPoint());
		double distance3 = this.getTopPoint().distance(to.getBottomPoint());
		List<Double> arr = Arrays.asList(distance0, distance1, distance2, distance3);
		double minElement = Collections.min(arr);
		return arr.indexOf(minElement);
	}

	public Point[] getClosestPoints(TypeUI to) {
		double distance0 = this.getBottomPoint().distance(to.getTopPoint());
		double distance1 = this.getRightPoint().distance(to.getLeftPoint());
		double distance2 = this.getLeftPoint().distance(to.getRightPoint());
		double distance3 = this.getTopPoint().distance(to.getBottomPoint());
		List<Point[]> points = Arrays.asList(new Point[] { this.getBottomPoint(), to.getTopPoint() },
				new Point[] { this.getRightPoint(), to.getLeftPoint() },
				new Point[] { this.getLeftPoint(), to.getRightPoint() },
				new Point[] { this.getTopPoint(), to.getBottomPoint() });
		List<Double> distances = Arrays.asList(distance0, distance1, distance2, distance3);

		double minDist = Collections.min(distances);
		return points.get(distances.indexOf(minDist));
	}

	// check if point is in the inner rectangle
	public boolean containsPoint(Point p) {
		Rectangle rect = new Rectangle();
		rect.setBounds(getLocation().x, getLocation().y, w - marginLR * 2, h - marginTB * 2);
		return rect.contains(p);
	}

	public boolean containsPoint(double x, double y) {
		Rectangle rect = getInsideRectangle();
//		rect.setBounds(getLocation().x + marginLR, getLocation().y + marginTB, w - marginLR * 2, h - marginTB * 2);
		return rect.contains(x, y);
	}

	public Rectangle getInsideRectangle() {
		Rectangle rect = new Rectangle();
		rect.setBounds(getLocation().x + marginLR, getLocation().y + marginTB, w - marginLR * 2, h - marginTB * 2);
		return rect;
	}

	public Point getClosestCorner(Point p) {
		Point corners[] = getDrawingCorners();
		double distance0 = p.distance(corners[0]);
		double distance1 = p.distance(corners[1]);
		double distance2 = p.distance(corners[2]);
		double distance3 = p.distance(corners[3]);
		List<Double> distances = Arrays.asList(distance0, distance1, distance2, distance3);
		double minDist = Collections.min(distances);
		return corners[distances.indexOf(minDist)];
	}

//	public Point getClosestCorner(Point p,List<Point> cornersUsed) {
//		Point corners[] = getDrawingCorners();
//		double distance0 = p.distance(corners[0]);
//		double distance1 = p.distance(corners[1]);
//		double distance2 = p.distance(corners[2]);
//		double distance3 = p.distance(corners[3]);
//		List<Double> distances = Arrays.asList(distance0, distance1, distance2, distance3);
//		double minDist = Collections.min(distances);
//		return corners[distances.indexOf(minDist)];
//	}

	public Point getNextCorner(Point p, List<Point> cornersUsed) {
		if (cornersUsed.size() == 0) {
			return getClosestCorner(p);
		} else {
			List<Point> cornersList = new Vector<Point>();
			Point corners[] = getDrawingCorners();
			for (Point point : corners) {
				cornersList.add(point);
			}
//			List<Point> cornersList = Arrays.asList(corners);
			cornersList.removeAll(cornersUsed);// ??????
			double minDist = 100000000000.0;
			if (!cornersList.isEmpty()) {
				Point nextCorner = cornersList.get(0);
				for (Point c : cornersList) {
					if (c.distance(p) < minDist) {
						minDist = c.distance(p);
						nextCorner = c;
					}
				}
				return nextCorner;
			}

		}
		return new Point(0, 0);

	}

	public Point[] getDrawingCorners() {
//		Point corners[] = new Point[] {
//				new Point(getLocation().x + marginLR - cornerCounter, getLocation().y + marginTB - cornerCounter), // top
//																													// left
//				new Point(getLocation().x + w - (marginLR - cornerCounter), getLocation().y + marginTB - cornerCounter), // top
//																															// right
//				new Point(getLocation().x + marginLR - cornerCounter, getLocation().y + h - (marginTB - cornerCounter)), // bottom
//																															// left
//				new Point(getLocation().x + w - (marginLR - cornerCounter),
//						getLocation().y + h - (marginTB - cornerCounter)) // bottom right
//		};
//		cornerCounter += 3;
//		return corners;

		Rectangle rect = new Rectangle();
		rect.setBounds(getLocation().x + marginLR - cornerCounter, getLocation().y + marginTB - cornerCounter,
				w - marginLR * 2 + cornerCounter * 2, h - marginTB * 2 + cornerCounter * 2);
		Point corners[] = new Point[] { new Point(rect.getLocation().x, rect.getLocation().y), // top left
				new Point(rect.getLocation().x + rect.width, rect.getLocation().y), // top right
				new Point(rect.getLocation().x, rect.getLocation().y + rect.height), // bottom left
				new Point(rect.getLocation().x + rect.width, rect.getLocation().y + rect.height) // bottom right
		};
		
		return corners;

	}

	public SuperType getType() {
		return type;
	}

	public int getMarginLR() {
		return marginLR;
	}

	public int getMarginTB() {
		return marginTB;
	}

}
