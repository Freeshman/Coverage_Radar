package application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import mil.nga.tiff.*;

/**
 * Test reading an argument provided TIFF file
 *
 * @author osbornb
 */
public class OurTiffReader {

	/**
	 * Main method, provide a single file path argument
	 *
	 * @param args
	 * @throws IOException
	 */
	private static Rasters rasters;
	public int pixelx(double latitude){
		return (int) Math.round(6001*(40-latitude)/5.0);

	}
	public int pixely(double longitude) {
		return (int) Math.round(6001*(longitude-115)/5.0);
		
	}
	public  void loadDEM(String filename) throws IOException{
		File file = new File(filename);
		if (!file.exists()) {
			throw new IllegalArgumentException("TIFF file does not exist: "
					+ file.getAbsolutePath());
		}
		TIFFImage tiffImage = TiffReader.readTiff(file);
		List<FileDirectory> fileDirectories = tiffImage.getFileDirectories();
		for (int i = 0; i < fileDirectories.size(); i++) {
			FileDirectory fileDirectory = fileDirectories.get(i);
			rasters = fileDirectory.readRasters();
		}
	}

	/**
	 * Print a pixel from the rasters
	 *
	 * @param rasters
	 *            rasters
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public static float getPixel( int x, int y) {
		return rasters.getPixel(x, y)[0].floatValue();

	}

}
