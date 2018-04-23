package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import TiffReader.DynamicTiffReader;



public class SuperRadar extends OneRadar{
	static int iter = 0;
	List<double[]> rangeArray = new ArrayList<>();
	List<double[]> shieldArray = new ArrayList<>();
	List<double[]> maxPowerArray = new ArrayList<>();
	List<double[]> viewArray = new ArrayList<>();
	CoordinateSys coordinateSys = new CoordinateSys();
	int N;
	double L;
	double R;
	double[] oneRange;//LonLat
	double[] mostFarLonLat;
	double[] converageAngle;
	//=============================
	double[] sheild;

	public SuperRadar calculateOneRay(long i2) {
		iter++;
		getViewLimitOfEarth(latitudeC, heightC_real, HL);
		mostFarLonLat = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, L, i2*thetaAngle);
		oneRange = mostFarLonLat;
		double[] LonLatS;
		double heightS = 0;
		double viewAngleS;
		double limitRangeS;
		double PowerRangeS;
		List<double[]> maybeSheild = new ArrayList<>();
		maybeSheild.add(mostFarLonLat);
		//==============================
		for(int i = 1; i <= N; i++) {

			//1.�õ�����������ľ�γ�ȸ߶�
			LonLatS = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, i*dist, i2*thetaAngle);
			try {
				heightS = dynamicTiffReader.GetHeight(LonLatS[0], LonLatS[1]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//2.�õ������������ӽ�
			viewAngleS = getViewAngle(LonLatS[1],LonLatS[0],HL);
			//3.���εõ����������㼫�޷�Χ��������Χ
			limitRangeS = getJudgeRange(HL,i*dist);
			PowerRangeS = getPowerRange(radar_power_gain( viewAngleS ),range );


			if(limitRangeS <= PowerRangeS) {
				//�õ������������ڱν�
				converageAngle[i] = getViewAngle(LonLatS[1],LonLatS[0],heightS);

				if(converageAngle[i] < converageAngle[i-1]) {
					if( i!=1 && converageAngle[i-2] < converageAngle[i-1]) {
						double[] shieldLonLat = new double[2];
						shieldLonLat = LonLatS;
						maybeSheild.add(shieldLonLat);
					}
					converageAngle[i] = converageAngle[i-1];
				}


				if(converageAngle[i] >= viewAngleS) {
					oneRange = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, i*dist, i2*thetaAngle);
					break;
				}
			}
			else {
				oneRange = coordinateSys.LambdaPhiLTheta2LB(longitudeC, latitudeC, i*dist, i2*thetaAngle);
				break;
			}
			sheild = maybeSheild.get(maybeSheild.size()-1);

		}
		return this;
	}

	public void calculateAllRay2() {

		for(int i=0;i<(int)360/thetaAngle;i++) {
			calculateOneRay(i);
			rangeArray.add(oneRange);
		}
	}

	public static void main(String[] args) {
//		long a = System.currentTimeMillis();
		SuperRadar superRadar = new SuperRadar();
//		long c = 0;
		try {
			DynamicTiffReader dynamicTiffReader2 = new DynamicTiffReader(superRadar.longitudeC,superRadar.latitudeC);
			superRadar.setDynamicTiffReader(dynamicTiffReader2);
//			c = System.currentTimeMillis();
//			System.out.println(c-a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		superRadar.calculateAllRay2();


//		long b = System.currentTimeMillis();
//		System.out.println(b-a);
	}



	public double getViewAngle(double latitudeS, double longitudeS, double heightS) {
		double viewAngle;
		double[] NEUS = coordinateSys.JWH2NEU(longitudeS, latitudeS, heightS, longitudeC, latitudeC, heightC_real);
		viewAngle = Math.atan( NEUS[2] /(Math.sqrt( NEUS[0] * NEUS[0]+ NEUS[1] * NEUS[1] )) );
		return viewAngle;
	}
	public double getViewLimitOfEarth(double Latitude,double H,double HL) {
		R = coordinateSys.getR(Latitude);
		double theta = Math.acos(R/(R+H)) + Math.acos(R/(R+HL));
		L= R * theta;
		N =  (int) Math.floor( L /dist);
		//ÿ�����߹涨ԭ���ڱν�-90��
				converageAngle = new double[N+1];
				converageAngle[0] = -Math.PI/2;
		return L;
	}
	public double radar_power_gain(double theta_deg) {
		//theta_degΪ������
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
		return Math.sqrt( (R+heightC_real)*(R+heightC_real) + (R+HL)*(R+HL) - 2*(R+heightC_real)*(R+HL)*Math.cos( dis/R )   );
	}
	public double getPowerRange(double gain,double range) {
		return Math.sqrt( gain/ 2.7001)*range;
	}
}
