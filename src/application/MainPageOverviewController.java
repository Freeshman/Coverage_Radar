package application;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.geom.Point2D;
import java.awt.print.Printable;
import java.io.IOException;


import Lambert.Projection;
import application.CoordinateSys;
import application.OurTiffReader;
public class MainPageOverviewController {

	@FXML
	private AnchorPane myAnchorPane;
//	@FXML
//	private VBox myBox;
//	@FXML
//	private Pane myPane;
	@FXML
	private BorderPane mainPage;
	@FXML
	private ImageView imageView;
    @FXML
    private WebView mapWebView;
    // Reference to the main application.
    private MainApp mainApp;
    private double longitude_center;
    private double latitude_center;
    private double longitude1;
    private double latitude1;
    private double longitude2;
    private double latitude2;
    private double longitude3;
    private double latitude3;
    private double longitude4;
    private double latitude4;
    private double[][] P_lambert;
    private int[][] Pixel_map;
    private double coef;//鍦板浘鏀惧ぇ绯绘暟
    /*								 /\ latitude
	 * 0--------------------> y		  |
	 * |  1-------------2			  |
	 * |  |			    |			  |
	 * |  4-------------3			  |
	 * V  ----------------------------|-------------> lontitude
	 * x
	 */
	double Height;
	double Width;
	int windowWidth;
    int windowHeight;
	double coefx;
	double coefy;
    int step;
	double xv;
	  double yv;
	  double wv;
	  double hv;
	private boolean zoomInFlag;
//	private projectionHelper projection;
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MainPageOverviewController() {
    	windowHeight=1500;
    	windowWidth=1500;
    	step=20;
    	 xv=0;
  	   yv=0;
  	   wv=0;
  	   hv=0;
//  		latitude_center=39.1097618747;
//  		longitude_center=117.3529134562;
  		latitude_center=37.25;
  		longitude_center=117.25;
//  		for(int i=0;i<4;i++){
//  			P[i]=new double[2];
//  			Pixel[i]=new double[2];
//  		}
  		P_lambert=new double[4][2];
  		Pixel_map=new int[4][2];
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * @throws IOException
     */
    @FXML
    private void initialize() throws IOException {



        // Listen for selection changes and show the person details when changed.

        WebEngine webEngine = new WebEngine();
        webEngine=mapWebView.getEngine();
//        webEngine.load("https://map.baidu.com/");
//        webEngine.load("http://api.map.baidu.com/marker?location=39.1097618747,117.3529134562&title=SIAE&content=鐧惧害濂庣澶у帵&output=html");
        webEngine.load("http://uri.amap.com/marker?location="+"latitude=39.1097618747,"+"117.3529134562&title=SIAE&content=SIAE&output=html");
//    	mainPage.getChildren().remove(mapWebView.getId());

        coefx=windowWidth/imageView.getFitWidth();
    	coefy=windowHeight/imageView.getFitHeight();
//    	Height=mapWebView.getHeight();
//  		Width=mapWebView.getWidth();
  		coef=Math.pow(10, -3);
  		latitude1=latitude_center+coef*windowHeight/2;//y
  		longitude1=longitude_center-coef*windowWidth/2;//x

  		latitude2=latitude_center+coef*windowHeight/2;
  		longitude2=longitude_center+coef*windowWidth/2;

  		latitude3=latitude_center-coef*windowHeight/2;
  		longitude3=longitude_center+coef*windowWidth/2;

  		latitude4=latitude_center-coef*windowHeight/2;
  		longitude4=longitude_center-coef*windowWidth/2;



        System.out.println(longitude_center+" "+latitude_center);
  		System.out.println(longitude1+" "+latitude1);
  		System.out.println(longitude2+" "+latitude2);
  		System.out.println(longitude3+" "+latitude3);
  		System.out.println(longitude4+" "+latitude4);
//    	System.out.println(Height+" "+Width);
    	//ZBTJ 117.345 39.123333333333335
//    	projection=new projectionHelper();
  		Projection projection=new Projection(longitude_center,latitude_center);
    	double[] result0=projection.WGS842Lambert(longitude_center,latitude_center);
    	double[] result1=projection.WGS842Lambert(longitude1,latitude1);
    	double[] result2=projection.WGS842Lambert(longitude2,latitude2);
    	double[] result3=projection.WGS842Lambert(longitude3,latitude3);
    	double[] result4=projection.WGS842Lambert(longitude4,latitude4);

		System.out.println("Lambert鍙樻崲鍓嶇殑缁忕含搴�="+longitude1+"    "+latitude1);
		System.out.println("Lambert鍙樻崲鍚庣殑鍧愭爣(x,y)=("+result0[0]+","+result0[1]+")");
		System.out.println("Lambert鍙樻崲鍚庣殑鍧愭爣(x,y)=("+result1[0]+","+result1[1]+")");
		System.out.println("Lambert鍙樻崲鍚庣殑鍧愭爣(x,y)=("+result2[0]+","+result2[1]+")");
		System.out.println("Lambert鍙樻崲鍚庣殑鍧愭爣(x,y)=("+result3[0]+","+result3[1]+")");
		System.out.println("Lambert鍙樻崲鍚庣殑鍧愭爣(x,y)=("+result4[0]+","+result4[1]+")");
		double[] test=projection.Lambert2WGS84(result1[0], result1[1]);
		System.out.println("Lambert 鍙嶅彉鎹㈠悗鐨勭粡绾害="+test[0]+" "+test[1]);
		/*
		 * Four points of the window
		 */
		  		P_lambert[0][0]=result1[0];
		  		P_lambert[0][1]=result1[1];
		  		P_lambert[1][0]=result2[0];
		  		P_lambert[1][1]=result2[1];
		  		P_lambert[2][0]=result3[0];
		  		P_lambert[2][1]=result3[1];
		  		P_lambert[3][0]=result4[0];
		  		P_lambert[3][1]=result4[1];
    	/*
    	 * Four pixel indexs of the four points in the DEM file
    	 */
		Pixel_map[0][0]=(int) Math.round(6001*(longitude1-115)/5.0);
		Pixel_map[0][1]=(int) Math.round(6001*(40-latitude1)/5.0);
		Pixel_map[1][0]=(int) Math.round(6001*(longitude2-115)/5.0);
		Pixel_map[1][1]=(int) Math.round(6001*(40-latitude2)/5.0);
		Pixel_map[2][0]=(int) Math.round(6001*(longitude3-115)/5.0);
		Pixel_map[2][1]=(int) Math.round(6001*(40-latitude3)/5.0);
		Pixel_map[3][0]=(int) Math.round(6001*(longitude4-115)/5.0);
		Pixel_map[3][1]=(int) Math.round(6001*(40-latitude4)/5.0);

//		Pixel[1].setLocation((int) Math.round(6001*(longtitude2-115)/5.0),(int) Math.round(6001*(40-latitude2)/5.0));
//		Pixel[2].setLocation((int) Math.round(6001*(longtitude3-115)/5.0),(int) Math.round(6001*(40-latitude3)/5.0));
//		Pixel[3].setLocation((int) Math.round(6001*(longtitude4-115)/5.0),(int) Math.round(6001*(40-latitude4)/5.0));
    	System.out.println(Pixel_map[0]+" "+Pixel_map[1]+" "+Pixel_map[2]+" "+Pixel_map[3]+" ");
//		int Pixel_ZBTJx=(int) Math.round(6001*(117.345-115)/5.0);
//		int Pixel_ZBTJy=(int) Math.round(6001*(40-39.123333333333335)/5.0);

//		System.out.println(getPixel(Pixel_ZBTJx, Pixel_ZBTJy));

   		System.out.println(P_lambert[0]);


//        mainPage.setCenter(imageView);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    public void handleScroll(ScrollEvent se) {

    	double rate=1.1;
    	double deltaY = se.getDeltaY();

    	if (deltaY > 0) {// 鍚戜笂婊氬姩,鏀惧ぇ鍥剧墖
    		/*
    		 double width = imageView.getFitWidth() * rate;
    		double height = imageView.getFitHeight() * rate;
//    		if(width>myAnchorPane.getWidth())width=myAnchorPane.getWidth();
//    		if(height>myAnchorPane.getHeight())height=myAnchorPane.getHeight();
    		imageView.setFitWidth(width);
    		imageView.setFitHeight(height);
    		*/
    		step-=2;
    		if(step<5)step=5;
    	} else {// 鍚戜笅婊氬姩,缂╁皬鍥剧墖
    		/*
    		double width = imageView.getFitWidth() / rate;
    		double height = imageView.getFitHeight() / rate;
    		if(width<200)width=200;
    		if(height<150)height=150;
    		imageView.setFitWidth(width);
    		imageView.setFitHeight(height);
    		*/
    		step+=2;
    		if(step>100)step=100;
    	}
    	if(se.getX()<step){
  		  xv=0;
  		  wv=2*step;
  	  }else if(se.getX()>imageView.getFitWidth()-step){
  		  xv=imageView.getFitWidth()-2*step;
  		  wv=2*step;
  	  }else{
  		  xv= se.getX()-step;
  		  wv=2*step;

  	  }
  	  if(se.getY()<step){
  		  yv=0;
  		  hv=2*step;
  	  }else if(se.getY()>imageView.getFitHeight()-step){
  		  yv=imageView.getFitHeight()-2*step;
  		  hv=2*step;
  	  }else{
  		  yv=se.getY()-step;
  		  hv=2*step;
  	  }
  	Rectangle2D viewportRect = new Rectangle2D(coefx*xv, coefy*yv,coefx*wv,coefy*hv);
    	imageView.setViewport(viewportRect);
    }


    @FXML
    private void zoomIn(MouseEvent e){
    	CoordinateSys coordinateSys=new CoordinateSys();
    	coordinateSys.test();
    		MouseButton button = e.getButton();
            switch(button) {
              case PRIMARY:
            	  zoomInFlag=true;

//             	 System.out.println("Point latitude:" + pt.getY() + " longitude:" + pt.getX());

            	  if(e.getX()<step){
            		  xv=0;
            		  wv=2*step;
            	  }else if(e.getX()>imageView.getFitWidth()-step){
            		  xv=imageView.getFitWidth()-2*step;
            		  wv=2*step;
            	  }else{
            		  xv=(int) (e.getX()-step);
            		  wv=2*step;

            	  }
            	  if(e.getY()<step){
            		  yv=0;
            		  hv=2*step;
            	  }else if(e.getY()>imageView.getFitHeight()-step){
            		  yv=imageView.getFitHeight()-2*step;
            		  hv=2*step;
            	  }else{
            		  yv=(int) (e.getY()-step);
            		  hv=2*step;
            	  }

          			Rectangle2D viewportRect = new Rectangle2D(coefx*xv, coefy*yv,coefx*wv,coefy*hv);
          			imageView.setViewport(viewportRect);
            	  	break;
              case SECONDARY:
            	  imageView.setViewport(null);
            	  break;
//              case MIDDLE: System.out.println("Middle Button Pressed"); break;
//              default:
//                System.out.println(button);
			default:
				break;
            }


//    	}else{
//    		zoomInFlag=false;
//    	}
    }
    @FXML
    private void Test() throws IOException{
        show_DEM();
    }
    @FXML
    private void show_DEM() throws IOException{

//        if(windowHeight>6000)windowHeight=6000;
//        if(windowWidth>6000)windowWidth=6000;
         /* Original
        WritableImage writableImage = new WritableImage(windowWidth,windowHeight);
        PixelWriter pw = writableImage.getPixelWriter();
        OurTiffReader tiffReader=new OurTiffReader();
        tiffReader.loadDEM("srtm_60_05.tif");
        for(int i=0;i<windowWidth;i++)
        	for(int j=0;j<windowHeight;j++)
        		pw.setColor(i, j, ColorMap(tiffReader.getPixel(i, j), 950));

        imageView.setImage(writableImage);

        */
   /*
        6000*6000

        鍖楃含60搴﹁嚦鍗楃含60搴�
        3' 90m
        longitude:-180
        latitude:60
        60_05:
        (n-1)*5-180=115
        60-(n-1)*5=40
        */
//        /*
        WritableImage writableImage = new WritableImage(6000,6000);
        PixelWriter pw = writableImage.getPixelWriter();
        OurTiffReader tiffReader=new OurTiffReader();
        tiffReader.loadDEM("srtm_60_05.tif");
        Projection projection=new Projection(longitude_center,latitude_center);
        int ix=0;
        int jy=0;
        System.out.println(latitude2);
        System.out.println("i0="+tiffReader.pixelx(latitude1)+" iend="+tiffReader.pixelx(latitude4));
        System.out.println("j0="+tiffReader.pixely(longitude1)+" jend="+tiffReader.pixely(longitude2));
//        int row=tiffReader.pixelx(latitude1);////////////Fuck!!!!
//        int col=tiffReader.pixely(longitude1);
        int col=0;
        int row=0;
        System.out.println("row="+row+" col="+col);
//        for(int row=tiffReader.pixelx(latitude1);row<tiffReader.pixelx(latitude4);row++){
//        	for(int col=tiffReader.pixely(longitude1);col<tiffReader.pixely(longitude2);col++){
                for(int k=0;k<=6000;k++){
//                	row=tiffReader.pixelx(latitude1);
                	row=0;
                	for(int l=0;l<=6000;l++){

        		double longitude=longitude1+k*3/3600.0;
        		double latitude=latitude1-l*3/3600.0;
//                System.out.println("i="+row+" j="+col);
//        		System.out.println("long1="+longitude1+" lat1="+latitude1);
//        		System.out.println("long="+longitude+" lat="+latitude);
        		double[] tmp=projection.WGS842Lambert(longitude,latitude);
//        		System.out.println(tmp[0]+" "+tmp[1]);
        		ix=(int) ((P_lambert[0][0]-tmp[0])/600.0);
        		jy=(int) ((tmp[1]-P_lambert[3][1])/600.0);
//        		l++;

//                System.out.println("k="+k+" l="+l+" ix="+ix+" jy="+jy+" P_lambert="+P_lambert[0][0]+" "+P_lambert[3][1]);
//                System.out.println("row="+row+" col="+col);
//                System.out.println(ColorMap(tiffReader.getPixel(row,col),950).getRed()+" "+ColorMap(tiffReader.getPixel(row,col),950).getGreen()+" "+ColorMap(tiffReader.getPixel(row,col),950).getBlue());
        		pw.setColor(jy+250, ix+250, ColorMap(tiffReader.getPixel(row,col), 950));
        		row++;
        	}
//        	k++;
                	col++;
        }

        System.out.println("Here 351");
        imageView.setImage(writableImage);
//        */
//    	imageView.setVisible(true);
//        mainPage.setCenter(imageView);
    }
    private Color ColorMap(float z,float average){
    	float r,g,b;
    	float Max=2*average;
    	float slop=1/(average/2);
    	if(z<=0){
    		r=0;
    		g=0;
    		b=0;
    	}
    	else if(z<Max/4){
    		b=slop*z;
    		g=0;
    		r=0;
    	}else if(z<average){
    		b=-slop*(z-average/2)+1;
    		g=slop*(z-Max/4);
    		r=0;
    	}else if(z<3*Max/4){
    		b=0;
    		g=1;
    		r=slop*(z-average);
    	}else if(z<Max){
    		b=0;
    		g=-slop*(z-3*Max/4)+1;
    		r=1;
    	}else if(z>=Max){
    		b=0;
    		g=0;
    		r=1;
    	}else{
    		r=g=b=0;
    	}
    	return new Color(r,g,b,1);
    }
    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {

    }
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeletePerson() {

    }
    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewPerson() {


    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleConfiguration() {
         boolean okClicked = mainApp.showConfigurationDialog();
        }
}