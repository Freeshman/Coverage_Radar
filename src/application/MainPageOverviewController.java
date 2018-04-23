package application;

import cvrgController.CvrgController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.awt.Checkbox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

import Lambert.Projection;
import TiffReader.DynamicTiffReader;

public class MainPageOverviewController {
//	@FXML
//	 SplitPane Todo3,Todo4,Todo5;
//	@FXML
//	 VBox Todo2;
//	@FXML
//	 Checkbox Todo1;
	//=============== Todo list====================
	//
//	Todo1.setVisible(false);
//	Todo2.setVisible(false);
//	Todo3.setVisible(false);
//	Todo4.setVisible(false);
//	Todo5.setVisible(false);
	static int k=0;
	@FXML
	 ProgressIndicator progressMap;
	@FXML
	 ProgressBar progresBar;
	@FXML
	 Label realRadarHeight;
	@FXML
	 TextField longRadar;
	@FXML
	 TextField RadarHeight;
	@FXML
	 TextField RadarTil;
	@FXML
	 TextField FlightLevelInput;
	@FXML
	 TextField latRadar;
	static double HL;
	double angle, range, height_center, Til, height_real;
	double dist;
	@FXML
	 AnchorPane myAnchorPane;
	@FXML
	 TextField longPoint;
	@FXML
	 TextField latPoint;
	@FXML
	 TextArea Console;
	@FXML
	 CheckBox showobstacle;
	@FXML
	 BorderPane mainPage;
	@FXML Canvas imageView;
	//  ImageView imageView;

	@FXML
	 WebView mapWebView;
	@FXML
	 StackPane Key;
	// Reference to the main application.
	 MainApp mainApp;
	 Polyline[] polyline;
	 int polylineIndex;
	 Color[] colorBar;
	 double longitude_center;
	 double latitude_center;
	 double longitude1;
	 double latitude1;
	 double longitude2;
	 double latitude2;
	 double longitude3;
	 double latitude3;
	 double longitude4;
	 double latitude4;
	 double[][] P_lambert = new double[4][2];
	 static WritableImage img;
	 RadarParameters radarParameters;
	//  int[][] Pixel_map;
	 double coef;// 鍦板浘鏀惧ぇ绯绘暟
	 boolean finishflag, mapflag, CRflag;
	double Height;
	double Width;
	int windowWidth;
	int windowHeight;
	double coefx;
	double coefy;

	 Projection projectionLeft;
	 Projection projectionRight;

	DynamicTiffReader dynamicTiffReaderCR;
	DynamicTiffReader dynamicTiffReaderMapLeft;
	DynamicTiffReader dynamicTiffReaderMapRight;

	static CvrgController cvrgController = new CvrgController();

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	TopRadar topRadar = new TopRadar();
	public MainPageOverviewController() {
		HL = topRadar.HL;
		angle = topRadar.thetaAngle;
		range = topRadar.range;
		height_center = topRadar.height_center;
		Til = topRadar.til;
		dist = topRadar.dist;
		latitude_center = topRadar.latitudeC;
		longitude_center = topRadar.longitudeC;

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
	void handleExit() {
		System.exit(0);
	}
	public void setHL(double hL) {
		this.HL = hL;
		topRadar.HL = hL;
	}
}