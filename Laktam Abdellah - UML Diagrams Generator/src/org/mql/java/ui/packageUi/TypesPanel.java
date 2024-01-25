package org.mql.java.ui.packageUi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.mql.java.extraction.SuperType;

public class TypesPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<SuperType> types;
	private List<String> names;
	private int textBottomPadding = 4;
	private int padding = 6;
	private double fontHeight;
	private int w,h;
	
	public TypesPanel(List<SuperType> types) {
		this.types = types;
		this.names = new Vector<String>();
		types.forEach((t) -> {
			names.add(t.getSimpleName());
		});
		calculateSize();
//		setBorder(new LineBorder(Color.black));
	}

	public void calculateSize() {
		Font font = new Font("Segoe UI", Font.BOLD, 12);
		setFont(font);
		FontMetrics fm = getFontMetrics(font);
		// need to calculate the the longest string to get width

		String widestString = "";
		for (String s : names) {
			if (fm.stringWidth(s) > fm.stringWidth(widestString)) {
				widestString = s;
			}
		}

		fontHeight = font.createGlyphVector(fm.getFontRenderContext(), widestString).getVisualBounds().getHeight();
		double height = (fontHeight + textBottomPadding) * (names.size()) + padding * 2;

		w = fm.stringWidth(widestString) + (padding * 2);
		h = (int) height;
		setSize(w, h);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		FontMetrics fm = getFontMetrics(getFont());

		int x = padding;
		int y = (int) fontHeight + padding;
		for (String s : names) {
			g.drawString(s, x, y);
			y += fontHeight + textBottomPadding;
		}
		
		//draw top side to complete the PackageUi rectangle
		g.drawLine(0, 0, getWidth(), 0);
	}
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(w, h);
	}
}
