package cvrgController;

import java.util.ArrayList;
import java.util.List;

import cvrgController.model.CvrgDataModel;

public class PowerRange {
	public static void main(String[] args) {
//		double s = radar_power_gain(7.6577*Math.PI/180);
//		System.out.println(s);
//		System.out.println(getJudgeRange(250000,250000));
//		System.out.println(getJudgeDegree(getJudgeRange(250000,250000),250000, 250000));
//		System.out.println( Math.PI/4 );
		PowerRange powerRange = new PowerRange();
//		double Next[] =  myTest.HeightLambdaPhiLTheta2LB(250000,0,30,250000,0);
//		System.out.println( Next[0] );
//		System.out.println( Next[1] );
		double longitude = 0;
		double latitude = 30;
		
		double HF = 250000;
		double latitudeC = 38.058942;
	 	double longitudeC= 106.2028889;
		double HeightC = 1144;
		
		double thelta = 0;
		double dis = 110000;
		double range = 250000;
		//double N = Math.floor(range/dis);
	
		double iternallongitude;
		double iternallatitude;
		double externallongitude;
		double exterallatitude;
		int k = 0;
		int m = 0;
		
//		for(int i=1;i<=N;i++) {
			//double next[] = powerRange.LambdaPhiLTheta2LB(longitude,latitude,dis*i,thelta);
//			double nextlongitude = next[0];
//			double nextlatitude = next[1];
//			System.out.println( next[0] );
//			System.out.println( next[1] );
			double judgeRange = getJudgeRange(HeightC,HF, dis*2);
			//double judgeDegree = getJudgeDegree(judgeRange,HF,dis*i);
			//double powerRangeNumber = radar_power_gain(judgeDegree) * range/2.7;
			System.out.println( judgeRange );
//			System.out.println( judgeDegree );
//			if(powerRangeNumber < judgeRange) {
//				break;
//			}
//			else {
			//	double mountainHeight = ;
//				if(mountainHeight < HF) {
//					k++;
//					if(k == 1) {
//						iternallongitude = longitude;
//						iternallatitude = latitude;
//						}
//				}
//				else {
//					m++;
//					if(m ==1) {
//						externallongitude = longitude;
//						exterallatitude = latitude;
//					}
//				}
//		}
		
	}
	
	public static double radar_power_gain(double theta_deg) {
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
	public static double[] complexMul(double xRe,double xIm,double yRe,double yIm) {
		double[] result = new double[2];
			result[0] = xRe*yRe - xIm*yIm;
			result[1] = xRe*yIm + yRe*xIm;		
		return result;
	}
	public static double getJudgeRange(double real_heightC,double HL, double dis) {
		//double R = CvrgDataModel.R;
		double R = 6400000;
		return Math.sqrt( (R+real_heightC)*(R+real_heightC) + (R+HL)*(R+HL) - 2*(R+real_heightC)*(R+HL)*Math.cos( dis/R )   );
	}
	public static double getJudgeDegree(double degree) {
		return Math.PI/2 - degree;
	}
	

}

