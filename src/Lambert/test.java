package Lambert;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Projection projection=new Projection(117.25, 37.25);
		double[] check=projection.WGS842Lambert(117.25, 37.25);
		double[] e=projection.Lambert2WGS84(0, 0);
		double[] result1=projection.Lambert2WGS84(10000, 10000);
		double[] result2=projection.Lambert2WGS84(-10000, -10000);
		double[] result3=projection.Lambert2WGS84(0, 1000);
		double[] result4=projection.Lambert2WGS84(1000, 0);
		double[] result5=projection.Lambert2WGS84(0, 0);
//		double[] result1=projection.WGS842Lambert(118.25, 37.25);
//		double[] result2=projection.WGS842Lambert(116.25, 37.25);
//		double[] result3=projection.WGS842Lambert(117.25, 38.25);
//		double[] result4=projection.WGS842Lambert(117.25, 36.25);
//		double[] result5=projection.WGS842Lambert(118.25, 38.25);
		System.out.println("e="+e[0]+","+e[1]);
		System.out.println("check="+check[0]+","+check[1]);
		System.out.println("result1="+result1[0]+","+result1[1]);
		System.out.println("result2="+result2[0]+","+result2[1]);
		System.out.println("result3="+result3[0]+","+result3[1]);
		System.out.println("result4="+result4[0]+","+result4[1]);
		System.out.println("result5="+result5[0]+","+result5[1]);
	}

}
