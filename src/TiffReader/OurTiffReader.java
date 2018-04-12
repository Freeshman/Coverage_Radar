package TiffReader;

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
	public OurTiffReader() {
		// TODO Auto-generated constructor stub
	}
	private int longitude_s;
	private int latitude_s;
//	private static Rasters rasters;
	private Rasters rasters;
	public int pixelx(double longitude){
		int x=(int)Math.floor(Math.round(100000*6001*(longitude-longitude_s)/5.0)/100000);
		if(x<0){
			System.out.println("stupid longitude="+longitude+" and x="+x);
			}

//		System.out.println("Check="+(int) Math.floor(6001*(longitude-longitude_s)/5.0));
		return x;

	}
	public int pixely(double latitude) {
		int y=(int) Math.floor(Math.round(100000*6001*(latitude_s-latitude)/5.0)/100000);
		if(y<0){
			System.out.println("stupid latitude="+latitude+" and y="+y);
		}
		return y;
	}
	public  void loadDEM(String filename) throws IOException{
		File file = new File(filename);
		if (!file.exists()) {
			throw new IllegalArgumentException("TIFF file does not exist: "
					+ file.getAbsolutePath());
		}

		longitude_s=-180+5*(Integer.valueOf(filename.substring(filename.length()-9,filename.length()-7)).intValue()-1);
		latitude_s=60-5*(Integer.valueOf(filename.substring(filename.length()-6,filename.length()-4)).intValue()-1);
//		System.out.println(longitude_s+" "+latitude_s);
//		System.out.println(filename.substring(filename.length()-9,filename.length()-7)+" "+filename.substring(filename.length()-6,filename.length()-4));

		TIFFImage tiffImage = TiffReader.readTiff(file);

		List<FileDirectory> fileDirectories = tiffImage.getFileDirectories();
//		System.out.println("size: " + fileDirectories.size());

		for (int i = 0; i < fileDirectories.size(); i++) {
			FileDirectory fileDirectory = fileDirectories.get(i);

			rasters = fileDirectory.readRasters();
		}
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
//			ZBTJ 117.345 39.123333333333335
//			int Pixel_ZBTJx=(int) Math.round(6001*(117.345-115)/5.0);
//			int Pixel_ZBTJy=(int) Math.round(6001*(40-39.123333333333335)/5.0);
//			System.out.println(getPixel(Pixel_ZBTJx, Pixel_ZBTJy));
//
//			System.out.println(getPixel(Pixel_ZBTJx, Pixel_ZBTJy));
//			System.out.println(getPixel(Pixel_ZBTJx, Pixel_ZBTJy));

//
//			int Pixel_Testx=(int) Math.round(6001*(119.61561153904017-115)/5.0);
//			int Pixel_Testy=(int) Math.round(6001*(40-38.952000681484314)/5.0);
//			System.out.println(getPixel(rasters, Pixel_Testx, Pixel_Testy));


//			System.out.println();


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
	public float getPixel( int x, int y) {
//		System.out.println(" x,y="+x+","+y);
		return rasters.getPixel(x, y)[0].floatValue();

	}

}
