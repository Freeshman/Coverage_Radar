import cvrgController.CvrgController;

import java.io.IOException;

import TiffReader.DynamicTiffReader;
public class CRTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double latitude_center=38;
  		double longitude_center=106;
  		double angle=45;
		 double HL = 10000;
		 double dist = 0.1;
		 double range = 500000;
		 double height_center = 12;
		 double height_real;
		 DynamicTiffReader dynamicTiffReader=new DynamicTiffReader(longitude_center, latitude_center);
		 double tmp=dynamicTiffReader.GetHeight(longitude_center, latitude_center);
			if(tmp<0)tmp=0;
			height_real=height_center+tmp;
			CvrgController cvrgController=new CvrgController();
  		for(int k=0;k<(int)360.0/angle;k++) {

        	double[] tmp_LB=cvrgController.start(100,dynamicTiffReader,latitude_center,longitude_center,height_real,angle*k,dist,range,HL);
	}
	}
}
