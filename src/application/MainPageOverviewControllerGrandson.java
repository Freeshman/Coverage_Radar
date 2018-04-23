package application;

import java.io.IOException;

import Lambert.Projection;
import TiffReader.DynamicTiffReader;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class MainPageOverviewControllerGrandson extends MainPageOverviewControllerSon{
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 *
	 * @throws IOException
	 */
	@FXML
	 void initialize() throws IOException {
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


		} catch (IOException e) {
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
		coef = 700;
		Console.appendText("欢迎使用\nRSW-雷达覆盖范围分析软件\n");
		Console.appendText("Ines  Jules  Tom\n");
		Console.appendText("CAUC-SIAE\n");
		Console.appendText("2018-04-23\n");
		UpdatePosition(longitude_center, latitude_center, coef, height_center, 0);
		UpdatePosition();

	}
	void UpdatePosition(double Newlongitude, double Newlatitude, double Newcoef, double NewHeight,
			double NewTil, double NewRange) {
		if (finishflag == true) {
			if (NewRange != range) range = NewRange;
				UpdatePosition(Newlongitude, Newlatitude, Newcoef, NewHeight, NewTil);
				change(topRadar, radarParameters);
				UpdatePosition();
		}
	}

	 void UpdatePosition(double Newlongitude, double Newlatitude, double Newcoef, double NewHeight,
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
	// ˢ��ȫ�ֱ���
	 void UpdatePosition() {
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
				topRadar.heightC_real = height_real;
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
	@FXML
	public void handleScroll(ScrollEvent se) throws IOException {
		double deltaY = se.getDeltaY();
		double Newcoef = 0;
		if (deltaY > 0) {
			Newcoef = coef - 80;
			if (Newcoef < 800)
				Newcoef = 800;
		} else {
			Newcoef = coef + 80;
			if (Newcoef > 1200)
				Newcoef = 1200;
		}
		UpdatePosition(longitude_center, latitude_center, Newcoef, height_center, Til);
		UpdatePosition();
	}
	@FXML
	 void PickPosition(MouseEvent e) throws IOException {
		//�����ѡ��λ��
		if (finishflag == true) {
			MouseButton button = e.getButton();
			switch (button) {
			case PRIMARY:
				double NewPosition_Ly = coef * (e.getX() - windowWidth / 2.0);
				double NewPosition_Lx = -coef * (e.getY() - windowHeight / 2.0);
				double[] NewLB = projectionLeft.Lambert2WGS84(NewPosition_Lx, NewPosition_Ly);
				UpdatePosition(NewLB[0], NewLB[1], coef, height_center, Til);
				change(topRadar, radarParameters);
				UpdatePosition();
				break;
			case SECONDARY:// �Ҽ�
				break;
			}
		}

	}
	@FXML
	void handleConfiguration() {
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
	void handleNewLocation() {
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
	 void UpdateFL(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				HL = Double.valueOf(FlightLevelInput.getText()).doubleValue();
				setHL(HL);
				topRadar.HL = HL;
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
	 void handleLongChanged(KeyEvent e) {
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
	 void handleLatChanged(KeyEvent e) {
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
	 void handleHeightChanged(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				try {
					UpdatePosition(Double.valueOf(longRadar.getText()).doubleValue(),
							Double.valueOf(latRadar.getText()).doubleValue(), coef,
							Double.valueOf(RadarHeight.getText()).doubleValue(), Double.valueOf(RadarTil.getText()));
					UpdatePosition();
					double tmp = dynamicTiffReaderMapLeft.GetHeight(longitude_center, latitude_center);
					if (tmp < 0)
						tmp = 0;
					height_real = height_center + tmp;
					realRadarHeight.setText(String.valueOf(height_real));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}
	@FXML
	 void handleTilChanged(KeyEvent e) {
		if (finishflag == true) {
			if (e.getCode() == KeyCode.ENTER) {
				UpdatePosition(Double.valueOf(longRadar.getText()).doubleValue(),
						Double.valueOf(latRadar.getText()).doubleValue(), coef,
						Double.valueOf(RadarHeight.getText()).doubleValue(), Double.valueOf(RadarTil.getText()));
				UpdatePosition();
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
