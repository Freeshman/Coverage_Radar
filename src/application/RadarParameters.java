package application;

import java.awt.print.Printable;

public class RadarParameters {
	private double    Maxrange, height_center, Til, longitude_center,latitude_center;
	private String antenteFile;
	public RadarParameters(double longitude_,double latitude_,double range_,double height_center_,double Til_,String anteneFile_) {
		Maxrange=range_;
		height_center=height_center_;
		Til=Til_;
		antenteFile=anteneFile_;
		longitude_center=longitude_;
		latitude_center=latitude_;
//		System.out.println("you input "+)
		//==============
		//雷达文件读取，并存为参数表
		//==============
	}
	public double[] GetLBH() {
		double[] LBH=new double[3];
		LBH[0]=longitude_center;
		LBH[1]=latitude_center;
		LBH[2]=height_center;
		return LBH;
	}
	public void  SetL(double longitude_) {
		longitude_center=longitude_;
	}
	public void  SetB(double latitude_) {
		latitude_center=latitude_;
	}
	public void  SetH(double height_center_) {
		height_center=height_center_;
	}
	public void  SetTil(double Til_) {
		Til=Til_;
	}
	public void  SetMaxRange(double MaxRange_) {
		Maxrange=MaxRange_;
	}
	public double GetL() {
		return longitude_center;
	}
	public double GetB() {
		return latitude_center;
	}
	public double GetH() {
		return height_center;
	}
	public double GetRange(double theta) {
		return Maxrange*Math.sin(theta*Math.PI/180);
	}
	public double GetMaxRange() {
		return Maxrange;
	}
	public double GetTil() {
		return Til;
	}
	public void PrintRadar(){
		System.out.println("");
		System.out.println("====================================");
		System.out.println("===========Radar Information========");
		System.out.println("L= "+longitude_center);
		System.out.println("B= "+latitude_center);
		System.out.println("H= "+height_center);
		System.out.println("MaxRange= "+Maxrange);
		System.out.println("Til= "+Til);
		System.out.println("====================================");
		System.out.println("");
	}
}
