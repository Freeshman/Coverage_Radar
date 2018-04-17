package application;
import java.lang.Math;
public class CoordinateSys {

	final double LONGa=6378137;
	final double SHORTb=6356752.31414;
	final double e=0.0818191910428;
	final double e2=0.0820944381526;
	final double SIAEL=117*Math.PI/180;
	final double SIAEB=41.5*Math.PI/180;
	final double SIAEH=1088;
	final double Pi = Math.PI;
	public void test(){
		double[] center = {117*Math.PI/180,40*Math.PI/180,100};
		center=JWH2ECEF(center[0], center[1], center[2]);
		System.out.println("Cx="+center[0]+"Cy="+center[1]+"Cz="+center[2]);
		
		double[] ECEFresult=JWH2ECEF(SIAEL, SIAEB, SIAEH);
		System.out.println("x="+ECEFresult[0]+"y="+ECEFresult[1]+"z="+ECEFresult[2]);
		
		double[] NEUresult2=ECEF2NEU(ECEFresult[0],ECEFresult[1],ECEFresult[2],center[0],center[1],center[2]);
		System.out.println("N="+NEUresult2[0]+"E="+NEUresult2[1]+"U="+NEUresult2[2]);
		
	}
	public double[] JWH2ECEF(double L,double B,double H){
		double x;
		double y;
		double z;
		double[] location=new double[3];

		double R=LONGa/Math.sqrt(1-e*e*Math.sin(B)*Math.sin(B));

	     x=(R+H)*Math.cos(B)*Math.cos(L);

		 y=(R+H)*Math.cos(B)*Math.sin(L);

		 z=(R*(1-e*e)+H)*Math.sin(B);
		 location[0]=x;
		 location[1]=y;
		 location[2]=z;
		return location;
	}
public  double[] ECEF2JWH(double x,double y,double z){
	double L;
	double B;
	double H;
	double[] location=new double[3];
	try{
		L=Math.PI/2-Math.atan(x/y);
		}
		//鎹曟崏鍒伴敊璇俊鎭紝e鐢ㄦ潵鎺ユ敹寮傚父瀵硅薄ArrayIndexOutOfBoundsException鏍堟孩鍑�
		catch(ArithmeticException e){
		e.printStackTrace();//鍙互鐞嗚В涓鸿緭鍑鸿寮傚父鐨勫叿浣撲俊鎭��
		System.out.println("ECEF2JWH y=0!");
		L=0;
		}

	double U=Math.atan(z/(Math.sqrt(x*x+y*y)*Math.sqrt(1-e*e)));

	double tanB=(z+SHORTb*e2*e2*Math.sin(U)*Math.sin(U)*Math.sin(U))/
			((Math.sqrt(x*x+y*y)-LONGa*e*e*Math.cos(U)*Math.cos(U)*Math.cos(U)));
	B=Math.atan(tanB);

	double R=LONGa/Math.sqrt(1-e*e*Math.sin(B)*Math.sin(B));
	H=Math.sqrt(x*x+y*y)/Math.cos(B)-R;
	location[0]=L;
	location[1]=B;
	location[2]=H;
	return location;
};
public double[] NEU2ECEF(double N,double E,double U,double x0,double y0,double z0){
	double[] vector_right=new double[3];//store the N,E,U....
	double[] vector_left_ECEF=new double[3];//float x,y,z;
	double[] location0=ECEF2JWH(x0, y0, z0);//Get the LBH of x0 y0 z0
	double[][] Matrix=new double[3][3];
	Matrix[0][0]=-Math.sin(location0[1])*Math.cos(location0[0]);
	Matrix[0][1]=-Math.sin(location0[0]);
	Matrix[0][2]=Math.cos(location0[1])*Math.cos(location0[0]);
	Matrix[1][0]=-Math.sin(location0[1])*Math.sin(location0[0]);
	Matrix[1][1]=Math.cos(location0[0]);
	Matrix[1][2]=Math.cos(location0[1])*Math.sin(location0[0]);
	Matrix[2][0]=Math.cos(location0[1]);
//	Matrix[2][1]=0;
	Matrix[2][2]=Math.sin(location0[1]);


	vector_right[0]=N;
	vector_right[1]=E;
	vector_right[2]=U;
	for (int row=0;row<3;row++){
		for(int col=0;col<3;col++){
			vector_left_ECEF[row]+=Matrix[row][col]*vector_right[col];
		}
	}
	vector_left_ECEF[0]=vector_left_ECEF[0]+x0;
	vector_left_ECEF[1]=vector_left_ECEF[1]+y0;
	vector_left_ECEF[2]=vector_left_ECEF[2]+z0;
	return vector_left_ECEF;
};

	public double[] ECEF2NEU(double x,double y,double z,double x0,double y0,double z0){
		double[] vector_right=new double[3];//store the x-x0....
		double[] vector_left_NEU=new double[3];//float East,North,Up;
		double[] location0=ECEF2JWH(x0, y0, z0);//Get the LBH of x0 y0 z0
		double[][] Matrix=new double[3][3];
	//	Matrix[1][0]=-Math.sin(location0[1])*Math.cos(location0[0]);
	//	Matrix[1][1]=-Math.sin(location0[0]);
	//	Matrix[1][2]=Math.cos(location0[1])*Math.cos(location0[0]);
	//	Matrix[0][0]=-Math.sin(location0[1])*Math.sin(location0[0]);
	//	Matrix[0][1]=Math.cos(location0[0]);
	//	Matrix[0][2]=Math.cos(location0[1])*Math.sin(location0[0]);
	//	Matrix[2][0]=Math.cos(location0[1]);
	////	Matrix[2][1]=0;
	//	Matrix[2][2]=Math.sin(location0[1]);
	
		Matrix[0][0]=-Math.sin(location0[1])*Math.cos(location0[0]);
		Matrix[1][0]=-Math.sin(location0[0]);
		Matrix[2][0]=Math.cos(location0[1])*Math.cos(location0[0]);
		Matrix[0][1]=-Math.sin(location0[1])*Math.sin(location0[0]);
		Matrix[1][1]=Math.cos(location0[0]);
		Matrix[2][1]=Math.cos(location0[1])*Math.sin(location0[0]);
		Matrix[0][2]=Math.cos(location0[1]);
	//	Matrix[1][2]=0;
		Matrix[2][2]=Math.sin(location0[1]);
		
		vector_right[0]=x-x0;
		vector_right[1]=y-y0;
		vector_right[2]=z-z0;
		for (int row=0;row<3;row++){
			for(int col=0;col<3;col++){
				vector_left_NEU[row]+=Matrix[row][col]*vector_right[col];
			}
		}
		return vector_left_NEU;
	}

	
	public static double[] getLatLonFromRanCenAng(double range, double latitudeC, double longitudeC, double angle) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public double[] LambdaPhiLTheta2LB(double LambdaA,double PhiA,double L,double Theta0) {
		double Lambda=LambdaA*Pi/180;
		double Phi=PhiA*Pi/180;
		double Theta=Theta0*Pi/180;
		double R=LONGa/Math.sqrt(1-e*e*Math.sin(Phi)*Math.sin(Phi));
		double alphac=L/R;
		double cos_alphaa=Math.sin(Phi)*Math.cos(alphac)+Math.cos(Phi)*Math.sin(alphac)*Math.cos(Theta);
		double alphaa=Math.acos(cos_alphaa);
		double C=Math.asin(Math.sin(alphac)*Math.sin(Theta)/Math.sin(alphaa));
		double PhiB=Pi/2-alphaa;
		PhiB=PhiB*180/Pi;
		double LambdaB=Lambda+C;
		LambdaB=LambdaB*180/Pi;
		double[] result=new double[2];
		result[0]=LambdaB;
		result[1]=PhiB;
		return result;
	}
	public double getR(double PhiA) {
		double Phi=PhiA*Pi/180;
		return LONGa/Math.sqrt(1-e*e*Math.sin(Phi)*Math.sin(Phi));
	}
	public double[] NEU2JWH(double N,double E,double U,double CL,double CB,double CH) {
		CL = CL*Math.PI/180;CB = CB*Math.PI/180;CH = CH*Math.PI/180;
		double[] center=JWH2ECEF(CL, CB, CH);
		double[] ECEFS = NEU2ECEF(N, E, U,center[0],center[1],center[2]);
		double[] JWHS = ECEF2JWH(ECEFS[0],ECEFS[1],ECEFS[2]);		
		JWHS[0] = JWHS[0]*180/Math.PI;JWHS[1] = JWHS[1]*180/Math.PI;JWHS[2] = JWHS[2]*180/Math.PI;  
		return JWHS;
	}
	public double[] JWH2NEU(double L,double B,double H,double CL,double CB,double CH) {
		L = L*Math.PI/180;B = B*Math.PI/180;H = H*Math.PI/180;
		CL = CL*Math.PI/180;CB = CB*Math.PI/180;CH = CH*Math.PI/180;
		double[] center=JWH2ECEF(CL, CB, CH);
		System.out.println("Cx="+center[0]+"Cy="+center[1]+"Cz="+center[2]);
		
		double[] JWHS=JWH2ECEF(L, B, H);
		System.out.println("x="+JWHS[0]+"y="+JWHS[1]+"z="+JWHS[2]);
		
		double[] NEUresult2=ECEF2NEU(JWHS[0],JWHS[1],JWHS[2],center[0],center[1],center[2]);
		System.out.println("N="+NEUresult2[0]+"E="+NEUresult2[1]+"U="+NEUresult2[2]);
		return NEUresult2;
	}
}
