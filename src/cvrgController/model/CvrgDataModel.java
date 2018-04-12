package cvrgController.model;

import application.CoordinateSys;

public class CvrgDataModel {
	double latitudeC;//�״�����ά��
	double longitudeC;//�״����ھ���
	double heightC;//�״����ڸ߶�
	double angle;//�Ƕ���
	double dist;
	double range;
	int N;
	double converageAngle[];
	public CvrgDataModel  (int N,double latitudeC,double longitudeC,double heightC,double angle,double dist,double range){
		this.latitudeC = latitudeC;
		this.longitudeC = longitudeC;
		this.heightC = heightC;
		this.angle = angle;
		this.dist = dist;
		this.range = range;
		this.N = N;
		converageAngle = new double[N+1];
		converageAngle[0] = -Math.PI/2;
	}


	public double[] LonLat = new double[2];
//	public double calculateN() {
//
//		CoordinateSys coordinateSys = new CoordinateSys();
//		LonLat = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, range, angle);
//		//********Test**********
//		double deltaLat = latitudeC - LonLat[1];
//		//System.out.println("deltaLat = " + deltaLat);
//		double deltaLon = longitudeC - LonLat[0];
//		//********Test**********
//		double LengthInLatLon = Math.sqrt(deltaLat*deltaLat+deltaLon*deltaLon);
//		N = (int) Math.floor(LengthInLatLon/dist);
//		converageAngle = new double[N+1];
//		converageAngle[0] = -Math.PI/2;
//		return N;
//	}
	public double[] calculate(int i) {
		double LL[] = new double[2];

		CoordinateSys coordinateSys = new CoordinateSys();
		LonLat = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, i*dist, angle);

		LL[0] = LonLat[1];
		LL[1] = LonLat[0];
		return LL;
	}
	public double getLastConverageAngle(int i) {

		return converageAngle[i-1];
	}
	public void setConverageAngle(int i,double angle) {
		converageAngle[i] = angle;
	}
	public double getViewAngle(double latitudeS, double longitudeS, double heightS) {
		//System.out.println(latitudeS+"\n"+longitudeS+"\n"+heightS);
		// TODO Auto-generated method stub
		double viewAngle;
		CoordinateSys coordinateSys = new CoordinateSys();
		double[] ECEFC = coordinateSys.JWH2ECEF(longitudeC*Math.PI/180, latitudeC*Math.PI/180, heightC);

		double[] ECEFS = coordinateSys.JWH2ECEF(longitudeS*Math.PI/180, latitudeS*Math.PI/180, heightS);
		//System.out.println(ECEFS[0]+"\n"+ECEFS[1]+"\n"+ECEFS[2]);
		double[] NEUS = coordinateSys.ECEF2NEU(ECEFS[0],ECEFS[1],ECEFS[2],ECEFC[0],ECEFC[1],ECEFC[2]);
		//System.out.println(NEUS[0]+"\n"+NEUS[1]+"\n"+NEUS[2]);
		viewAngle = Math.atan( NEUS[2] /(Math.sqrt( NEUS[0] * NEUS[0]+ NEUS[1] * NEUS[1] )) );
		return viewAngle;
	}
	public double compareConverageAngle(double lastAngle,double thisAngle) {
		double Temp;
		if(lastAngle < thisAngle) {
			Temp = thisAngle;
		}
		else {
			Temp = lastAngle;
		}
		return Temp;
	}
	public boolean judgeConverage(double limitAngle,int i) {
		boolean judge = false;
		//System.out.println("limitAngle"+limitAngle);
		//System.out.println("converageAngle"+converageAngle[i]);
		if(limitAngle <= converageAngle[i]) {
			judge = true;
		}
		//System.out.println(judge);
		return judge;
	}

}


