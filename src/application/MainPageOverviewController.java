package application;
import cvrgController.CvrgController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.web.WebView;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;

import org.omg.CORBA.PRIVATE_MEMBER;

import Lambert.Projection;
import TiffReader.DynamicTiffReader;
public class MainPageOverviewController {
	@FXML
	private ProgressIndicator progressMap;
	@FXML
	private ProgressBar progresBar;
	@FXML
	private Label realRadarHeight;
	@FXML
	private TextField longRadar;
	@FXML
	private TextField RadarHeight;
	@FXML
	private TextField RadarTil;
	@FXML
	private TextField FlightLevelInput;
	@FXML
	private TextField latRadar;
	double HL,angle,dist,range,height_center,Til,height_real;
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
	private Canvas imageView;
    @FXML
    private WebView mapWebView;
    @FXML
    private Pane Key;
    // Reference to the main application.
    private MainApp mainApp;
    private Polyline[] polyline ;
    private int polylineIndex;
    private Color[] colorBar;
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
    private DynamicTiffReader dynamicTiffReader_;
//    private int[][] Pixel_map;
    private double coef;//鍦板浘鏀惧ぇ绯绘暟
    private boolean finishflag;
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
//	CvrgController cvrgController;

//	private projectionHelper projection;
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public MainPageOverviewController() {
		 HL = 2000;
		 angle=0.5;
		 range = 250000;
		 height_center = 12;

    	windowHeight=400;
    	windowWidth=400;
    	 xv=0;
  	   yv=0;
  	   wv=0;
  	   hv=0;
  	   Til=0;
//  		latitude_center=39.1097618747;
//  		longitude_center=117.3529134562;
  		latitude_center=38.058942;
  		longitude_center=106.2028889;
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
//  		Console.appendText("Hello\n");
    	  progresBar.setVisible(false);
    	  progressMap.setVisible(false);
  		FlightLevelInput.setText(String.valueOf(HL));
  		RadarHeight.setText(String.valueOf(height_center));
  	    RadarTil.setText(String.valueOf(Til));
  	    longRadar.setText(String.valueOf(longitude_center));
  	    latRadar.setText(String.valueOf(latitude_center));
//        WebEngine webEngine = new WebEngine();
//        webEngine=mapWebView.getEngine();
//        webEngine.load("http://uri.amap.com/marker?location="+"latitude=39.1097618747,"+"117.3529134562&title=SIAE&content=SIAE&output=html");
  	  imageView.setHeight(windowHeight);
  	  imageView.setWidth(windowWidth);
        coefx=windowWidth/imageView.getWidth();
    	coefy=windowHeight/imageView.getHeight();
    	coef=1000;
    	projection=new Projection(longitude_center,latitude_center);
    	polyline=new Polyline[2];
    	polylineIndex=0;
    	colorBar=new Color[5];
    	colorBar[0]=new Color(0, 0, 1, 1);
    	colorBar[1]=new Color(0, 1, 1, 1);
    	colorBar[2]=new Color(0, 0, 1, 1);
    	colorBar[3]=new Color(0.5, 0.5, 1, 1);
    	colorBar[4]=new Color(0, 0, 0, 1);
    	try {
			dynamicTiffReader=new DynamicTiffReader(longitude_center,latitude_center );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.out.println(" "+longitude_center+"    "+latitude_center);
//   		mapWebView.setVisible(false);
   		double tmp=dynamicTiffReader.GetHeight(longitude_center, latitude_center);
		if(tmp<0)tmp=0;
		height_real=height_center+tmp;
//		realRadarHeight.setText(String.valueOf(height_real));
		/*
		 * Here set longitude and latitude =0 is to refresh the map!!!!!
		 *
		 */
		longitude_center=0;
		latitude_center=0;
		finishflag=true;
		UpdatePostion(longitude_center,latitude_center,coef);

//        mainPage.setCenter(imageView);
    }
    private void UpdatePostion(double Newlongitude,double Newlatitude,double Newcoef) throws IOException {
    	/*
		 * Four points of the window in Lambert coordinate system
		 */
    	Console.appendText("coef= "+coef);
    	if(finishflag==true)
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
  		try {
			dynamicTiffReader=new DynamicTiffReader(longitude_center,latitude_center );
			double tmp_=dynamicTiffReader.GetHeight(longitude_center, latitude_center);
			if(tmp_<0)tmp_=0;
			height_real=height_center+tmp_;
			realRadarHeight.setText(String.valueOf(height_real));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	show_DEM();
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

    }


    @FXML
    private void PickPosition(MouseEvent e) throws IOException{
//    	CoordinateSys coordinateSys=new CoordinateSys();
//    	coordinateSys.test();
        if(finishflag==true){
    		MouseButton button = e.getButton();
            switch(button) {
              case PRIMARY:
//            	  windowWidth=(int) imageView.getHeight();
//            	  windowHeight=(int) imageView.getHeight();

//            	  Console.appendText("h="+windowWidth+"\n");
//            	  Console.appendText("w="+windowHeight+"\n");
//            	  Console.appendText("x="+String.valueOf(e.getX())+"\n");
//            	  Console.appendText("y="+String.valueOf(e.getY())+"\n");
//            	  System.out.println("x="+e.getX()+" y="+e.getY());
            	  double NewPosition_Ly=coef*(e.getX()-windowWidth/2.0);
            	  double NewPosition_Lx=-coef*(e.getY()-windowHeight/2.0);
            	  longPoint.setText(String.valueOf(NewPosition_Ly));
            	  latPoint.setText(String.valueOf(NewPosition_Lx));
            	  double[] NewLB=projection.Lambert2WGS84(NewPosition_Lx, NewPosition_Ly);
//            	  System.out.println("So ="+NewLB[0]+", "+NewLB[1]);
            	  UpdatePostion(NewLB[0],NewLB[1],coef);
            	  break;
              case SECONDARY://右键
//            	  UpdatePostion(115.1, 40.1, 100);
            	  break;
            }
        }
//             	 System.out.println("Point latitude:" + pt.getY() + " longitude:" + pt.getX());


    }
    @FXML
    private void Test() throws IOException{
//        dynamicTiffReader.FileName();
//        GraphicsContext gc=imageView.getGraphicsContext2D();
//      gc.clearRect(0, 0, windowWidth, windowHeight);
    	Range();

    }
    @FXML
    private void show_DEM() throws IOException{
    	finishflag=false;
    	dynamicTiffReader_=new DynamicTiffReader(longitude_center, latitude_center);
    	if(polyline[0]!=null)Key.getChildren().remove(polyline[0]);
    	 Range();
    	 Task<Integer> task = new Task<Integer>() {
     		 private int col,row;
     		 private int Height;
     		 private WritableImage img;
 	      	  private Color color;
    		    @Override protected Integer call() throws Exception {
    		    	 progressMap.setVisible(true);
    		    	img=new WritableImage(windowHeight, windowWidth);
    		    	PixelWriter pw=img.getPixelWriter();
    	                for( row=(int) P_lambert[0][1];row!=(int)P_lambert[1][1];row+=2*coef){//x
    	                	for( col=(int)P_lambert[0][0];col!=(int)P_lambert[3][0];col-=coef){//y
    	                		 projection=new Projection(longitude_center,latitude_center);
    	      	               double[] result=projection.Lambert2WGS84(-row, -col);
    	      	              		try {
    									Height =(int) dynamicTiffReader_.GetHeight(result[0], result[1]);
    								} catch (IOException e) {
    									// TODO Auto-generated catch block
    									e.printStackTrace();
    								}
    	      	              		 color=ColorMap(Height, 950);
    	      	            		if((row/coef*row/coef+col/coef*col/coef)==50){
    	      	            			if(color.getBrightness()>0.5){
    	      	            				if(color.getBlue()>0.5)
    	      	            			color= new Color(1,1,1,1);
    	      	            				else color= new Color(0,0,0,1);
    	      	            			}
    	      	            			else if (color.getBrightness()<0.5)
    	      	        				color =new Color(1, 1, 1, 1);
    	      	            		}
	        	    	            int i=(int) ((row-(int)P_lambert[0][1])/coef);
	        	    	       		  int  j=-(int) ((col-(int)P_lambert[0][0])/coef);
	        	    	       		  updateProgress(i+1, windowHeight);

	      	    	            	pw.setColor(j, i, color);

//	        	    	       		Platform.runLater(() -> {
//       	    	       	         progressMap.setProgress((i+1)/400.0);
//       	    	       	    });
    	                	}
    	                	Platform.runLater(() -> {
        	    	       	    GraphicsContext gc=imageView.getGraphicsContext2D();
        	      		        gc.drawImage(img, 0, 0);
        	    	       	    });
    	                }
    	                	 for( row=(int)( P_lambert[0][1]+coef);row!=(int)P_lambert[1][1]-coef;row+=2*coef){//x
    	    	                	for( col=(int)P_lambert[0][0];col!=(int)P_lambert[3][0];col-=coef){//y
    	    	                		 projection=new Projection(longitude_center,latitude_center);
    	    	      	               double[] result=projection.Lambert2WGS84(-row, -col);
    	    	      	              		try {
    	    									Height =(int) dynamicTiffReader_.GetHeight(result[0], result[1]);
    	    								} catch (IOException e) {
    	    									// TODO Auto-generated catch block
    	    									e.printStackTrace();
    	    								}
    	    	      	              		 color=ColorMap(Height, 950);
    	    	      	            		if((row/coef*row/coef+col/coef*col/coef)==50){
    	    	      	            			if(color.getBrightness()>0.5){
    	    	      	            				if(color.getBlue()>0.5)
    	    	      	            			color= new Color(1,1,1,1);
    	    	      	            				else color= new Color(0,0,0,1);
    	    	      	            			}
    	    	      	            			else if (color.getBrightness()<0.5)
    	    	      	        				color =new Color(1, 1, 1, 1);
    	    	      	            		}
    		        	    	            int i=(int) ((row-(int)P_lambert[0][1])/coef);
    		        	    	       		  int  j=-(int) ((col-(int)P_lambert[0][0])/coef);
    		        	    	       		  updateProgress(i+1, windowHeight);
    		      	    	            	pw.setColor(j, i, color);
    	    	                	}
    	            	Platform.runLater(() -> {
    	    	       	    GraphicsContext gc=imageView.getGraphicsContext2D();
    	      		        gc.drawImage(img, 0, 0);
    	    	       	    });
    	    			}
    		        return 0;
    		    }

    		    @Override protected void succeeded() {
    		        super.succeeded();
   		    	 progressMap.setVisible(false);

    		        updateMessage("Done!");
    		    }

    		    @Override protected void cancelled() {
    		        super.cancelled();
    		        updateMessage("Cancelled!");
    		    }

    		@Override protected void failed() {
    		    super.failed();
    		    updateMessage("Failed!");
    		    }
    		};


	      	Thread thread =new Thread(task);
	      	progressMap.progressProperty().bind(task.progressProperty());
	      	thread.start();
    	}






private void  Range() {
    Task<Void> progressTask = new Task<Void>(){
    	  private Polyline tmp;
        @Override
        protected void succeeded() {
            super.succeeded();
            System.out.println("Succeeded");
            polyline[0]=tmp;
            Key.getChildren().add(tmp);
            progresBar.setVisible(false);
            finishflag=true;
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            System.out.println("Cancelled");
        }

        @Override
        protected void failed() {
            super.failed();
            System.out.println("Failed");
        }
        @Override
        protected Void call() throws Exception {
    	 	progresBar.setVisible(true);
            CvrgController cvrgController=new CvrgController();
        	int N = 2777;
    	 	double dist = Math.floor(range/N);//8.25
    	 	tmp=new Polyline();
    	 	Double[] points=new Double[2*(int)(360.0/angle)];
        for(int k=0;k<(int)360.0/angle;k++) {
        	updateProgress(k + 1, (int)360.0/angle);
//            System.out.println("Loading..." + (k + 1) + "%");
        	double[]tmp_LB=cvrgController.start(N,dynamicTiffReader,latitude_center,longitude_center,height_real,angle*k,dist,range,HL);
        	double[]tmp_Lambert=projection.WGS842Lambert(tmp_LB[0], tmp_LB[1]);
        	if(tmp_Lambert[0]<=P_lambert[0][0]&&tmp_Lambert[0]>=P_lambert[3][0]&&tmp_Lambert[1]>=P_lambert[0][1]&&tmp_Lambert[1]<=P_lambert[1][1]){
        	int i=(int) ((tmp_Lambert[1]-(int)P_lambert[0][1])/coef);
        	int j=-(int) ((tmp_Lambert[0]-(int)P_lambert[0][0])/coef);
    		points[2*k]=Double.valueOf(i);
    		points[2*k+1]=Double.valueOf(j);
    		}
        }
        	tmp.getPoints().addAll(points);
           	tmp.setStroke(colorBar[polylineIndex]);
//            System.out.println("Finish");
            return null;
        }
  };
  		progresBar.progressProperty().bind(progressTask.progressProperty());
	 	new Thread(progressTask).start();


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
    @FXML
    private void handleNewLocation(){
    	try {
			UpdatePostion(Double.valueOf(longRadar.getText()).doubleValue(), Double.valueOf(latRadar.getText()).doubleValue(), coef);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    private void handleStartDrag(MouseEvent e)throws IOException{
//    	System.out.println("start drag");
     if(finishflag==true){
  	  double NewPosition_Ly=-coef*(e.getX()-windowWidth/2.0);
  	  double NewPosition_Lx=coef*(e.getY()-windowHeight/2.0);
  	  longPoint.setText(String.valueOf(NewPosition_Ly));
  	  latPoint.setText(String.valueOf(NewPosition_Lx));
  	  double[] NewLB=projection.Lambert2WGS84(NewPosition_Lx, NewPosition_Ly);
  	  UpdatePostion(NewLB[0],NewLB[1],coef);
     }
    }
    @FXML
    private void handleExit(){
    	System.exit(0);
    }
    @FXML
    private void UpdateFL(KeyEvent e){
        if(finishflag==true){
    	if(e.getCode()==KeyCode.ENTER){
    	HL=Double.valueOf(FlightLevelInput.getText()).doubleValue();
    	try {
			show_DEM();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//    	System.out.println("HL="+HL);
    	}
        }
    }
    @FXML
    private void handleLongChanged(KeyEvent e){
        if(finishflag==true){
    	if(e.getCode()==KeyCode.ENTER){
        	try {
				UpdatePostion(Double.valueOf(longRadar.getText()).doubleValue(), latitude_center, coef);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		}
        }
    }
    @FXML
    private void handleLatChanged(KeyEvent e){
        if(finishflag==true){
    	if(e.getCode()==KeyCode.ENTER){
        	try {
				UpdatePostion(longitude_center, Double.valueOf(latRadar.getText()).doubleValue(), coef);
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    }
        }
    }
    @FXML
    private void handleHeightChanged(KeyEvent e){
        if(finishflag==true){
    	if(e.getCode()==KeyCode.ENTER){
    		try {
    			dynamicTiffReader=new DynamicTiffReader(longitude_center,latitude_center );
    			height_center=Double.valueOf(RadarHeight.getText()).doubleValue();
    			Console.appendText("Height_center"+height_center);
    			double tmp=dynamicTiffReader.GetHeight(longitude_center, latitude_center);
    			if(tmp<0)tmp=0;
    			height_real=height_center+tmp;
    			realRadarHeight.setText(String.valueOf(height_real));
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		try {
				show_DEM();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
        }
    }
    @FXML
    private void handleTilChanged(KeyEvent e){
        if(finishflag==true){
    	if(e.getCode()==KeyCode.ENTER){
    		Til=Double.valueOf(RadarTil.getText()).doubleValue();
    		try {
				show_DEM();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    }
    }
    }
}