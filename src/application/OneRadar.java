package application;

import java.io.IOException;

import TiffReader.DynamicTiffReader;;

public class OneRadar {
//	OneRadar(){
//		try {
//			long a = System.currentTimeMillis();
//			dynamicTiffReader = new DynamicTiffReader(longitudeC,latitudeC);
//			long b = System.currentTimeMillis();
//			System.out.println(b-a);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	};
	//double latitudeC = 38.058942;
	double latitudeC = 38;
	//double latitudeC = 42.57;
	//double latitudeC = 40;
	double longitudeC = 106.2028889;
	double HL = 2000;
	double thetaAngle = 1;
	double threadNumber = 4;
	double range = 250000;
	double dist = 50;
	double height_center = 12;
	double heightC_real = 1147;
	double til = 10;
	DynamicTiffReader dynamicTiffReader;
	double getDist()  {
		return dist;
	}
//	public void setDist(double dist) {
//		this.dist = dist;
//	}
	public DynamicTiffReader getDynamicTiffReader() {
		return dynamicTiffReader;
	}
	public void setDynamicTiffReader(DynamicTiffReader dynamicTiffReader) {
		this.dynamicTiffReader = dynamicTiffReader;
	}

	
}

