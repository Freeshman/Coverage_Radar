package cvrgController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import TiffReader.DynamicTiffReader;
import cvrgController.model.CvrgDataModel;

public class CvrgController {
//	 public static void main(String[] args) {
//	 	double latitudeC = 38.058942;
//	 	double longitudeC= 106.2028889;
//	 	double heightC = 1120+0;
//	 	double angle= 0.5;
//
//	 	double range = 250000;
//	 	
//	 	double dist = 50;//8.25
//	 	
//	 	//******************************
//	 	double HL = 2000;
//	 	//******************************
//	 	CvrgController cvrgController = new CvrgController();
//	 	try {
//	 		DynamicTiffReader dynamicTiffReader=new DynamicTiffReader(longitudeC, latitudeC);
//
//	 	for(int i=0;i<720;i++) {
//	 		//System.out.println("Angle:"+ angle*i);
//	 		cvrgController.start(dynamicTiffReader,latitudeC,longitudeC,heightC,angle*i,dist,range,HL);
//	 	}
//	 	} catch (IOException e) {
//	 		// TODO Auto-generated catch block
//	 		e.printStackTrace();
//	 	}
//	 }
	public List<double[] >start(DynamicTiffReader dynamicTiffReader,double latitudeC,double longitudeC,double heightC,double angle,double dist,double range,double HL) {
		List<double[]> pointList = new ArrayList<>();
		double ConverageAngle;
		double limitAngle;
		//double cvrglat_in = 100;
		//double cvrglon_in = 200;
		double limitRange;
		double PowerRange;
		int k = 0;
		//0.获得曲率影响下视域范围
		CvrgDataModel cvrgDataModel = new CvrgDataModel(latitudeC,longitudeC,heightC,angle,dist,range);
		double L = cvrgDataModel.getViewLimitOfEarth(latitudeC, heightC, HL);
		
		boolean judge = false;
		//1.得到最远点经纬度
		double[] cvrgLonLat = cvrgDataModel.calculateLonLatOfLimitPoint(L);
		double cvrglat = cvrgLonLat[1];
		double cvrglon = cvrgLonLat[0];
		//int myCase = 1;
		for(int i = 1; i <= cvrgDataModel.N; i++) {
			//2.计算视角
			limitAngle = getLimitAngle(cvrgDataModel,i,HL);
			
			//3.依次得到极限范围、威力范围
			limitRange = cvrgDataModel.getJudgeRange(HL,i*dist);
			PowerRange = cvrgDataModel.getPowerRange(cvrgDataModel.radar_power_gain( limitAngle ),range );
			//if(i == 1 && (limitRange > PowerRange) ) { 
			//	myCase = 2; //两个圈
			//}
			
			//4.判断是否在威力范围内
			//if(myCase == 1 ) {
				//一个圈
				if(limitRange <= PowerRange) {
					//5.计算遮蔽度
					ConverageAngle = calculateConverageAngle(dynamicTiffReader,longitudeC, latitudeC,cvrgDataModel,i);
					
					
					if(ConverageAngle >= limitAngle) {
						cvrglat = cvrgDataModel.calculate(i)[0];
						cvrglon = cvrgDataModel.calculate(i)[1];
						break;
					}
				}
				else {
					cvrglat = cvrgDataModel.calculate(i)[0];
					cvrglon = cvrgDataModel.calculate(i)[1];
					break;
				}

		}
		double[] LB=new double[3];
		LB[0]=cvrglon;
		LB[1]=cvrglat;
		LB[2]=angle;
		pointList.add(LB);
		//System.out.println("angle"+angle+" "+"cvrglat:"+cvrglat+"cvrglon:"+cvrglon);
		//System.out.println(cvrgLonLat[1]+" "+cvrgLonLat[0]);
		
		return pointList;

	}

	public double calculateConverageAngle(DynamicTiffReader dynamicTiffReader,double longitudeC, double latitudeC,CvrgDataModel cvrgDataModel,int i) {
		double latitudeS;
		double longitudeS;
		double heightS = 0;
		double lastConverageAngle;
		double thisConverageAngle;

		latitudeS = cvrgDataModel.calculate(i)[0];
		longitudeS = cvrgDataModel.calculate(i)[1];
		try {
			heightS = dynamicTiffReader.GetHeight(longitudeS, latitudeS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*********************************************
		lastConverageAngle = cvrgDataModel.getLastConverageAngle(i);

		thisConverageAngle = cvrgDataModel.getViewAngle(latitudeS,longitudeS,heightS);

		thisConverageAngle = cvrgDataModel.compareConverageAngle(lastConverageAngle,thisConverageAngle);

		cvrgDataModel.setConverageAngle(i, thisConverageAngle);
		return thisConverageAngle;
	}
	public double getLimitAngle(CvrgDataModel cvrgDataModel,int i,double HL) {
		double latitudeS;
		double longitudeS;
		double limitAngle;
		latitudeS = cvrgDataModel.calculate(i)[0];
		longitudeS = cvrgDataModel.calculate(i)[1];
		limitAngle = cvrgDataModel.getViewAngle(latitudeS,longitudeS,HL);
		return limitAngle;
	}
	
}
