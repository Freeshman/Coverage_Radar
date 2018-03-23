package TiffReader;

import java.io.IOException;

public class OurTiffReaderTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		OurTiffReader tiffReader1=new OurTiffReader();
		tiffReader1.loadDEM("srtm_59_04.tif");
		System.out.println("result1="+tiffReader1.getPixel(tiffReader1.pixelx(114.88434270191767), tiffReader1.pixely(40.00001594900542)));
		OurTiffReader tiffReader2=new OurTiffReader();
		tiffReader2.loadDEM("srtm_59_05.tif");
		
//		tiffReader1.loadDEM("srtm_59_04.tif");
		System.out.println("result2="+tiffReader2.getPixel(tiffReader2.pixelx(114.87599819761915), tiffReader2.pixely(39.99910272281527)));
		System.out.println("result3="+tiffReader1.getPixel(tiffReader1.pixelx(114.88434270191767), tiffReader1.pixely(40.00001594900542)));
	}

}
