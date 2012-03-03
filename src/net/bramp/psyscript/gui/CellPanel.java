package net.bramp.psyscript.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CellPanel {

	final String name;

	final ProgramGUI gui;
	final Rectangle pos;        // Pos the user thinks we are
	boolean userSpecifiedPos = false;

	Rectangle screenPos = null; // The real screen pos

	boolean visible = false;

	// The image to draw
	BufferedImage image = null;
	String text = null;
	Font font = null;

	final boolean vcenter = true;
	final boolean hcenter = true;

	public CellPanel(ProgramGUI gui, String name) {
		this.gui = gui;
		this.name = name;

		pos = new Rectangle(0, 0, 100, 100);
	}

	// Causes the parent gui to repaint us
	void repaint() {
		EventQueue.invokeLater( new Runnable() {
			public void run() {
				gui.repaint();
			};
		} );
	}

	public void setImage(String filename) throws IOException {
		text = null;
		image = ImageFactory.getImage( filename );

		// Reset the Cell's width/height
		setBounds(pos.x , pos.y, image.getWidth(), image.getHeight());
		userSpecifiedPos = false;

		if ( visible )
			repaint();
	}

	public void setLocation(int x, int y) {
		pos.setLocation(x, y);
		screenPos = null;

		if ( visible )
			repaint();
	}

	public void setBounds(int x, int y, int width, int height) {
		userSpecifiedPos = true;

		pos.setBounds(x, y, width, height);
		screenPos = null;

		// If this is text, then blank the image
		if ( text != null )
			image = null;

		if ( visible )
			repaint();
	}

	public void setVisible(boolean flag) {
		if ( visible != flag ) {
			visible = flag;
			repaint();
		}
	}

	public boolean isVisible() {
		return visible;
	}

	/**
	 * Given a string will add newlines where it would wrap
	 * @param g
	 * @param string
	 * @param width
	 * @return
	 */
	protected static String wordWrap(Graphics g, String string, int width) {
		FontMetrics f = g.getFontMetrics();

		StringBuffer s = new StringBuffer(string);

		int p = s.indexOf(" ");
		int lastLine = 0;

		while ( p != -1 ) {
			String line = s.substring(lastLine, p);

			// Work out the length of the line
			Rectangle2D b = f.getStringBounds(line, g);

			// If this line is too long, break and start on next line
			if (b.getWidth() > width) {
				s.replace(p, p + 1, "\n");
				lastLine = p;
			}

			// Find next space
			p = s.indexOf(" ", p+1);
		}

		return s.toString();
	}

	public Image getImage() {
		if ( text != null && image == null ) {
			// Create the text image
			image = new BufferedImage(pos.width, pos.height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();

			if ( font != null )
				g.setFont(font);

			// If the user didn't set the size, then we should resize
			if ( !userSpecifiedPos ) {
				Rectangle2D b = g.getFontMetrics().getStringBounds(text, g);
				
				if ( b.getWidth() > gui.getWidth()) {
					String wrapText = wordWrap(g, text, gui.getWidth());
					b = g.getFontMetrics().getStringBounds(wrapText, g);
				}
				
				setBounds(pos.x , pos.y, (int)b.getWidth(), g.getFontMetrics().getHeight() );
				g.dispose();

				// We have to recreate the image, with the new bounds
				image = new BufferedImage(pos.width, pos.height, BufferedImage.TYPE_INT_RGB);
				g = image.createGraphics();

				if ( font != null )
					g.setFont(font);
			}

			String wrapText = wordWrap(g, text, pos.width);

			g.setBackground(Color.white);
			g.setColor( g.getBackground() );
			g.fillRect(0, 0, pos.width, pos.height);

			g.setColor(Color.black);

			final String[] lines = wrapText.split("\n");
			final FontMetrics f = g.getFontMetrics();
			int height = f.getHeight();
			int y = ( pos.height - (lines.length * height) ) / 2 + height;

			for (String line : lines ) {
				int x = 0;
				Rectangle2D b = f.getStringBounds(line, g);

				if ( hcenter )
					x = (int) (( pos.width - b.getWidth() ) / 2);

				g.drawString(line, x, y);
				y += height;
			}

			g.dispose();
		}
		return image;
	}

	public Rectangle getPos() {
		return pos;
	}

	public String getName() {
		return name;
	}

	public void setText(String string) {
		image = null;
		text = string;

		if ( visible )
			repaint();
	}

	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
}
