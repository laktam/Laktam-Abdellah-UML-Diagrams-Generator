package org.mql.java.ui.packageUi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.mql.java.extraction.PackageType;
import org.mql.java.extraction.SuperType;

public class Header extends JPanel {
	private static final long serialVersionUID = 1L;
	private String name;
	private int padding = 5;
	private int titleW;
	private int titleH;

	public Header(String name) {
		this.name = name;
		calculateTitleSize();
	}

	public void calculateTitleSize() {
		Font font = new Font("Segoe UI", Font.BOLD, 12);
		setFont(font);
		FontMetrics fm = getFontMetrics(font);
		double fontHeight = font.createGlyphVector(fm.getFontRenderContext(), name).getVisualBounds().getHeight();
		int nameWidth = fm.stringWidth(name);
		titleW = nameWidth + padding * 2;
		titleH = (int) fontHeight + padding * 2;
//		setSize(titleW, titleH);
//		setPreferredSize(new Dimension(titleW, titleH));
		setBorder(new LineBorder(Color.black));
		setBounds(getX(), getY(), titleW, titleH );
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Font font = new Font("Segoe UI", Font.BOLD, 12);
		g.setFont(font);
		// paint name
//		g.drawRect(getX(), getY(), titleW, titleH);
		g.drawString(name, padding, titleH - padding); // titleH count also top and bottom paddings
	}
	
	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return new Dimension(titleW, titleH );
	}

}
