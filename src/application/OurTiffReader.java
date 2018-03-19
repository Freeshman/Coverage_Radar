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
	public int pixelx(double longitude){
		return (int) Math.round(6001*(longitude-115)/5.0);

	}
	public int pixely(double latitude) {
		return (int) Math.round(6001*(40-latitude)/5.0);
	}
	public  void loadDEM(String filename) throws IOException{
		File file = new File(filename);
		if (!file.exists()) {
			throw new IllegalArgumentException("TIFF file does not exist: "
					+ file.getAbsolutePath());
		}

		TIFFImage tiffImage = TiffReader.readTiff(file);
		//System.out.println("TIFF Image: " + file.getName());

		List<FileDirectory> fileDirectories = tiffImage.getFileDirectories();
		for (int i = 0; i < fileDirectories.size(); i++) {
			FileDirectory fileDirectory = fileDirectories.get(i);

			//System.out.println();
			//System.out.print("-- File Directory ");
			//if (fileDirectories.size() > 1) {
			//	System.out.print((i + 1) + " ");
			//}
			//System.out.println("--");

//			for (FileDirectoryEntry entry : fileDirectory.getEntries()) {
//
//				System.out.println();
//				System.out.println(entry.getFieldTag() + " ("
//						+ entry.getFieldTag().getId() + ")");
//				System.out.println(entry.getFieldType() + " ("
//						+ entry.getFieldType().getBytes() + " bytes)");
//				System.out.println("Count: " + entry.getTypeCount());
//				System.out.println("Values: " + entry.getValues());
//			}

			rasters = fileDirectory.readRasters();
//			System.out.println();
//			System.out.println("-- Rasters --");
//			System.out.println();
//			System.out.println("Width: " + rasters.getWidth());
//			System.out.println("Height: " + rasters.getHeight());
//			System.out.println("Number of Pixels: " + rasters.getNumPixels());
//			System.out.println("Samples Per Pixel: "
//					+ rasters.getSamplesPerPixel());
//			System.out
//					.println("Bits Per Sample: " + rasters.getBitsPerSample());
//
//			System.out.println();
//			System.out.println(getPixel(rasters, 0, 0));
//			System.out.println(getPixel(rasters, (int) (rasters.getWidth() / 2.0),
//					(int) (rasters.getHeight() / 2.0)));
//			System.out.println(getPixel(rasters, rasters.getWidth() - 1, rasters.getHeight() - 1));
//
			//ZBTJ 117.345 39.123333333333335
//			int Pixel_ZBTJx=(int) Math.round(6001*(117.345-115)/5.0);
//			int Pixel_ZBTJy=(int) Math.round(6001*(40-39.123333333333335)/5.0);
//			System.out.println(getPixel(Pixel_ZBTJx, Pixel_ZBTJy));

//
//			int Pixel_Testx=(int) Math.round(6001*(119.61561153904017-115)/5.0);
//			int Pixel_Testy=(int) Math.round(6001*(40-38.952000681484314)/5.0);
//			System.out.println(getPixel(rasters, Pixel_Testx, Pixel_Testy));


//			System.out.println();
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
