package cvrgController;

import java.io.IOException;

import TiffReader.DynamicTiffReader;
import cvrgController.model.CvrgDataModel;

public class CvrgController {
	// public static void main(String[] args) {
	// 	double latitudeC = 38;
	// 	double longitudeC= 106;
	// 	double heightC = 1147;
	// 	double angle= 45;

	// 	double range = 500000;
	// 	int N = 100;
	// 	double dist = Math.floor(range/N);//8.25

	// 	//******************************
	// 	double HL = 10000;
	// 	//******************************
	// 	CvrgController cvrgController = new CvrgController();
	// 	try {
	// 		DynamicTiffReader dynamicTiffReader=new DynamicTiffReader(longitudeC, latitudeC);

	// 	for(int i=0;i<8;i++) {
	// 		System.out.println("Angle��"+ angle*i);
	// 		cvrgController.start(N,dynamicTiffReader,latitudeC,longitudeC,heightC,angle*i,dist,range,HL);
	// 	}
	// 	} catch (IOException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
	// }
	public double[] start(int N,DynamicTiffReader dynamicTiffReader,double latitudeC,double longitudeC,double heightC,double angle,double dist,double range,double HL) {

		double idealConverageAngle;
		CvrgDataModel cvrgDataModel = new CvrgDataModel(N,latitudeC,longitudeC,heightC,angle,dist,range);
		//double N = cvrgDataModel.calculateN();//3.25

//		System.out.println("ѭ������"+N);
		//******************************

		boolean judge = false;
		double cvrglat = cvrgDataModel.calculate(N)[0];
		double cvrglon = cvrgDataModel.calculate(N)[1];
		for(int i = 1; i <= N; i++) {

			CvrgController cvrgController = new CvrgController();
			idealConverageAngle = cvrgController.calculateConverageAngle(dynamicTiffReader,longitudeC, latitudeC,cvrgDataModel,i);
			if(!judge) {
				judge = cvrgController.getConverageRange(cvrgDataModel,i,HL);
				if(judge) {
					cvrglat = cvrgDataModel.calculate(i)[0];
					cvrglon = cvrgDataModel.calculate(i)[1];
//					System.out.println("i��"+ i);
				}
			}
		}
		double[] LB=new double[2];
		LB[0]=cvrglon;
		LB[1]=cvrglat;

//		System.out.println("angle"+angle+" "+"cvrglat:"+cvrglat+"cvrglon:"+cvrglon);
//		System.out.println(cvrgDataModel.LonLat[1]+" "+cvrgDataModel.LonLat[0]);
		return LB;
//		System.out.println("cvrglat:"+cvrglat+"cvrglon:"+cvrglon);
		//System.out.println(cvrgDataModel.LonLat[1]+" "+cvrgDataModel.LonLat[0]);

	}

	public double calculateConverageAngle(DynamicTiffReader dynamicTiffReader,double longitudeC, double latitudeC,CvrgDataModel cvrgDataModel,int i) {
		double latitudeS;
		double longitudeS;
		double heightS = 0;
		double lastConverageAngle;
		double thisConverageAngle;

		latitudeS = cvrgDataModel.calculate(i)[0];
		longitudeS = cvrgDataModel.calculate(i)[1];
		//System.out.println(latitudeS);
		//System.out.println(longitudeS);
		//******�õ��߶�********************************
		try {
			heightS = dynamicTiffReader.GetHeight(longitudeS, latitudeS);
			//System.out.println(" heightS "+heightS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*********************************************
		lastConverageAngle = cvrgDataModel.getLastConverageAngle(i);
		//System.out.println("lastConverageAngle"+lastConverageAngle);

		thisConverageAngle = cvrgDataModel.getViewAngle(latitudeS,longitudeS,heightS);
		//System.out.println("ideaThisConverageAngle"+thisConverageAngle);

		thisConverageAngle = cvrgDataModel.compareConverageAngle(lastConverageAngle,thisConverageAngle);
		//System.out.println("realThisConverageAngle"+thisConverageAngle);

		cvrgDataModel.setConverageAngle(i, thisConverageAngle);
		return thisConverageAngle;
	}
	public boolean getConverageRange(CvrgDataModel cvrgDataModel,int i,double HL) {
		double latitudeS;
		double longitudeS;
		double limitAngle;
		boolean cvrg = false;
		latitudeS = cvrgDataModel.calculate(i)[0];
		longitudeS = cvrgDataModel.calculate(i)[1];
		//System.out.println(latitudeS +" "+ longitudeS);
		//******�õ��߶�********************************
		//*********************************************
		limitAngle = cvrgDataModel.getViewAngle(latitudeS,longitudeS,HL);
		//System.out.println("limitAngle "+limitAngle);
		//System.out.println("ici3");
		cvrg = cvrgDataModel.judgeConverage(limitAngle,i);
		//System.out.println("ici4");
		return cvrg;
	}
}
