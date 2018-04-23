package cvrgController.model;

import java.util.ArrayList;
import java.util.List;

import application.CoordinateSys;

public class CvrgDataModel {
	double latitudeC;//ï¿½×´ï¿½ï¿½ï¿½ï¿½ï¿½Î¬ï¿½ï¿½
	double longitudeC;//ï¿½×´ï¿½ï¿½ï¿½ï¿½Ú¾ï¿½ï¿½ï¿½
	double heightC;//ï¿½×´ï¿½ï¿½ï¿½ï¿½Ú¸ß¶ï¿½
	double angle;//ï¿½Ç¶ï¿½ï¿½ï¿½
	double dist;
	double range;
	public int N;
	double converageAngle[];
	public static double R;
	
	public CvrgDataModel  (double latitudeC,double longitudeC,double heightC,double angle,double dist,double range){
		this.latitudeC = latitudeC;
		this.longitudeC = longitudeC;
		this.heightC = heightC;
		this.angle = angle;
		this.dist = dist;
		this.range = range;
		
		
	}

	CoordinateSys coordinateSys = new CoordinateSys();
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

		
		LonLat = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, i*dist, angle);

		LL[0] = LonLat[1];
		LL[1] = LonLat[0];
		return LL;
	}
	public double[] calculateLonLatOfLimitPoint(double distant) {
		return coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, distant, angle);
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
		double[] ECEFC = coordinateSys.JWH2ECEF(longitudeC*Math.PI/180, latitudeC*Math.PI/180, heightC);

		double[] ECEFS = coordinateSys.JWH2ECEF(longitudeS*Math.PI/180, latitudeS*Math.PI/180, heightS);
		//System.out.println(ECEFS[0]+"\n"+ECEFS[1]+"\n"+ECEFS[2]);
		double[] NEUS = coordinateSys.ECEF2NEU(ECEFS[0],ECEFS[1],ECEFS[2],ECEFC[0],ECEFC[1],ECEFC[2]);
		//System.out.println(NEUS[0]+"\n"+NEUS[1]+"\n"+NEUS[2]);
		viewAngle = Math.atan( NEUS[2] /(Math.sqrt( NEUS[0] * NEUS[0]+ NEUS[1] * NEUS[1] )) );
		return viewAngle;
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
	public double getViewLimitOfEarth(double Latitude,double H,double HL) {
		R = coordinateSys.getR(Latitude);
		double theta = Math.acos(R/(R+H)) + Math.acos(R/(R+HL));
		double L= R * theta;
		N =  (int) Math.floor( L /dist);
		converageAngle = new double[N+1];
		converageAngle[0] = -Math.PI/2;
		return L;
	}
	
	
	public double radar_power_gain(double theta_deg) {
		//theta_degÎª»¡¶ÈÖÆ
		double G;
		double[] dB = {-10.64, -5.78, -6.01, -10.98, -9.91, -8.89, -13.48, -15.08, -17.95, -20.60, -20.11};
		
		double[] A = new double[dB.length];
		for(int i=0;i<A.length;i++) {
		A[i] = Math.pow(10, dB[i]/20);
		}
		double[] deg =  {0, 66.8, 126.3, 153.8, 151.4, -174.1, -155.6, -144.9, -130.9, -112.2, -124.2};
		double[] phi0 = new double[deg.length];
		for(int j=0;j<deg.length;j++) {
			phi0[j] = deg[j] * Math.PI/180.0;
		}
		double[] intensityRe = new double[A.length];
		double[] intensityIm = new double[A.length];
		for(int i = 0; i < A.length ;i++) {
		intensityRe[i] = A[i] *Math.cos(phi0[i]);
		intensityIm[i] = A[i] *Math.sin(phi0[i]);
		}
		double[] phi = new double[dB.length];
		for(int i = 0; i < A.length ;i++) {
			phi[i] = Math.PI * i * Math.sin(theta_deg);
			}
		double phaseRe[] = new double[A.length];
		double phaseIm[] = new double[A.length];
		for(int i = 0; i < A.length ;i++) {
			phaseRe[i] = Math.cos(phi[i]);
			phaseIm[i] = -Math.sin(phi[i]);
			}
		double[][] complexArray = new double[A.length][2];
		double sum1 = 0;
		double sum2 = 0;
		for(int i = 0; i < A.length ;i++) {
			double[] result = complexMul(intensityRe[i],intensityIm[i],phaseRe[i],phaseIm[i]);
			complexArray[i][0] = result[0];
			complexArray[i][1] = result[1];
			sum1 = sum1 + complexArray[i][0];
			sum2 = sum2 + complexArray[i][1];
			}
		G = Math.sqrt( sum1*sum1 + sum2*sum2 );
		return G;
	}
	public double[] complexMul(double xRe,double xIm,double yRe,double yIm) {
		double[] result = new double[2];
			result[0] = xRe*yRe - xIm*yIm;
			result[1] = xRe*yIm + yRe*xIm;		
		return result;
	}
	public double getJudgeRange(double HL, double dis) {
		double real_heightC = heightC;
		double R = CvrgDataModel.R;
		
		return Math.sqrt( (R+real_heightC)*(R+real_heightC) + (R+HL)*(R+HL) - 2*(R+real_heightC)*(R+HL)*Math.cos( dis/R )   );
	}
	public double getPowerRange(double gain,double range) {
		return Math.sqrt( gain/ 2.7001)*range;
	}
	
}


