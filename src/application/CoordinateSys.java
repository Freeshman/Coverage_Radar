package application;
import java.lang.Math;
public class CoordinateSys {

	final double LONGa=6378137;
	final double SHORTb=6356752.31414;
	final double e=0.0818191910428;
	final double e2=0.0820944381526;
	final double SIAEB=39.1097618747*Math.PI/180;
	final double SIAEL=117.3529134562*Math.PI/180;
	final double SIAEH=100;
//	final double PI=3.1415926;
	public void test(){
		double[] ECEFresult=JWH2ECEF(SIAEL, SIAEB, SIAEH);
		System.out.println("x="+ECEFresult[0]+"y="+ECEFresult[1]+"z="+ECEFresult[2]);
		double[] check=ECEF2JWH(ECEFresult[0], ECEFresult[1], ECEFresult[2]);
		System.out.println("L="+check[0]*180/Math.PI+"B="+check[1]*180/Math.PI+"H="+check[2]);
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
		//捕捉到错误信息，e用来接收异常对象ArrayIndexOutOfBoundsException栈溢出
		catch(ArithmeticException e){
		e.printStackTrace();//可以理解为输出该异常的具体信息。
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
public double[] ENU2ECEF(double E,double N,double U,double x0,double y0,double z0){
	double[] vector_right=new double[3];//store the N,E,U....
	double[] vector_left_ECEF=new double[3];//float x,y,z;
	double[] location0=ECEF2JWH(x0, y0, z0);//Get the LBH of x0 y0 z0
	double[][] Matrix=new double[3][3];
	Matrix[1][0]=-Math.sin(location0[1])*Math.cos(location0[0]);
	Matrix[1][1]=-Math.sin(location0[0]);
	Matrix[1][2]=Math.cos(location0[1])*Math.cos(location0[0]);
	Matrix[0][0]=-Math.sin(location0[1])*Math.sin(location0[0]);
	Matrix[0][1]=Math.cos(location0[0]);
	Matrix[0][2]=Math.cos(location0[1])*Math.sin(location0[0]);
	Matrix[2][0]=Math.cos(location0[1]);
//	Matrix[2][1]=0;
	Matrix[2][2]=Math.sin(location0[1]);


	vector_right[0]=E;
	vector_right[1]=N;
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

public double[] ECEF2ENU(double x,double y,double z,double x0,double y0,double z0){
	double[] vector_right=new double[3];//store the x-x0....
	double[] vector_left_ENU=new double[3];//float East,North,Up;
	double[] location0=ECEF2JWH(x0, y0, z0);//Get the LBH of x0 y0 z0
	double[][] Matrix=new double[3][3];
	Matrix[1][0]=-Math.sin(location0[1])*Math.cos(location0[0]);
	Matrix[1][1]=-Math.sin(location0[0]);
	Matrix[1][2]=Math.cos(location0[1])*Math.cos(location0[0]);
	Matrix[0][0]=-Math.sin(location0[1])*Math.sin(location0[0]);
	Matrix[0][1]=Math.cos(location0[0]);
	Matrix[0][2]=Math.cos(location0[1])*Math.sin(location0[0]);
	Matrix[2][0]=Math.cos(location0[1]);
//	Matrix[2][1]=0;
	Matrix[2][2]=Math.sin(location0[1]);


	vector_right[1]=x-x0;
	vector_right[0]=y-y0;
	vector_right[2]=z-z0;
	for (int row=0;row<3;row++){
		for(int col=0;col<3;col++){
			vector_left_ENU[row]+=Matrix[row][col]*vector_right[col];
		}
	}
	return vector_left_ENU;
};

}
