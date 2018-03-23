package Lambert;

public class Projection
{
	private  double longitude0;
	private static  double latitude0;
	private static double a,b;
	private static  double e;
	private static double l0;
	private double phi0,phi1,phi2;
	private double mb1,mb2,tb0,tb1,tb2;
	private static double n;
	private static double F;
	private static double r0;
	public Projection(double longitude,double latitude){
			longitude0=longitude;
			latitude0=latitude;

			 a=6378140;


//	         e = 0.016710219;
//	         e = 0.000900000;
	         e=0;
	         l0 = (Math.PI / 180) * longitude0;
	        phi0 = (Math.PI / 180) * latitude0; //latitude d'origine en radian
	        phi1 = (Math.PI / 180) * 25; //1er parallele automécoïque
	        phi2 = (Math.PI / 180) * 47; //2eme parallele automécoïque
	         mb1 = Math.cos(phi1) / Math.sqrt(1 - e * e * Math.sin(phi1) * Math.sin(phi1));
	         mb2 = Math.cos(phi2) / Math.sqrt(1 - e * e * Math.sin(phi2) * Math.sin(phi2));
//	         System.out.println("mb1="+mb1+" mb2="+mb2+" phi0="+phi0+" phi1="+phi1+" phi2="+phi2);
	        //calculs des latitudes isométriques
	         tb1 = Math.tan(Math.PI / 4 - phi1 / 2) /Math.pow((1 - e * Math.sin(phi1)) / (1 + e * Math.sin(phi1)), e / 2);
	         tb2 = Math.tan(Math.PI / 4 - phi2 / 2) / Math.pow((1 - e * Math.sin(phi2)) / (1 + e * Math.sin(phi2)), e / 2);
	         tb0 = Math.tan(Math.PI / 4 - phi0 / 2) / Math.pow((1 - e * Math.sin(phi0)) / (1 + e * Math.sin(phi0)), e / 2);
//	         System.out.println("tb1="+tb1+" tb2="+tb2+" tb0="+tb0);
	         n = Math.log((mb1 / mb2 ))/Math.log(tb1/tb2);//ok
	         F=mb1/(n*Math.pow(tb1,n));
	         r0=a*F*Math.pow(tb0,n);
//	         System.out.println("F="+F+" n="+n+" r0="+r0);
	}
//	public static void main(String[] args) {
//		double latitude=40;
//		double longitude=105;
//		double[] result=WGS842Lambert93(latitude,longitude);
////		double[] result=WGS84ToLambert93(39.1097618747,117.3529134562);
//		System.out.println("Lambert变换前的经纬度="+longitude+"    "+latitude);
//		System.out.println("Lambert变换后的坐标(x,y)=("+result[0]+","+result[1]+")");
////		System.out.println("Lambert反变换的经纬度="+result2[1]+"    "+result2[0]);
////		LambertPoint pt = Lambert.convertToWGS84Deg(result[0], result[1], LambertZone.Lambert93);
////    	 System.out.println("Point latitude:" + pt.getY() + " longitude:" + pt.getX());
//	}


	    /// <summary>
	    ///
	    /// </summary>
	    /// <param name="latitude"></param>
	    /// <param name="longitude"></param>
	    /// <returns></returns>
	    public  double[] WGS842Lambert(double longitude, double latitude)
	    {
	        /**** Conversion latitude,longitude en coordonée lambert 93 ****/
	        // Projection conforme sécante, algo détailler dans NTG_71.pdf : http://www.ign.fr/affiche_rubrique.asp?rbr_id=1700&lng_id=FR
	        //  > ACCUEIL > L'offre IGN Pro > Géodésie > RGF93 > Outils

	        //doubleiables:



	        double phi = (Math.PI / 180) * latitude;
	        double l = (Math.PI / 180) * longitude;

	        double tb = Math.tan(Math.PI / 4 - phi / 2) / Math.pow((1 - e * Math.sin(phi)) / (1 + e * Math.sin(phi)), e / 2);
//	        System.out.println("e="+e+" phi="+phi);
	        // double m=Math.cos(phi)/Math.sqrt(1-e*e*Math.sin(phi)*Math.sin(phi));
	        //calcul de l'exposant de la projection
	        double r=a*F*Math.pow(tb,n);
	        double theta=n*(l-l0);
//	        System.out.println("tb="+tb+"r="+r+" theta="+theta);
	        double x=r0-r*Math.cos(theta);
	        double y=r*Math.sin(theta);
	        double[] tabXY = new double[2];

	        tabXY[0] = x;
	        tabXY[1] = y;

	        return tabXY;
	    }
 public  double[] Lambert2WGS84(double x, double y)
	    {
	    	double r=n/Math.abs(n)*Math.sqrt(y*y+(r0-x)*(r0-x));
	    	double t=Math.pow(r/(a*F),1/n);
	    	double theta=Math.atan(y/(r0-x));
	    	double B0=Math.PI;
	    	double B1=Math.PI/2-2*Math.atan(t*Math.pow((1 - e * Math.sin(B0)) / (1 + e * Math.sin(B0)), e / 2));
	    	double epsilon=Math.pow(10,-25 );
	    	while(Math.abs(B0-B1)<epsilon){
	    		B0=B1;
	    		B1=Math.PI/2-2*Math.atan(t*Math.pow((1 - e * Math.sin(B0)) / (1 + e * Math.sin(B0)), e / 2));

	    	}
	    	double L=theta/n+l0;
	    	double[] LB=new double[2];
	    	LB[0]=L*180/Math.PI;
	    	LB[1]=B1*180/Math.PI;
//	    	System.out.println("r="+r+" theta="+theta+" t="+t);
	    	return LB;
	    }
}