package application;

import cvrgController.CvrgController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
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
//	private SplitPane Todo3,Todo4,Todo5;
//	@FXML
//	private VBox Todo2;
//	@FXML
//	private Checkbox Todo1;
	//=============== Todo list====================
	//
//	Todo1.setVisible(false);
//	Todo2.setVisible(false);
//	Todo3.setVisible(false);
//	Todo4.setVisible(false);
//	Todo5.setVisible(false);
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
	double HL, angle, range, height_center, Til, height_real;
	double dist = 50;
	@FXML
	private AnchorPane myAnchorPane;
	@FXML
	private TextField longPoint;
	@FXML
	private TextField latPoint;
	@FXML
	private TextArea Console;
	// @FXML
	// private VBox myBox;
	// @FXML
	// private Pane myPane;
	@FXML
	private BorderPane mainPage;
	@FXML
	private Canvas imageView;
	// private ImageView imageView;

	@FXML
	private WebView mapWebView;
	@FXML
	private StackPane Key;
	// Reference to the main application.
	private MainApp mainApp;
	private Polyline[] polyline;
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
	private double[] dragPoints;
	private static WritableImage img;
	private RadarParameters radarParameters;
	// private int[][] Pixel_map;
	private double coef;// 鍦板浘鏀惧ぇ绯绘暟
	private boolean finishflag, mapflag, CRflag;
	/*
	 * /\ xL 0--------------------> y | | 1-------------2 | | | | | |
	 * 4-------------3 | V ----------------------------|-------------> yL x
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
	private Projection projectionLeft;
	private Projection projectionRight;

	DynamicTiffReader dynamicTiffReaderCR;
	private DynamicTiffReader dynamicTiffReaderMapLeft;
	private DynamicTiffReader dynamicTiffReaderMapRight;

	CvrgController cvrgController = new CvrgController();
	// CvrgController cvrgController;

	// private projectionHelper projection;
	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public MainPageOverviewController() {

		HL = 2000;
		angle = 0.72;
		range = 250000;
		height_center = 0;
		xv = 0;
		yv = 0;
		wv = 0;
		hv = 0;
		Til = 10;
		// =====================
		// Here Til=10 then set NewTil = 0 is to update the map at
		// startup
		// ======================
		// latitude_center=39.1097618747;
		// longitude_center=117.3529134562;
		latitude_center = 38.058942;
		longitude_center = 106.2028889;
		P_lambert = new double[4][2];
		dragPoints = new double[2];

	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 *
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {
		radarParameters = new RadarParameters(longitude_center, latitude_center, range, height_center, Til, "");

//		progresBar.setVisible(false);
//		progressMap.setVisible(false);
		FlightLevelInput.setText(String.valueOf(HL));
		RadarHeight.setText(String.valueOf(height_center));
		RadarTil.setText(String.valueOf(Til));
		longRadar.setText(String.valueOf(longitude_center));
		latRadar.setText(String.valueOf(latitude_center));
		projectionRight = new Projection(longitude_center, latitude_center);
		projectionLeft = new Projection(longitude_center, latitude_center);
		polyline = new Polyline[2];
		polylineIndex = 0;
		colorBar = new Color[5];
		colorBar[0] = new Color(0, 0, 1, 1);
		colorBar[1] = new Color(0, 1, 1, 1);
		colorBar[2] = new Color(0, 0, 1, 1);
		colorBar[3] = new Color(0.5, 0.5, 1, 1);
		colorBar[4] = new Color(0, 0, 0, 1);
		try {
			dynamicTiffReaderMapLeft = new DynamicTiffReader(longitude_center, latitude_center);
			dynamicTiffReaderMapRight = new DynamicTiffReader(longitude_center, latitude_center);
			// dynamicTiffReaderCR=new
			// DynamicTiffReader(longitude_center,latitude_center );

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double tmp = dynamicTiffReaderMapLeft.GetHeight(longitude_center, latitude_center);
		if (tmp < 0)
			tmp = 0;
		height_real = height_center + tmp;
		finishflag = true;
		mapflag = true;
		CRflag = true;
		windowHeight = 800;
		windowWidth = 800;
		img = new WritableImage(windowHeight, windowWidth);
		imageView.setHeight(windowHeight);
		imageView.setWidth(windowWidth);
		coefx = windowWidth / imageView.getWidth();
		coefy = windowHeight / imageView.getHeight();
		coef = 500;
		Console.appendText("欢迎使用!\nRSW-雷达覆盖范围分析软件!\n");
		UpdatePosition(longitude_center, latitude_center, coef, height_center, 0);
		UpdatePosition();

	}

	private void UpdatePosition(double Newlongitude, double Newlatitude, double Newcoef, double NewHeight,
			double NewTil, double NewRange) {
		if (finishflag == true) {
			if (NewRange != range) range = NewRange;
				UpdatePosition(Newlongitude, Newlatitude, Newcoef, NewHeight, NewTil);
				UpdatePosition();

		}
	}

	private void UpdatePosition(double Newlongitude, double Newlatitude, double Newcoef, double NewHeight,
			double NewTil) {
		if (finishflag == true)
			if (Newcoef != coef | Newlongitude != longitude_center | Newlatitude != latitude_center
					| NewHeight != height_center | NewTil != Til) {
				longitude_center = Newlongitude;
				latitude_center = Newlatitude;
				height_center = NewHeight;
				Til = NewTil;
				radarParameters.SetL(longitude_center);
				radarParameters.SetB(latitude_center);
				radarParameters.SetH(height_center);
				radarParameters.SetMaxRange(range);
				radarParameters.SetTil(Til);
				coef = Newcoef;
			}
	}

	// 刷新全局变量
	private void UpdatePosition() {
		if (finishflag == true) {
			longRadar.setText(String.valueOf(longitude_center));
			latRadar.setText(String.valueOf(latitude_center));
			RadarHeight.setText(String.valueOf(height_center));
			RadarTil.setText(String.valueOf(Til));
			projectionLeft = new Projection(longitude_center, latitude_center);
			projectionRight = new Projection(longitude_center, latitude_center);
			double[] lonlat_center = new double[2];
			lonlat_center[0] = longitude_center;
			lonlat_center[1] = latitude_center;
			double[] result0 = projectionLeft.WGS842Lambert(lonlat_center);
			P_lambert[0][0] = result0[0] + coef * windowHeight / 2.0;// x
			P_lambert[0][1] = result0[1] - coef * windowWidth / 2.0;// y
			P_lambert[1][0] = result0[0] + coef * windowHeight / 2.0;// x
			P_lambert[1][1] = result0[1] + coef * windowWidth / 2.0;// y
			P_lambert[2][0] = result0[0] - coef * windowHeight / 2.0;// x
			P_lambert[2][1] = result0[1] + coef * windowWidth / 2.0;// y
			P_lambert[3][0] = result0[0] - coef * windowHeight / 2.0;// x
			P_lambert[3][1] = result0[1] - coef * windowWidth / 2.0;// y
			double[] tmp = projectionLeft.Lambert2WGS84(P_lambert[0][0], P_lambert[0][1]);
			latitude1 = tmp[1];
			longitude1 = tmp[0];
			tmp = projectionLeft.Lambert2WGS84(P_lambert[1][0], P_lambert[1][1]);
			latitude2 = tmp[1];
			longitude2 = tmp[0];
			tmp = projectionLeft.Lambert2WGS84(P_lambert[2][0], P_lambert[2][1]);
			latitude3 = tmp[1];
			longitude3 = tmp[0];
			tmp = projectionLeft.Lambert2WGS84(P_lambert[3][0], P_lambert[3][1]);
			latitude4 = tmp[1];
			longitude4 = tmp[0];
			try {
				double tmp_ = dynamicTiffReaderMapLeft.GetHeight(longitude_center, latitude_center);
				if (tmp_ < 0)
					tmp_ = 0;
				height_real = height_center + tmp_;
				realRadarHeight.setText(String.valueOf(height_real));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				show_DEM();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

		double deltaY = se.getDeltaY();
		double Newcoef = 0;
		if (deltaY > 0) {

			Newcoef = coef - 80;
			if (Newcoef < 100)
				Newcoef = 100;
		} else {

			Newcoef = coef + 80;
			if (Newcoef > 1200)
				Newcoef = 1200;
		}
		UpdatePosition(longitude_center, latitude_center, Newcoef, height_center, Til);
		UpdatePosition();
	}

	@FXML
	private void PickPosition(MouseEvent e) throws IOException {

		if (finishflag == true) {
			MouseButton button = e.getButton();
			switch (button) {
			case PRIMARY:
				double NewPosition_Ly = coef * (e.getX() - windowWidth / 2.0);
				double NewPosition_Lx = -coef * (e.getY() - windowHeight / 2.0);
//				longPoint.setText(String.valueOf(NewPosition_Ly));
//				latPoint.setText(String.valueOf(NewPosition_Lx));
				double[] NewLB = projectionLeft.Lambert2WGS84(NewPosition_Lx, NewPosition_Ly);
				UpdatePosition(NewLB[0], NewLB[1], coef, height_center, Til);
				UpdatePosition();
				break;
			case SECONDARY:// 右键
				break;
			}
		}

	}

	@FXML
	private void show_DEM() throws IOException {
		finishflag = false;
		CRflag = false;
		mapflag = false;
		Range();
		Task<Void> taskLeft = new Task<Void>() {
			private int col, row;
			private int Height;
			private Color color;

			@Override
			protected Void call() throws Exception {
//				progressMap.setVisible(true);
				PixelWriter pw = img.getPixelWriter();
				for (row = (int) P_lambert[0][1]; row != (int) P_lambert[1][1]; row += 2 * coef) {// x
					for (col = (int) P_lambert[0][0]; col != (int) P_lambert[3][0]; col -= coef) {// y
						double[] result = projectionLeft.Lambert2WGS84(-row, -col);
						try {
							Height = (int) dynamicTiffReaderMapLeft.GetHeight(result[0], result[1]);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						color = ColorMap(Height, 950);
						if (Math.abs((row / coef * row / coef + col / coef * col / coef) - 50) < 10) {
							if (color.getBrightness() > 0.5) {
								if (color.getBlue() > 0.5)
									color = new Color(1, 1, 1, 1);
								else
									color = new Color(0, 0, 0, 1);
							} else if (color.getBrightness() < 0.5)
								color = new Color(1, 1, 1, 1);
						}
						int i = (int) ((row - (int) P_lambert[0][1]) / coef);
						int j = -(int) ((col - (int) P_lambert[0][0]) / coef);
						updateProgress(i + 1, windowHeight);
						pw.setColor(j, i, color);
					}

				}
				Platform.runLater(() -> {
					imageView.getGraphicsContext2D().drawImage(img, 0, 0);
				});
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
//				progressMap.setVisible(false);
				mapflag = true;
				if (CRflag == true)
					finishflag = true;
			}
		};

		Task<Void> taskRight = new Task<Void>() {
			private int col, row;
			private int Height;
			private Color color;

			@Override
			protected Void call() throws Exception {
				PixelWriter pw = img.getPixelWriter();
				for (row = (int) (P_lambert[0][1] + coef); row != (int) P_lambert[1][1] - coef; row += 2 * coef) {// x
					for (col = (int) P_lambert[0][0]; col != (int) P_lambert[3][0]; col -= coef) {// y
						double[] result = projectionRight.Lambert2WGS84(-row, -col);
						try {
							Height = (int) dynamicTiffReaderMapRight.GetHeight(result[0], result[1]);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						color = ColorMap(Height, 950);
						if (Math.abs((row / coef * row / coef + col / coef * col / coef) - 50) < 10) {
							if (color.getBrightness() > 0.5) {
								if (color.getBlue() > 0.5)
									color = new Color(1, 1, 1, 1);
								else
									color = new Color(0, 0, 0, 1);
							} else if (color.getBrightness() < 0.5)
								color = new Color(1, 1, 1, 1);
						}
						int i = (int) ((row - (int) P_lambert[0][1]) / coef);
						int j = -(int) ((col - (int) P_lambert[0][0]) / coef);
						updateProgress(i + 1, windowHeight);
						pw.setColor(j, i, color);
					}

				}
				Platform.runLater(() -> {

					imageView.getGraphicsContext2D().drawImage(img, 0, 0);
				});
				return null;
			}

			@Override
			protected void succeeded() {
				super.succeeded();
			}
		};
		Thread thread = new Thread(taskLeft);
		new Thread(taskRight).start();
		progressMap.progressProperty().bind(taskLeft.progressProperty());
		thread.start();
	}

	private void Range() {
		Task<Void> progressTask = new Task<Void>() {
			private Double[] points;
			@Override
			protected void succeeded() {
				super.succeeded();
				GraphicsContext graphicsContext = imageView.getGraphicsContext2D();
				graphicsContext.setStroke(new Color(0.3, 0, 1, 1));
				graphicsContext.setLineWidth(1);
				graphicsContext.beginPath();
				for (int i = 0; i < points.length; i += 2) {
					graphicsContext.lineTo(points[i].doubleValue(), points[i + 1].doubleValue());
				}
				graphicsContext.stroke();
				CRflag = true;
				if (mapflag == true) {
					finishflag = true;
				}
			}
			@Override
			protected Void call() throws Exception {
				updateProgress(0, 100);
//				progresBar.setVisible(true);
				points = new Double[2 * (int) (360.0 / angle) + 2];

//				long begin = System.currentTimeMillis();
//				System.out.println(dynamicTiffReaderCR + "\t" + latitude_center + "\t" + longitude_center + "\t"
//						+ height_real + "\t" + angle + "\t" + dist + "\t" + range + "\t" + HL);

				ForkJoinPool forkJoinPool = new ForkJoinPool();
				MainPageOverviewController mainPageOverviewController = new MainPageOverviewController();
//				System.out.println("projectionLeft" + projectionLeft);
				updateProgress(10, 100);
				CountTask task = mainPageOverviewController.new CountTask(1, (int) (360.0 / angle), projectionLeft,
						dynamicTiffReaderCR, longitude_center, latitude_center, height_real);
//				long beginTime = System.currentTimeMillis();
				Future<List<double[]>> result = forkJoinPool.submit(task);
				updateProgress(50, 100);
//				try {
//					System.out.println("The sum from 1 to 2000000 is " + result.get().get(0)[0]);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				List<double[]> LambertArray = new ArrayList<double[]>();

				LambertArray = result.get();
				updateProgress(70, 100);
//				System.out.println(LambertArray.get(0)[1]);
//				System.out.println("Time consumed(nano second) By ForkJoin algorithm : "
//						+ (System.currentTimeMillis() - beginTime));
				// LambertArray.parallelStream().forEach(e ->{
				// try {
				// Thread.sleep(1);
				// } catch (InterruptedException f) {
				// f.printStackTrace();
				// }
				// });

//				long end = System.currentTimeMillis();
//				System.out.println("耗时 " + (end - begin));
				updateProgress(90, 100);
				for (int k = 0; k < (int) 360.0 / angle; k++) {

					int i = (int) ((LambertArray.get(k)[1] - (int) P_lambert[0][1]) / coef);
					int j = -(int) ((LambertArray.get(k)[0] - (int) P_lambert[0][0]) / coef);
					points[2 * k] = Double.valueOf(i);
					points[2 * k + 1] = Double.valueOf(j);
					if (k != 0) {
						if (i < 0 | i > windowHeight)
							points[2 * k] = points[2 * (k - 1)];
						if (j < 0 | j > windowWidth)
							points[2 * k + 1] = points[2 * (k - 1) + 1];
					}

				}

				points[2 * (int) (360.0 / angle)] = points[0];
				points[2 * (int) (360.0 / angle) + 1] = points[1];
				updateProgress(100, 100);
				// System.out.println("Finish");
				return null;
			}
		};
		progresBar.progressProperty().bind(progressTask.progressProperty());
		new Thread(progressTask).start();

	}

	private Color ColorMap(double d, float average) {
		float r, g, b;
		float Max = 2 * average;
		float slop = 1 / (average / 2);
		if (d <= 0) {
			r = 0;
			g = 0;
			b = 0;
		} else if (d < Max / 4) {
			b = (float) (slop * d);
			g = 0;
			r = 0;
		} else if (d < average) {
			b = (float) (-slop * (d - average / 2) + 1);
			g = (float) (slop * (d - Max / 4));
			r = 0;
		} else if (d < 3 * Max / 4) {
			b = 0;
			g = 1;
			r = (float) (slop * (d - average));
		} else if (d < Max) {
			b = 0;
			g = (float) (-slop * (d - 3 * Max / 4) + 1);
			r = 1;
		} else if (d >= Max) {
			b = 0;
			g = 0;
			r = 1;
		} else {
			r = g = b = 0;
		}
		return new Color(r, g, b, 1);
	}

	@FXML
	private void handleConfiguration() {
		boolean okClicked;
		okClicked = mainApp.showConfigurationDialog(radarParameters);
		if (okClicked == true) {
			longRadar.setText(String.valueOf(longitude_center));
			latRadar.setText(String.valueOf(latitude_center));
			RadarHeight.setText(String.valueOf(height_center));
			RadarTil.setText(String.valueOf(Til));
			UpdatePosition(radarParameters.GetL(), radarParameters.GetB(), coef, radarParameters.GetH(),
					radarParameters.GetTil(), radarParameters.GetMaxRange());
			UpdatePosition();
		}
	}

	@FXML
	private void handleNewLocation() {
		try {
			UpdatePosition(Double.valueOf(longRadar.getText()).doubleValue(),
					Double.valueOf(latRadar.getText()).doubleValue(), coef, Double.valueOf(RadarHeight.getText()),
					Double.valueOf(RadarTil.getText()));
			UpdatePosition();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

	@FXML
	private void UpdateFL(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				HL = Double.valueOf(FlightLevelInput.getText()).doubleValue();
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
	private void handleLongChanged(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					UpdatePosition(Double.valueOf(longRadar.getText()).doubleValue(),
							Double.valueOf(latRadar.getText()).doubleValue(), coef,
							Double.valueOf(RadarHeight.getText()), Double.valueOf(RadarTil.getText()));
					UpdatePosition();
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@FXML
	private void handleLatChanged(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					UpdatePosition(Double.valueOf(longRadar.getText()).doubleValue(),
							Double.valueOf(latRadar.getText()).doubleValue(), coef,
							Double.valueOf(RadarHeight.getText()), Double.valueOf(RadarTil.getText()));
					UpdatePosition();
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@FXML
	private void handleHeightChanged(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					height_center = Double.valueOf(RadarHeight.getText()).doubleValue();
					Console.appendText("Height_center" + height_center);
					double tmp = dynamicTiffReaderMapLeft.GetHeight(longitude_center, latitude_center);
					if (tmp < 0)
						tmp = 0;
					height_real = height_center + tmp;
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
	private void handleTilChanged(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				Til = Double.valueOf(RadarTil.getText()).doubleValue();
				try {
					show_DEM();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private List<double[]> sum1(long start, long end, Projection projectionLeft, DynamicTiffReader dynamicTiffReaderCR,
			double longitude_center, double latitude_center, double height_real) {
		List<double[]> Array = new ArrayList<>();
		DynamicTiffReader dynamicTiffReader1;
		try {
			dynamicTiffReader1 = new DynamicTiffReader(longitude_center, latitude_center);
			for (long i = start; i <= end; i++) {
				double[] d = cvrgController.start(dynamicTiffReader1, latitude_center, longitude_center, height_real,
						angle * i, dist, range, HL).get(0);
				double[] ds = projectionLeft.WGS842Lambert(d);
				double[] dss = new double[3];
				dss[0] = ds[0];
				dss[1] = ds[1];
				dss[2] = d[2];
				Array.add(dss);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Array;
	}
	private class CountTask extends RecursiveTask<List<double[]>> {
		private static final int THRESHOLD = 500 / 4 + 1;
		private int start;
		private int end;
		private DynamicTiffReader dynamicTiffReader;
		private double real_hight;
		private Projection projectionLeft;
		private double latitude_center, longitude_center;

		public CountTask(int start, int end, Projection projectionLeft, DynamicTiffReader dynamicTiffReader,
				double longitude_center, double latitude_center, double real_hight) {
			this.start = start;
			this.end = end;
			this.dynamicTiffReader = dynamicTiffReader;
			this.real_hight = real_hight;
			this.projectionLeft = projectionLeft;
			this.longitude_center = longitude_center;
			this.latitude_center = latitude_center;
		}

		protected List<double[]> compute() {
			// System.out.println("Thread ID: " +
			// Thread.currentThread().getId());
			List<double[]> Array = new ArrayList<>();

			if ((end - start) <= THRESHOLD) {
				Array = sum1(start, end, projectionLeft, dynamicTiffReader, longitude_center, latitude_center,
						real_hight);
			} else {
				int middle = (start + end) / 2;
				CountTask leftTask = new CountTask(start, middle, projectionLeft, dynamicTiffReader, longitude_center,
						latitude_center, real_hight);
				CountTask rightTask = new CountTask(middle + 1, end, projectionLeft, dynamicTiffReader,
						longitude_center, latitude_center, real_hight);
				leftTask.fork();
				rightTask.fork();

				List<double[]> leftResult = leftTask.join();
				List<double[]> rightResult = rightTask.join();
				leftResult.addAll(rightResult);
				Array = leftResult;
			}

			return Array;
		}
	}

}