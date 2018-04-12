package TiffReader;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DynamicTiffReader dynamicTiffReader=new DynamicTiffReader(80, 15);
		System.out.println("");
		System.out.println("DynamicTiffReader class test");
		System.out.println("");
		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
		System.out.println(dynamicTiffReader.GetHeight(114.87599819761915, 39.99910272281527));
		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
		System.out.println(dynamicTiffReader.GetHeight(115.88434270191767, 40.00001594900542));
		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
//		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
//		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
//		System.out.println(dynamicTiffReader.GetHeight(114.88434270191767, 40.00001594900542));
//		System.out.println( "Math.round="+(119.99960639631014-115.0)/5.0);
//		System.out.println((int) Math.floor(6001*(119.99960639631014-115)/5.0));
	}

}
