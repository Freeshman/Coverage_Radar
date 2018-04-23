package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import TiffReader.DynamicTiffReader;


public class TopRadar extends SuperRadar{
	double[][] maxPowerArray = new double[(int) ((int)360/thetaAngle)][2];
	double[][] viewArray =  new double[(int) ((int)360/thetaAngle)][2];//����Χ
	double[][] array = new double[(int) ((int)360/thetaAngle)][2];
	double[][] sheildArray = new double[(int) ((int)360/thetaAngle)][2];
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long a = System.currentTimeMillis();
		TopRadar topRadar2 =new TopRadar();
		topRadar2.calculateAllRay();



		long b = System.currentTimeMillis();
		System.out.println(b-a);
	}
	public void calculateAllRay() {
		DynamicTiffReader dynamicTiffReader2 = null;
		try {
			dynamicTiffReader2 = new DynamicTiffReader(longitudeC,latitudeC);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		double[][] result = new double[360][2];
//		double[][] sheildResult = new double[360][2];
				double[][] result1 = new double[90][2];
				double[][] result2 = new double[90][2];
				double[][] result3 = new double[90][2];
				double[][] result4 = new double[90][2];
				double[][] sheildResult1 = new double[90][2];
				double[][] sheildResult2 = new double[90][2];
				double[][] sheildResult3 = new double[90][2];
				double[][] sheildResult4 = new double[90][2];
				double[][] view1 = new double[90][2];
				double[][] view2 = new double[90][2];
				double[][] view3 = new double[90][2];
				double[][] view4 = new double[90][2];
				ThreadRadar thread1 = new ThreadRadar(0,90,dynamicTiffReader2,this);
			    thread1.start();
			    result1 = thread1.result;
			    sheildResult1 = thread1.sheild;
			    view1 = thread1.view;
			    ThreadRadar thread2 = new ThreadRadar(90,180,dynamicTiffReader2,this);
			    thread2.start();
			    result2 = thread2.result;
			    sheildResult2 = thread2.sheild;
			    view2 = thread2.view;
			    ThreadRadar thread3 = new ThreadRadar(180,270,dynamicTiffReader2,this);
			    thread3.start();
			    result3 = thread3.result;
			    sheildResult3 = thread3.sheild;
			    view3 = thread3.view;
			    ThreadRadar thread4 = new ThreadRadar(270,360,dynamicTiffReader2,this);
			    thread4.start();
			    result4 = thread4.result;
			    sheildResult4 = thread4.sheild;
			    view4 = thread4.view;
//		ThreadRadar thread = new ThreadRadar(0,360,dynamicTiffReader2,this);
//	    thread.start();
//	    result = thread.result;
//	    sheildResult = thread.sheild;
          try {
//        	  thread.join();
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
          } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
          };

          for(int k=0;k<90;k++) {
        	  array[k] = result1[k];
        	  sheildArray[k] = sheildResult1[k];
        	  viewArray[k] = view1[k];
          }
          for(int k=90;k<180;k++) {
        	  array[k] = result2[k-90];
        	  sheildArray[k] = sheildResult2[k-90];
        	  viewArray[k] = view2[k-90];
          }
          for(int k=180;k<270;k++) {
        	  array[k] = result3[k-180];
        	  sheildArray[k] = sheildResult3[k-180];
        	  viewArray[k] = view3[k-180];
          }
          for(int k=270;k<360;k++) {
        	  array[k] = result4[k-270];
        	  sheildArray[k] = sheildResult4[k-270];
        	  viewArray[k] = view4[k-270];
          }
//          for(int k=0;k<360;k++) {
//        	  array[k] = result[k];
//        	  sheildArray[k] = sheildResult[k];
//          }


	}

}
class ThreadRadar extends Thread {
	DynamicTiffReader dynamicTiffReader2;
	SuperRadar bRadar;
	int begin;
	int end;
	double[][] view;
	double[][] maxPower;
	double[][] result;
	double[][] sheild;
	ThreadRadar(int begin,int end ,DynamicTiffReader dynamicTiffReader2,SuperRadar superRadar){
		this.dynamicTiffReader2 = dynamicTiffReader2;
		bRadar = new SuperRadar();
		bRadar.latitudeC = superRadar.latitudeC;
		bRadar.longitudeC = superRadar.longitudeC;
		bRadar.HL = superRadar.HL;
		bRadar.range = superRadar.range;
		bRadar.heightC_real=superRadar.heightC_real;
		bRadar.height_center=superRadar.height_center;
		this.begin = begin;
		this.end = end;
		result = new double[end - begin][2];
		sheild = new double[end - begin][2];
		view = new double[end - begin][2];
	}
    public void run() {

  	  bRadar.setDynamicTiffReader(dynamicTiffReader2);
  	  for(int i=begin;i<end;i++) {
				//System.out.println(i);

  		  	bRadar = bRadar.calculateOneRay(i);
				result[i-begin] = bRadar.oneRange;
				sheild[i-begin] = bRadar.sheild;
				view[i-begin] = bRadar.mostFarLonLat;
//				System.out.println(i);
//				System.out.print(sheild[i-begin][1]+" ");
//				System.out.println(sheild[i-begin][0]);
//				System.out.print(result[i-begin][1]+" ");
//	        	System.out.println(result[i-begin][0]);
			}
    }

}
