package application;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class MainPageOverviewControllerSon extends MainPageOverviewController {
	private Double[] points;
	private Double[] points2;
	private Double[] points3;

	protected void Range() {
		Task<Void> progressTask = new Task<Void>() {
			@Override
			protected void succeeded() {
				super.succeeded();
				//单独CR线程监控地图绘制，地图绘制时，线程睡眠0.5s直到地图绘制完毕
				Thread CR = new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (mapflag == false)
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						GraphicsContext graphicsContext = imageView.getGraphicsContext2D();
						graphicsContext.setStroke(new Color(0.3, 0, 1, 1));
						graphicsContext.setLineWidth(2);
						graphicsContext.beginPath();
						graphicsContext.moveTo(points[0], points[1]);
						for (int i = 0; i < points.length; i += 2) {
							graphicsContext.lineTo(points[i].doubleValue(), points[i + 1].doubleValue());
						}
						graphicsContext.stroke();
						if (showobstacle.isSelected()) {

							graphicsContext.setStroke(new Color(0, 0, 0, 1));
							graphicsContext.beginPath();
							graphicsContext.moveTo(points2[0], points2[1]);
							for (int i = 0; i < points2.length; i += 2) {
								graphicsContext.lineTo(points2[i].doubleValue(), points2[i + 1].doubleValue());
							}
							graphicsContext.stroke();
						}
						graphicsContext.setStroke(new Color(1, 1, 1, 1));
						graphicsContext.beginPath();
						graphicsContext.moveTo(points3[0], points3[1]);
						for (int i = 0; i < points3.length; i += 2) {
							graphicsContext.lineTo(points3[i].doubleValue(), points3[i + 1].doubleValue());
						}
						graphicsContext.stroke();
						CRflag = true;
						finishflag = true;
					}
				});
			CR.start();
			}
			Boolean myCase = true;
			@Override
			protected Void call() throws Exception {
				topRadar.iter = 0;//全局静态变量iter刷新计算进度
				Thread thread = new Thread(new Runnable() {
					public void run() {
						while (myCase) {
							k = topRadar.iter;
							updateProgress(k, 360);
							if (k >= 360)
								myCase = false;
						}
					}
				});
				thread.start();
				points = new Double[2 * (int) (360.0 / angle) + 2];
				points2 = new Double[2 * (int) (360.0 / angle) + 2];
				points3 = new Double[2 * (int) (360.0 / angle) + 2];
				HL = Double.valueOf(FlightLevelInput.getText()).doubleValue();
				topRadar.HL = HL;
				change(topRadar, radarParameters);
				topRadar.calculateAllRay();
				double[][] resultArray = topRadar.array;
				for (int k = 0; k < (int) 360.0 / angle; k++) {
					double[] LambertArray = projectionLeft.WGS842Lambert(resultArray[k]);
					int i = (int) ((LambertArray[1] - (int) P_lambert[0][1]) / coef);
					int j = -(int) ((LambertArray[0] - (int) P_lambert[0][0]) / coef);
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

				if (showobstacle.isSelected()) {
					double[][] resultSheildArray = topRadar.sheildArray;
					for (int k = 0; k < (int) 360.0 / angle; k++) {
						double[] LambertArray = projectionLeft.WGS842Lambert(resultSheildArray[k]);
						int i = (int) ((LambertArray[1] - (int) P_lambert[0][1]) / coef);
						int j = -(int) ((LambertArray[0] - (int) P_lambert[0][0]) / coef);
						if (i < 0) {
							i = 0;
						} else if (i > windowHeight) {
							i = windowHeight;
						}
						if (j < 0) {
							j = 0;
						} else if (j > windowWidth) {
							j = windowWidth;
						}
						points2[2 * k] = Double.valueOf(i);
						points2[2 * k + 1] = Double.valueOf(j);
					}

					points2[2 * (int) (360.0 / angle)] = points2[0];
					points2[2 * (int) (360.0 / angle) + 1] = points2[1];
				}
				double[][] resultViewArray = topRadar.viewArray;
				for (int k = 0; k < (int) 360.0 / angle; k++) {
					double[] LambertArray = projectionLeft.WGS842Lambert(resultViewArray[k]);
					int i = (int) ((LambertArray[1] - (int) P_lambert[0][1]) / coef);
					int j = -(int) ((LambertArray[0] - (int) P_lambert[0][0]) / coef);
					if (i < 0) {
						i = 0;
					} else if (i > windowHeight) {
						i = windowHeight;
					}
					if (j < 0) {
						j = 0;
					} else if (j > windowWidth) {
						j = windowWidth;
					}
					points3[2 * k] = Double.valueOf(i);
					points3[2 * k + 1] = Double.valueOf(j);

				}

				points3[2 * (int) (360.0 / angle)] = points3[0];
				points3[2 * (int) (360.0 / angle) + 1] = points3[1];

				return null;
			}
		};
		progresBar.progressProperty().bind(progressTask.progressProperty());
		new Thread(progressTask).start();
	}

	void show_DEM() throws IOException {
		finishflag = false;
		CRflag = false;
		mapflag = false;
		Range();
		Task<Void> taskLeft = new Task<Void>() {
			int col, row;
			int Height;
			Color color;
			@Override
			protected Void call() throws Exception {
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
				mapflag = true;
				if (CRflag == true)
					finishflag = true;//finishflag决定用户是否能够进行新的操作
			}
		};

		Task<Void> taskRight = new Task<Void>() {
			int col, row;
			int Height;
			Color color;

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
		};

		Thread thread = new Thread(taskLeft);
		new Thread(taskRight).start();
		progressMap.progressProperty().bind(taskLeft.progressProperty());
		thread.start();
	}

	Color ColorMap(double d, float average) {
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
//武瑞和胡刚雷达参数互换接口
	public void change(TopRadar topRadar, RadarParameters radarParameters) {
		topRadar.latitudeC = radarParameters.GetB();
		topRadar.longitudeC = radarParameters.GetL();
		topRadar.height_center = radarParameters.GetH();
		topRadar.range = radarParameters.GetMaxRange();
	}
}
