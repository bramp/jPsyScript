package net.bramp.psyscript.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

/**
 * Keeps cached copies of the images to improve performance
 * @author Andrew Brampton
 *
 */
public class ImageFactory {

	protected final static Map<File, BufferedImage> images = new TreeMap<File, BufferedImage>();

	public static BufferedImage getImage(String filename) throws IOException {

		File f = new File(filename);

		BufferedImage i = images.get( f );

		if ( i == null ) {
			try {
				i = ImageIO.read(f);
				
				if ( i == null )
					throw new IIOException("Unknown image type");
				
				images.put(f, i);
			} catch (IOException e) {
				throw new IOException( "Error loading image '" + filename + "'", e );
			}
		}

		return i;
	}

}
