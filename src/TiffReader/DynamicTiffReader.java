/**
 *
 */
package TiffReader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;

import com.sun.javafx.scene.EnteredExitedHandler;

import Lambert.test;

/**
 * @author hu-tom
 *
 */
public class DynamicTiffReader {
	private OurTiffReader[] TiffReader;
	private String[] TifFileBuffer;
//	private int count;
	private int index;
	public DynamicTiffReader(double longitude0,double latitude0,int winWidth,int winHeight) {
//		count=0;
//		TifFileBuffer=new String[5];
//		TifFileBuffer[count++]=
	}
	public  DynamicTiffReader(double longitude,double latitude) throws IOException {
		TifFileBuffer=new String[10];
		TiffReader=new OurTiffReader[10];
		for(int i=0;i<10;i++){
			TifFileBuffer[i]="";
			TiffReader[i]=new OurTiffReader();
		}
		index=0;

		String tmp=GetFileName(longitude, latitude);
		TiffReader[index].loadDEM(tmp);
		TifFileBuffer[index]=tmp;

//		GetHeight(longitude, latitude);
	}

	public double GetHeightNonSence(double longitude,double latitude) {
		return 0;
	}
	public double GetHeight(double longitude,double latitude) throws IOException {
		String tmp=GetFileName(longitude, latitude);
		int check = GetIndex(tmp);
//		for(int i=0;i<5;i++)
//			if(!TifFileBuffer[i].isEmpty()){
////				System.out.println("Position="+longitude+", "+latitude);
//			System.out.println("you want "+tmp+" we had "+TifFileBuffer[i]+" and the check="+check);
//			}
//		System.out.println("Index="+index);
		if(check==-1){
//			System.out.println("Not load "+tmp);
			index++;
			if(index>=10){
//				count=5;
				index=0;
				System.out.println("Reuse the TiffReader\n");
			}
//			TiffReader[index]=new OurTiffReader();
			TiffReader[index].loadDEM(tmp);
			TifFileBuffer[index]=tmp;
//			System.out.println("current filename="+TifFileBuffer[index]);

			return TiffReader[index].getPixel(TiffReader[index].pixelx(longitude), TiffReader[index].pixely(latitude));
		}else{
//			System.out.println("check="+check);
//			System.out.println("wo ca="+TiffReader[check].getPixel(TiffReader[check].pixelx(longitude), TiffReader[check].pixely(latitude)));
//			if(check==1)
//				return TiffReader[1].getPixel(TiffReader[1].pixelx(longitude), TiffReader[1].pixely(latitude));
//			else
		return TiffReader[check].getPixel(TiffReader[check].pixelx(longitude), TiffReader[check].pixely(latitude));
		}
	}
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Test();
	}
	public void FileName() {
		for(int i=0;i<10;i++){
			if(!TifFileBuffer[i].isEmpty())System.out.println("We have "+TifFileBuffer[i]);
		}
	}
public static void Test() throws IOException {
	System.out.println("");
	System.out.println("DynamicTiffReader Tester");
	System.out.println("");
	System.out.println("Test:");
	System.out.println("Longitude=117.35");
	System.out.println("Latitude=37.35");
	System.out.println("You need "+GetFileName(117.35, 37.25));
	System.out.println("");
	OurTiffReader TiffReader=new OurTiffReader();
	TiffReader.loadDEM(GetFileName(117.35,37.26));
//	TiffReader.loadDEM("srtm_60_05.tif");

}
private static String GetFileName(double longitude,double latitude) {
	String filename="srtm_";
	if(latitude<-60|latitude>60){
		System.out.println("Latitude overange!");
		return "";
	}
	int nLong=(int)((longitude+180)/5.0)+1;
	int nLat=(int)((60-latitude)/5.0)+1;
	DecimalFormat form= new DecimalFormat("00");
	filename+=form.format(nLong)+"_"+form.format(nLat)+".tif";
//	System.out.println("you need "+filename);
	return filename;
}
private int GetIndex(String check) {
	for(int i=0;i<10;i++){
		if(TifFileBuffer[i].equals(check)==true){

			return i;
		}
	}
//	for(int i=0;i<50;i++){
//		System.out.println(" ");
//		System.out.println("you want "+check+" we had "+TifFileBuffer[i]);
//}
	return -1;
}
}
