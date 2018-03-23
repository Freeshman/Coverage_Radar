package application;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.geom.Point2D;
import java.awt.print.Printable;
import java.io.IOException;


import Lambert.Projection;
import application.CoordinateSys;
import TiffReader.DynamicTiffReader;
import TiffReader.OurTiffReader;
public class MainPageOverviewController {
	@FXML
	private TextField longRadar;
	@FXML
	private TextField latRadar;
	@FXML
	private AnchorPane myAnchorPane;
	@FXML
	private TextField longPoint;
	@FXML
	private TextField latPoint;
	@FXML
	private TextArea Console;
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
//    private int[][] Pixel_map;
    private double coef;//鍦板浘鏀惧ぇ绯绘暟
    /*								 /\ xL
	 * 0--------------------> y		  |
	 * |  1-------------2			  |
	 * |  |			    |			  |
	 * |  4-------------3			  |
	 * V  ----------------------------|-------------> yL
	 * x
	 */
	double Height;
	double Width;
	int windowWidth;
    int windowHeight;
	double coefx;
	double coefy;
	double xv;
	  double yv;
	  double wv;
	  double hv;
	Projection projection;
	DynamicTiffReader dynamicTiffReader;
//	private projectionHelper projection;
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MainPageOverviewController() {
    	windowHeight=400;
    	windowWidth=400;
    	 xv=0;
  	   yv=0;
  	   wv=0;
  	   hv=0;
//  		latitude_center=39.1097618747;
//  		longitude_center=117.3529134562;
  		latitude_center=0;
  		longitude_center=0;
  		P_lambert=new double[4][2];

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * @throws IOException
     */
    @FXML
    private void initialize() throws IOException {



        // Listen for selection changes and show the person details when changed.
  		Console.appendText("Hello\n");
        WebEngine webEngine = new WebEngine();
        webEngine=mapWebView.getEngine();
        webEngine.load("http://uri.amap.com/marker?location="+"latitude=39.1097618747,"+"117.3529134562&title=SIAE&content=SIAE&output=html");
        coefx=windowWidth/imageView.getFitWidth();
    	coefy=windowHeight/imageView.getFitHeight();
    	coef=500;
    	projection=new Projection(117.25,39.25);
		 dynamicTiffReader=new DynamicTiffReader(117.25, 39.25);
    	UpdatePostion(117.25,39.25,coef);
		System.out.println(" "+longitude_center+"    "+latitude_center);
   		mapWebView.setVisible(false);

//        mainPage.setCenter(imageView);
    }
    private void UpdatePostion(double Newlongitude,double Newlatitude,double Newcoef) throws IOException {
    	/*
		 * Four points of the window in Lambert coordinate system
		 */

    	if(Newcoef!=coef|Newlongitude!=longitude_center|Newlatitude!=latitude_center){
        	longitude_center=Newlongitude;
        	latitude_center=Newlatitude;
        	coef=Newcoef;
        	longRadar.setText(String.valueOf(longitude_center));
        	latRadar.setText(String.valueOf(latitude_center));
        	projection=new Projection(longitude_center,latitude_center);
    	double[] result0=projection.WGS842Lambert(longitude_center,latitude_center);
		P_lambert[0][0]=result0[0]+coef*windowHeight/2.0;//x
  		P_lambert[0][1]=result0[1]-coef*windowWidth/2.0;//y
  		P_lambert[1][0]=result0[0]+coef*windowHeight/2.0;//x
  		P_lambert[1][1]=result0[1]+coef*windowWidth/2.0;//y
  		P_lambert[2][0]=result0[0]-coef*windowHeight/2.0;//x
  		P_lambert[2][1]=result0[1]+coef*windowWidth/2.0;//y
  		P_lambert[3][0]=result0[0]-coef*windowHeight/2.0;//x
  		P_lambert[3][1]=result0[1]-coef*windowWidth/2.0;//y
  		double[] tmp=projection.Lambert2WGS84(P_lambert[0][0], P_lambert[0][1]);
		latitude1=tmp[1];
  		longitude1=tmp[0];
  		tmp=projection.Lambert2WGS84(P_lambert[1][0], P_lambert[1][1]);
  		latitude2=tmp[1];
  		longitude2=tmp[0];
  		tmp=projection.Lambert2WGS84(P_lambert[2][0], P_lambert[2][1]);
  		latitude3=tmp[1];
  		longitude3=tmp[0];
  		tmp=projection.Lambert2WGS84(P_lambert[3][0], P_lambert[3][1]);
  		latitude4=tmp[1];
  		longitude4=tmp[0];
//  		System.out.println("");
//  		System.out.println("4 points location in Lambert world");
//  		System.out.println("");
//  		System.out.println(P_lambert[0][0]+", "+P_lambert[0][1]);
//  		System.out.println(P_lambert[1][0]+", "+P_lambert[1][1]);
//  		System.out.println(P_lambert[2][0]+", "+P_lambert[2][1]);
//  		System.out.println(P_lambert[3][0]+", "+P_lambert[3][1]);
//  		System.out.println("");
//  		System.out.println("4 points LB");
//  		System.out.println("");
//        System.out.println(longitude_center+" "+latitude_center);
//  		System.out.println("");
//  		System.out.println(longitude1+", "+latitude1);
//  		System.out.println(longitude2+", "+latitude2);
//  		System.out.println(longitude3+", "+latitude3);
//  		System.out.println(longitude4+", "+latitude4);
//  		System.out.println("");

    	}
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
    public void handleScroll(ScrollEvent se) throws IOException {

//    	double rate=1.1;
    	double deltaY = se.getDeltaY();
    	double Newcoef=0;
    	if (deltaY > 0) {// 鍚戜笂婊氬姩,鏀惧ぇ鍥剧墖
    		/*
    		 double width = imageView.getFitWidth() * rate;
    		double height = imageView.getFitHeight() * rate;
//    		if(width>myAnchorPane.getWidth())width=myAnchorPane.getWidth();
//    		if(height>myAnchorPane.getHeight())height=myAnchorPane.getHeight();
    		imageView.setFitWidth(width);
    		imageView.setFitHeight(height);
    		*/
    		Newcoef=coef-20;
    		if(Newcoef<50)Newcoef=50;
    	} else {// 鍚戜笅婊氬姩,缂╁皬鍥剧墖
    		/*
    		double width = imageView.getFitWidth() / rate;
    		double height = imageView.getFitHeight() / rate;
    		if(width<200)width=200;
    		if(height<150)height=150;
    		imageView.setFitWidth(width);
    		imageView.setFitHeight(height);
    		*/
    		Newcoef=coef+20;
    		if(Newcoef>1200)Newcoef=1200;
    	}
//    	System.out.println("");
//  		System.out.println("newcoef="+Newcoef);
//  		System.out.println("");
    	UpdatePostion(longitude_center, latitude_center, Newcoef);
    	show_DEM();
    }


    @FXML
    private void PickPosition(MouseEvent e) throws IOException{
//    	CoordinateSys coordinateSys=new CoordinateSys();
//    	coordinateSys.test();
    		MouseButton button = e.getButton();
            switch(button) {
              case PRIMARY:
//            	  Console.appendText("x="+String.valueOf(e.getX()));
//            	  Console.appendText("y="+String.valueOf(e.getY()));
//            	  System.out.println("x="+e.getX()+" y="+e.getY());
            	  double NewPosition_Ly=(50*e.getX()-windowWidth/2.0);
            	  double NewPosition_Lx=-(50*e.getY()-windowHeight/2.0);
            	  longPoint.setText(String.valueOf(NewPosition_Ly));
            	  latPoint.setText(String.valueOf(NewPosition_Lx));
//            	  System.out.println("NewPosiont="+-(e.getX()-windowHeight/2.0)+", "+(e.getY()-windowWidth/2.0));
//            	  System.out.println("NewPosiont="+NewPosition_Lx+", "+NewPosition_Ly);
//            	  double[] check=projection.Lambert2WGS84(0, 0);
//            	  System.out.println("Check="+check[0]+", "+check[1]);
            	  double[] NewLB=projection.Lambert2WGS84(NewPosition_Lx, NewPosition_Ly);
//            	  System.out.println("So ="+NewLB[0]+", "+NewLB[1]);
            	  UpdatePostion(NewLB[0],NewLB[1],coef);
            	  show_DEM();
            	  break;
              case SECONDARY:
            	  UpdatePostion(115.1, 40.1, 100);
            	  show_DEM();
            	  break;
            }
//             	 System.out.println("Point latitude:" + pt.getY() + " longitude:" + pt.getX());


    }
    @FXML
    private void Test() throws IOException{
        dynamicTiffReader.FileName();
    }
    @FXML
    private void show_DEM() throws IOException{
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
        WritableImage writableImage = new WritableImage(windowHeight,windowWidth);
        PixelWriter pw = writableImage.getPixelWriter();

         projection=new Projection(longitude_center,latitude_center);




//        int row=tiffReader.pixelx(latitude1);////////////Fuck!!!!
//        int col=tiffReader.pixely(longitude1);

//        System.out.println("row="+row+" col="+col);
//        for(int row=tiffReader.pixelx(latitude1);row<tiffReader.pixelx(latitude4);row++){
//        	for(int col=tiffReader.pixely(longitude1);col<tiffReader.pixely(longitude2);col++){
//         double[] checkd=projection.Lambert2WGS84(-(int) P_lambert[0][1], -(int)P_lambert[0][0]);
//         String debug="P0 position="+checkd[0]+","+checkd[1]+"\n";
//         Console.appendText(debug);
                for(int row=(int) P_lambert[0][1];row!=(int)P_lambert[1][1];row+=coef){//x
//                	row=tiffReader.pixelx(latitude1);
                	for(int col=(int)P_lambert[0][0];col!=(int)P_lambert[3][0];col-=coef){//y
//                        System.out.println("hello");
                		double[] result=projection.Lambert2WGS84(-row, -col);
//                		System.out.println("-row and -col="+-row+", "+-col);
//                		double[] result=projection.Lambert2WGS84(row*coef, col*coef);

//        		System.out.println("long1="+longitude1+" lat1="+latitude1);
//        		System.out.println("long="+longitude+" lat="+latitude);
//        		System.out.println((int)P_lambert[0][1]+" "+P_lambert[0][0]);
        		int i=(int) ((row-(int)P_lambert[0][1])/coef);
        		int j=-(int) ((col-(int)P_lambert[0][0])/coef);
//                System.out.println("i="+i+" j="+j);
//                System.out.println(" i="+i+" j="+j);

//                Console.appendText("row="+row+" col="+col+"\n");
//                Console.appendText(result[0]+" ,"+result[1]+"\n");
//                System.out.println(ColorMap(tiffReader.getPixel(row,col),950).getRed()+" "+ColorMap(tiffReader.getPixel(row,col),950).getGreen()+" "+ColorMap(tiffReader.getPixel(row,col),950).getBlue());
//        		ColorMap(, 950);
        		int check =(int) dynamicTiffReader.GetHeight(result[0], result[1]);
//        		Console.appendText(check+"\n");
        		pw.setColor(j, i, ColorMap(check, 950));
        	}
//                  System.out.println("row0="+(int) P_lambert[0][0]+" rowend="+(int)P_lambert[3][0]);
//                  System.out.println("col0="+(int)P_lambert[0][1]+" colend="+(int)P_lambert[1][1]);
//        	k++;
        }
//                checkd=projection.Lambert2WGS84(-(int) P_lambert[1][1], -(int)P_lambert[3][0]);
//                debug="P1 position="+checkd[0]+","+checkd[1]+"\n";
//                Console.appendText(debug);

        imageView.setImage(writableImage);
//        */
//    	imageView.setVisible(true);
//        mainPage.setCenter(imageView);
    }
    private Color ColorMap(double d,float average){
    	float r,g,b;
    	float Max=2*average;
    	float slop=1/(average/2);
    	if(d<=0){
    		r=0;
    		g=0;
    		b=0;
    	}
    	else if(d<Max/4){
    		b=(float) (slop*d);
    		g=0;
    		r=0;
    	}else if(d<average){
    		b=(float) (-slop*(d-average/2)+1);
    		g=(float) (slop*(d-Max/4));
    		r=0;
    	}else if(d<3*Max/4){
    		b=0;
    		g=1;
    		r=(float) (slop*(d-average));
    	}else if(d<Max){
    		b=0;
    		g=(float) (-slop*(d-3*Max/4)+1);
    		r=1;
    	}else if(d>=Max){
    		b=0;
    		g=0;
    		r=1;
    	}else{
    		r=g=b=0;
    	}
    	return new Color(r,g,b,1);
//    	return new Color(0,0,0,1);
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