package application;

import java.io.IOException;

import TiffReader.DynamicTiffReader;

public class TestApp {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CoordinateSys coordinateSys= new CoordinateSys();
		//coordinateSys.test();
		double CL = 170;
		double CB = 40;
		double CH = 100;
		double SN = 0;
		double SE ;
		double SU = 0;
		for(int i=1; i<=70; i++) {
			SE = 100 + i*20000;
			double[] JWHS = coordinateSys.NEU2JWH(SN,SE,SU,CL,CB,CH);
//			System.out.println(JWHS[0] + " "+JWHS[1] +" "+ JWHS[2]);
			System.out.println(JWHS[0] );
			System.out.println(JWHS[1] );
//			double[] NEUS = coordinateSys.JWH2NEU(JWHS[0], JWHS[1], JWHS[2], CL, CB, CH);
//			System.out.println(NEUS[0] + " "+NEUS[1] +" "+ NEUS[2]);
		}
	}

}
