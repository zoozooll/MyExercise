/**
 * 
 */
package com.idt.bw.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.ChartStyle;
import com.shinobicontrols.charts.ColumnSeries;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.DateFrequency;
import com.shinobicontrols.charts.DateFrequency.Denomination;
import com.shinobicontrols.charts.DateRange;
import com.shinobicontrols.charts.DateTimeAxis;
import com.shinobicontrols.charts.LineSeries;
import com.shinobicontrols.charts.LineSeriesStyle;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.NumberRange;
import com.shinobicontrols.charts.PointStyle;
import com.shinobicontrols.charts.Range;
import com.shinobicontrols.charts.Series;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.ShinobiChart.OnAxisRangeChangeListener;
import com.shinobicontrols.charts.ShinobiChart.OnSeriesSelectionListener;
import com.shinobicontrols.charts.SimpleDataAdapter;
import com.shinobicontrols.charts.Series.SelectionMode;
import com.shinobicontrols.charts.SeriesStyle.FillStyle;
import com.shinobicontrols.charts.Title.CentersOn;
import com.shinobicontrols.charts.Title.Position;

/**
 * @author aaronli
 * 
 */
public class BWChartFragment extends ChartFragment implements OnSeriesSelectionListener, ShinobiChart.OnSnapshotDoneListener, ShinobiChart.OnAxisRangeChangeListener {
	
	private static final String TAG = "BWChartFragment";
	
	private static final DateFormat DATEFORMAT_DD = new SimpleDateFormat("dd");
	private static final DateFormat DATEFORMAT_MM = new SimpleDateFormat("MMM");
	private static final DateFormat DATEFORMAT_YY = new SimpleDateFormat("yyyy");
	private static final String TEMP_FILE_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/.android/bw/data";
	public static final int CATEGORY_WEIGHT = 1;
	public static final int CATEGORY_HEIGHT = 2;
	public static final int CATEGORY_BMI = 4;
	
	public static final int PER_DAILY = 0;
	public static final int PER_WEEKLY = 1;
	public static final int PER_MONTHLY = 2;
	public static final int PER_YEARLY = 3;
	
	private int chartCategory;
	private ShinobiChart shinobiChart;
	private LineSeries datavalueSeries;
	private LineSeries targetLineSeries;
	private LineSeries referenceLineSeries;
	private LineSeries markerLineSeries1;
	private LineSeries markerLineSeries2;
	private LineSeries markerLineSeries3;
	private LineSeries markerLineSeries4;
	private DataAdapter<Date, Float> dataAdapter ;
	private DataAdapter<Double, Double> targetAdapter ;
	private DataAdapter<Date, Float> referenceAdapter ;
	private DataAdapter<Double, Float> markerAdapter1 ;
	private DataAdapter<Double, Float> markerAdapter2 ;
	private DataAdapter<Double, Float> markerAdapter3 ;
	private DataAdapter<Double, Float> markerAdapter4 ;
	private DateTimeAxis xAxis;
	private NumberAxis displayAxis;
	private NumberAxis yAxis;
	private TextView popupView;
	protected ViewGroup mRootView;
	private PopupWindow mPopupWindow;
	private Handler handler;
	private String unit;
	
	private OnSelectItemChangedEvent listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the a reference to the ShinobiChart
		shinobiChart = getShinobiChart();

		// TODO: replace <license_key_here> with you trial license key
		shinobiChart.setLicenseKey("<license_key_here>");
		ChartStyle chartStyle = new ChartStyle();
		int backgroundColor = Color.argb(255, 231, 231, 231);
		chartStyle.setBackgroundColor(backgroundColor);
		chartStyle.setCanvasBackgroundColor(backgroundColor);
		chartStyle.setPlotAreaBackgroundColor(backgroundColor);
		shinobiChart.getTitleStyle().setPosition(Position.BOTTOM_OR_LEFT);
		shinobiChart.setStyle(chartStyle);

		// Set the title
		//shinobiChart.setTitle("Custom Data Adapter");

		// Create X and Y axes and add to the chart
		xAxis = new DateTimeAxis();
		xAxis.enableGesturePanning(true);
		xAxis.enableGestureZooming(true);
		xAxis.enableMomentumPanning(true);
		xAxis.enableMomentumZooming(true);
		xAxis.enableBouncingAtLimits(false);
		xAxis.allowPanningOutOfMaxRange(true);
		xAxis.getStyle().getTickStyle().setMajorTicksShown (false);
		xAxis.getStyle().getTickStyle().setMinorTicksShown (false);
//		xAxis.getStyle().setInterSeriesPadding(.5f);
		shinobiChart.setXAxis(xAxis);
		displayAxis = new NumberAxis(new NumberRange(0., 80.));
		displayAxis.getStyle().getTickStyle().setLabelsShown(false);
		displayAxis.getStyle().getTickStyle().setMajorTicksShown (false);
		displayAxis.getStyle().getTickStyle().setMinorTicksShown (false);
		yAxis = new NumberAxis();
		yAxis.enableGesturePanning(true);
		yAxis.enableGestureZooming(true);
		yAxis.enableMomentumPanning(true);
		yAxis.enableMomentumZooming(true);
		yAxis.getStyle().getTickStyle().setMajorTicksShown(false);
		yAxis.getStyle().getTickStyle().setMinorTicksShown(false);
		yAxis.getStyle().getGridlineStyle().setGridlinesShown(true);
		shinobiChart.setYAxis(yAxis);

		// Create our custom DataAdapter
		dataAdapter = new SimpleDataAdapter<Date, Float>();
		// Create a ColumnSeries and give it the data adapter
		datavalueSeries = new LineSeries();
		LineSeriesStyle lineStyle = datavalueSeries.getStyle();
		lineStyle.setLineColor(Color.GRAY);
		lineStyle.setLineColorBelowBaseline(Color.GRAY);
		lineStyle.setLineWidth(3.f);
		PointStyle pointStyle = new PointStyle();
		pointStyle.setInnerRadius(0);
		pointStyle.setPointsShown(true);
		pointStyle.setColor(Color.GRAY);
		pointStyle.setRadius(4f);
		lineStyle.setPointStyle(pointStyle);
		lineStyle.setSelectedPointStyle(pointStyle);
		datavalueSeries.setSelectionMode(SelectionMode.POINT_SINGLE);
		datavalueSeries.setDataAdapter(dataAdapter);
		shinobiChart.addSeries(datavalueSeries, xAxis, yAxis);
		shinobiChart.setOnSeriesSelectionListener(this);
		shinobiChart.setOnSnapshotDoneListener(this);
		shinobiChart.setOnAxisRangeChangeListener(this);
		handler = new Handler();
		setShowingScale(new Date(), PER_WEEKLY);
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		handler.removeCallbacksAndMessages(null);
		if (mPopupWindow!= null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
		super.onStop();
	}

	/**
	 * 
	 * @param category can choose {@link CATEGORY_WEIGHT} {@link CATEGORY_HEIGHT} {@link CATEGORY_BMI} 
	 * @param weightUnit      
	 * @param heightUnit
	 */
	public void setChartCategoryChanged(int category, String weightUnit,
			String heightUnit) {
		if (category == 7) {
			category = this.chartCategory;
		}
		
		switch (category) {
		case CATEGORY_WEIGHT:
			if ("lb".equals(weightUnit)) {
				shinobiChart.setTitle (getResources().getString(R.string.chart_range_weightcategory_lbs));
				unit = getResources().getString(R.string.weight_value_lbs);
				//yAxis.setDefaultRange(new NumberRange(0., 330.6933933));
			} else {
				shinobiChart.setTitle (getResources().getString(R.string.chart_range_weightcategory));
				unit = getResources().getString(R.string.create_weight_kg);
				//yAxis.setDefaultRange(new NumberRange(0., 130.));
			}
			//setShowingTargetLine(true);
			break;
		case CATEGORY_HEIGHT:
			if ("ft".equals(heightUnit)) {
				shinobiChart.setTitle (getResources().getString(R.string.chart_range_heightcategory_ft));
				unit = getResources().getString(R.string.create_height_ft);
				//yAxis.setDefaultRange(new NumberRange(0., 9.1863517));
			} else {
				shinobiChart.setTitle (getResources().getString(R.string.chart_range_heightcategory));
				unit = getResources().getString(R.string.create_height_cm);
				//yAxis.setDefaultRange(new NumberRange(0., 280.));
			}
			//setShowingTargetLine(false);
			break;
		case CATEGORY_BMI:
			shinobiChart.setTitle (getResources().getString(R.string.chart_range_bmicategory));
			unit = "";
			//setShowingTargetLine(false);
			//yAxis.setDefaultRange(new NumberRange(0., 40.));
			break;

		default:
			break;
		}
    	this.chartCategory = category;
	}
	
	public void setCharHighestValue(double value) {
		yAxis.setDefaultRange(new NumberRange(0., value * 2.));
		yAxis.requestCurrentDisplayedRange(0.0, value * 2.0, false, false);
	}

	/**
	 * If need to showing reference line.
	 * @param targetLine true, when need to show target line, and create the series of target values.
	 * 		false, when it is no need to show target line, and remove the series.
	 */
	public void setShowingTargetLine(boolean targetLine) {
		if (targetLine) {
			if (targetLineSeries == null) {
				targetAdapter = new SimpleDataAdapter<Double, Double>();
				targetLineSeries = new LineSeries();
				PointStyle pointStyle = new PointStyle();
				pointStyle.setInnerRadius(0);
				pointStyle.setPointsShown(true);
				pointStyle.setColor(Color.GRAY);
				pointStyle.setRadius(1f);
				targetLineSeries.getStyle().setPointStyle(pointStyle);
				targetLineSeries.getStyle().setLineShown(false);
				targetLineSeries.setDataAdapter(targetAdapter);
			}
			
			shinobiChart.addSeries(targetLineSeries, displayAxis, yAxis);
			shinobiChart.redrawChart();
		} else {
			if (referenceLineSeries != null)
				shinobiChart.removeSeries(targetLineSeries);
		}

	}

	/**
	 * If need to showing reference line.
	 * @param referenceLine true, when need to show reference line, and create the series of reference values.
	 * 		false, when it is no need to show reference line, and remove the series.
	 */
	public void setShowingReferenceLine(boolean referenceLine) {
		if (referenceLine) {
			if (referenceLineSeries == null) {
				referenceAdapter = new SimpleDataAdapter<Date, Float>();
				referenceLineSeries = new LineSeries();
				LineSeriesStyle style1 = referenceLineSeries.getStyle();
	            style1.setFillStyle(FillStyle.GRADIENT);
	            style1.setLineColor(Color.GRAY);
	            style1.setLineColorBelowBaseline(Color.argb(50,  0,  0,  0));
	            style1.setAreaColor(Color.argb(50,  0,  0,  0));
	            style1. setAreaLineColor(Color.argb(50,  0,  0,  0));
	            style1.setAreaColorGradient(Color.argb(50,  0,  0,  0));
	            style1.setAreaColorBelowBaseline(Color.argb(50,  0,  0,  0));
	            style1.setAreaColorGradientBelowBaseline(Color.argb(50,  0,  0,  0));
	            referenceLineSeries.setDataAdapter(referenceAdapter);
			}
			shinobiChart.addSeries(referenceLineSeries, xAxis, yAxis);
			shinobiChart.redrawChart();
		} else {
			if (referenceLineSeries != null)
				shinobiChart.removeSeries(referenceLineSeries);
		}
	}
	
	public void setShowingMarkerLines(boolean showing) {
		if (showing) {
			if (markerLineSeries1 == null) {
				markerAdapter1 = new SimpleDataAdapter<Double, Float>();
				markerLineSeries1 = new LineSeries();
				markerLineSeries1.getStyle().setLineColor(0xffFF6600);
				markerLineSeries1.getStyle().setLineColorBelowBaseline(0xffFF6600);
				markerLineSeries1.setDataAdapter(markerAdapter1);
			}
			if (markerLineSeries2 == null) {
				markerAdapter2 = new SimpleDataAdapter<Double, Float>();
				markerLineSeries2 = new LineSeries();
				markerLineSeries2.getStyle().setLineColor(0xffFFCC00);
				markerLineSeries2.getStyle().setLineColorBelowBaseline(0xffFFCC00);
				markerLineSeries2.setDataAdapter(markerAdapter2);
			}
			if (markerLineSeries3 == null) {
				markerAdapter3 = new SimpleDataAdapter<Double, Float>();
				markerLineSeries3 = new LineSeries();
				markerLineSeries3.getStyle().setLineColor(0xff99CC33);
				markerLineSeries3.getStyle().setLineColorBelowBaseline(0xff99CC33);
				markerLineSeries3.setDataAdapter(markerAdapter3);
			}
			if (markerLineSeries4 == null) {
				markerAdapter4 = new SimpleDataAdapter<Double, Float>();
				markerLineSeries4 = new LineSeries();
				markerLineSeries4.getStyle().setLineColor(0xff20C4F7);
				markerLineSeries4.getStyle().setLineColorBelowBaseline(0xff20C4F7);
				markerLineSeries4.setDataAdapter(markerAdapter4);
			}
			shinobiChart.addSeries(markerLineSeries1, displayAxis, yAxis);
			shinobiChart.addSeries(markerLineSeries2, displayAxis, yAxis);
			shinobiChart.addSeries(markerLineSeries3, displayAxis, yAxis);
			shinobiChart.addSeries(markerLineSeries4, displayAxis, yAxis);
			shinobiChart.redrawChart();
		} else {
			if (markerLineSeries1 != null)
				shinobiChart.removeSeries(markerLineSeries1);
			if (markerLineSeries2 != null)
				shinobiChart.removeSeries(markerLineSeries2);
			if (markerLineSeries3 != null)
				shinobiChart.removeSeries(markerLineSeries3);
			if (markerLineSeries4 != null)
				shinobiChart.removeSeries(markerLineSeries4);
		}
	}

	public void setChartValue(Date period, float value) {
		if (dataAdapter != null) {
			dataAdapter.add(new DataPoint<Date, Float>(period, value));
		}
	}

	public void clearChartValue() {
		if (dataAdapter != null) {
			dataAdapter.clear();
		}
	}

	public void setChartTarget(double value) {
		if (targetAdapter != null) {
			for (int i = 0; i <= 80; i ++) {
				targetAdapter.add(new DataPoint<Double, Double>((double)i, value));
			}
		}
	}

	/**
	 * clear the target line' values, but not remove the target line
	 */
	public void clearChartTarget() {
		if (targetAdapter != null) {
			targetAdapter.clear();
		}
	}

	/**
	 * Add one reference value. 
	 * @param period
	 * @param value
	 */
	public void setChartReference(Date period, float value) {
		if (referenceAdapter != null) {
			referenceAdapter.add(new DataPoint<Date, Float>(period, value));
		}
	}

	public void clearChartReference() {
		if (referenceAdapter != null) {
			referenceAdapter.clear();
		}
	}

	public void setChartMarker(Date minDate, Date maxDate, float[] markValues) {
		if (markValues == null) {
			// clear mark lines;
			if (markerAdapter1 != null) {
				markerAdapter1.clear();
			}
			if (markerAdapter2 != null) {
				markerAdapter2.clear();
			}
			if (markerAdapter3 != null) {
				markerAdapter3.clear();
			}
			if (markerAdapter4 != null) {
				markerAdapter4.clear();
			}
		} else if (markValues.length >= 4) {
			// add a marker
			if (markerAdapter1 != null) {
				markerAdapter1.add(new DataPoint<Double, Float>(0., markValues[0]));
				markerAdapter1.add(new DataPoint<Double, Float>(80., markValues[0]));
			}
			if (markerAdapter2 != null) {
				markerAdapter2.add(new DataPoint<Double, Float>(0.,markValues[1]));
				markerAdapter2.add(new DataPoint<Double, Float>(80., markValues[1]));
			}
			if (markerAdapter3 != null) {
				markerAdapter3.add(new DataPoint<Double, Float>(0., markValues[2]));
				markerAdapter3.add(new DataPoint<Double, Float>(80., markValues[2]));
			}
			if (markerAdapter4 != null) {
				markerAdapter4.add(new DataPoint<Double, Float>(0., markValues[3]));
				markerAdapter4.add(new DataPoint<Double, Float>(80.,markValues[3]));
			}
		}

	}

	public void clearChart() {
		clearChartValue();
		clearChartTarget();
		clearChartReference();
		setChartMarker(null, null, null);
	}
	
	public void setShowingScale(Date currentDate,int per) {
//		DateRange currentRange = (DateRange) xAxis.getCurrentDisplayedRange();
		Date max;
		Date min;
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		switch (per) {
		case PER_DAILY:
			max = currentDate;
			c.add(Calendar.DAY_OF_YEAR, -1);
			min = c.getTime();
//			xAxis.setLabelFormat(DATEFORMAT_DD);
//			xAxis.setMajorTickFrequency(new DateFrequency(1, Denomination.DAYS));
			break;
		case PER_WEEKLY:
			c.add(Calendar.DAY_OF_YEAR, Calendar.SUNDAY  - c.get(Calendar.DAY_OF_WEEK) - 1);
			min = c.getTime();
			c.add(Calendar.DAY_OF_YEAR, 7);
			max = c.getTime();
//			xAxis.setLabelFormat(DATEFORMAT_DD);
//			xAxis.setMajorTickFrequency(new DateFrequency(24, Denomination.HOURS));
			break;
		case PER_MONTHLY:
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.DAY_OF_MONTH, -1);
			min = c.getTime();
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MONTH, 1);
			max = c.getTime();
//			xAxis.setLabelFormat(DATEFORMAT_DD);
//			xAxis.setMajorTickFrequency(new DateFrequency(24 * 4, Denomination.HOURS));
			break;
		case PER_YEARLY:
			c.set(Calendar.DAY_OF_YEAR, 1);
			c.add(Calendar.DAY_OF_MONTH, -1);
			min = c.getTime();
			c.add(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.YEAR, 1);
			max = c.getTime();
//			xAxis.setLabelFormat(DATEFORMAT_MM);
//			xAxis.setMajorTickFrequency(new DateFrequency(4*3, Denomination.WEEKS));
			break;

		default:
			return;
		}
		xAxis.requestCurrentDisplayedRange(min, max, true, false);
		//shinobiChart.redrawChart();
	}
	
	public void setSelectNext() {
		int currentSelectedIndex = -1;
		for (int i = 0, size = dataAdapter.size() ; i < size; i ++) {
			if (datavalueSeries.isPointSelected (i)) {
				currentSelectedIndex = i;
				break;
			}
		}
		if (currentSelectedIndex < dataAdapter.size() - 1) {
			if (currentSelectedIndex != -1)
				datavalueSeries.setPointSelected(false, currentSelectedIndex);
			datavalueSeries.setPointSelected(true, currentSelectedIndex+1);
			DateRange displayedRange = (DateRange) xAxis.getCurrentDisplayedRange();
			Date currentSelectedDate = dataAdapter.get(currentSelectedIndex + 1).getX();
			if (displayedRange.getMaximum().before(currentSelectedDate)) {
				xAxis.requestCurrentDisplayedRange(displayedRange.getMinimum(), currentSelectedDate);
				
			}
			shinobiChart.redrawChart();
		}
	}
	
	public void setSelectPrevious() {
		int size = dataAdapter.size();
		int currentSelectedIndex = size ;
		for (int i = 0 ; i < size; i ++) {
			if (datavalueSeries.isPointSelected (i)) {
				currentSelectedIndex = i;
				break;
			}
		}
		if (currentSelectedIndex > 0) {
			if (currentSelectedIndex != size)
				datavalueSeries.setPointSelected(false, currentSelectedIndex);
			datavalueSeries.setPointSelected(true, currentSelectedIndex -1);
			DateRange displayedRange = (DateRange) xAxis.getCurrentDisplayedRange();
			Date currentSelectedDate = dataAdapter.get(currentSelectedIndex - 1).getX();
			if (displayedRange.getMinimum().after(currentSelectedDate)) {
				xAxis.requestCurrentDisplayedRange(currentSelectedDate, displayedRange.getMaximum());
				
			}
			shinobiChart.redrawChart();
		}
	}
	
	 public void screenShot() {
    	shinobiChart.requestSnapshot();
    }
    
    public void deleteTempShot() {
    	new File(TEMP_FILE_FOLDER + ".temp.png").delete();
    }


	/**
	 * @return the chartCategory
	 */
	public int getChartCategory() {
		return chartCategory;
	}

	@Override
	public void onPointSelectionStateChanged(Series<?> arg0, int index) {
		Log.d(TAG, "onPointSelectionStateChanged" + index +" ("+ arg0.getDataAdapter().get(index).getX() + ", "+ arg0.getDataAdapter().get(index).getY());
		TextView popupView = getPopupViewByIndex(index,(Date)arg0.getDataAdapter().get(index).getX(), (Float) arg0.getDataAdapter().get(index).getY());
		showPopup(popupView, (Date)arg0.getDataAdapter().get(index).getX(), (Float)arg0.getDataAdapter().get(index).getY());
		if (listener != null) {
			listener.onSelectItemChanged((Date)arg0.getDataAdapter().get(index).getX(), (Float) arg0.getDataAdapter().get(index).getY());
		}
	}

	@Override
	public void onSeriesSelectionStateChanged(Series<?> arg0) {
		
		
	}
	

	@Override
	public void onSnapshotDone(Bitmap bitmap) {
		File folder = new File(TEMP_FILE_FOLDER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String strFileName =TEMP_FILE_FOLDER + ".temp.png";
		try {
			savePic(bitmap, strFileName);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(strFileName)));
			intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
			intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intent, getActivity().getTitle()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void onAxisRangeChange(Axis<?, ?> arg0) {
		handler.removeCallbacksAndMessages(null);
		if (mPopupWindow!= null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
		if (arg0 == xAxis) {
			DateRange range = (DateRange) xAxis.getCurrentDisplayedRange();
			int days = (int) (range.getSpan()/60/60/24);
			if (days < 4) {
				// too small;
				
			} else if (days < 8) {
				xAxis.setLabelFormat(DATEFORMAT_DD);
				xAxis.setMajorTickFrequency(new DateFrequency(1, Denomination.DAYS));
			} else if (days < 63) {
				xAxis.setLabelFormat(DATEFORMAT_DD);
				xAxis.setMajorTickFrequency(new DateFrequency(days/6, Denomination.DAYS));
			} else if (days < 220) {
				xAxis.setLabelFormat(DATEFORMAT_MM);
				xAxis.setMajorTickFrequency(new DateFrequency(1, Denomination.MONTHS));
			} else if (days < 1096) {
				xAxis.setLabelFormat(DATEFORMAT_MM);
				xAxis.setMajorTickFrequency(new DateFrequency(days/124, Denomination.MONTHS));
			} else if (days < 2557) {
				xAxis.setLabelFormat(DATEFORMAT_YY);
				xAxis.setMajorTickFrequency(new DateFrequency(1 , Denomination.YEARS));
			} else {
				xAxis.setLabelFormat(DATEFORMAT_YY);
				xAxis.setMajorTickFrequency(new DateFrequency(days/1406, Denomination.YEARS));
			}
		}
	}

	
	private TextView getPopupViewByIndex(int index,Date time, float value) {
		if (popupView == null) {
			popupView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.main_popupview, null);
		}
		String str;
		if (time.getHours() == 0 && time.getMinutes() == 0) {
			str = String.format(
					"%.1f%s",
					value,
					unit);
		} else {
			str = String.format(
					"%.1f%s\n %s",
					value,
					unit,
					android.text.format.DateFormat.getTimeFormat(
							getActivity().getApplicationContext()).format(time));
		}
		//Log.d(TAG, "popupView.setText "+str);
		popupView.setText(str);
		/*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		int widthofView = popupView.getWidth();
		// Log.d(TAG,"width of view:"+widthofView);
		int heightofView = popupView.getHeight();;
		// Log.d(TAG,"height of view:"+heightofView);
		int positionX = (int) positionXofPoint(index, widthofView);
		// Log.d(TAG,"position X:"+positionX);
		int positionY = (int) positionYofPoint(index, heightofView);
		// Log.d(TAG,"position Y:"+positionY);
		int widthofScreen = getWidthofScreen();
		// Log.d(TAG,"width of screen:"+widthofScreen);

		// if position x is less than zero
		int diff = 0;
		if (positionX < 0) {
			diff = positionX - 0;
			positionX = 0;
		}
		// if position x is too large
		else if ((positionX + widthofView) > widthofScreen) {
			diff = (positionX + widthofView) - widthofScreen;
			positionX = widthofScreen - widthofView;
		}

		layoutParams.setMargins(positionX, positionY, 0, 0);*/
		//popupView.setLayoutParams(layoutParams);
		return popupView;

	}

	private int getWidthofScreen() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		return width;
	}

	private double positionXofPoint(Date date, int widthOfView) {
		// Log.d(TAG,"index of point : "+index);
		double position = 0.0f;
		DateRange currentDisplayedRange = (DateRange) shinobiChart
				.getXAxis().getCurrentDisplayedRange();
		long numberDiff = (currentDisplayedRange.getMaximum().getTime() - currentDisplayedRange.getMinimum().getTime());
//		 Log.d(TAG,"x diff range:"+numberDiff);

		long diff = (date.getTime() - currentDisplayedRange.getMinimum().getTime());
//		 Log.d(TAG,"x index diff : "+diff);
		double percent = diff / (double)numberDiff;
//		 Log.d(TAG,"x precentage : "+percent);
		position = percent
				* (shinobiChart.getPlotAreaRect().right - shinobiChart
						.getPlotAreaRect().left)
				+ shinobiChart.getPlotAreaRect().left - widthOfView / 2;
		return position;
	}

	private double positionYofPoint(float index, int heightOfView) {
		// Log.d(TAG,"index of point : "+index);
		double position = 0.0;
		NumberRange currentDisplayedRange = (NumberRange) shinobiChart
				.getYAxis().getCurrentDisplayedRange();
		double numberDiff = (currentDisplayedRange.getMaximum() - currentDisplayedRange
				.getMinimum());
		 Log.d(TAG,"y diff range:"+" "+currentDisplayedRange.getMaximum() +" "+currentDisplayedRange
					.getMinimum()+" == "+numberDiff);

		double diff = ((double)index - currentDisplayedRange.getMinimum());
//		 Log.d(TAG,"y index diff : "+diff);
		double percent = diff / numberDiff;
//		 Log.d(TAG,"y precentage : "+percent);
		position = (1 - percent)
				* (shinobiChart.getPlotAreaRect().bottom - shinobiChart
						.getPlotAreaRect().top) + shinobiChart
						.getPlotAreaRect().top - heightOfView;
		return position;
	}

	private void showPopup(final TextView popupView, Date valueX, float valueY) {
		float density = getResources().getDisplayMetrics().density;
		int widthofView = (int) (density * 125);
//		Log.d(TAG,"width of view:"+widthofView);
		int heightofView = (int) (density * 75);
//		Log.d(TAG,"height of view:"+heightofView);
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(widthofView, heightofView);
		} 
		handler.removeCallbacksAndMessages(null);
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
		mPopupWindow.setContentView(popupView);
		mPopupWindow.update();
		
		int positionX = (int) positionXofPoint(valueX, widthofView);
		// Log.d(TAG,"position X:"+positionX);
		int positionY = (int) positionYofPoint(valueY, heightofView);
		// Log.d(TAG,"position Y:"+positionY);
		int widthofScreen = getWidthofScreen();
		// Log.d(TAG,"width of screen:"+widthofScreen);

		// if position x is less than zero
		int diff = 0;
		if (positionX < 0) {
			diff = positionX - 0;
			positionX = 0;
		}
		// if position x is too large
		else if ((positionX + widthofView) > widthofScreen) {
			diff = (positionX + widthofView) - widthofScreen;
			positionX = widthofScreen - widthofView;
		}
//		Log.d(TAG, "showAsDropDown " +positionX +" , "+positionY);
		mPopupWindow.showAsDropDown(getView(), positionX, positionY - getView().getHeight());
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		}, 3000);
		/*Animation animationFadeIn = AnimationUtils.loadAnimation(
				getActivity(), android.R.anim.fade_in);
		animationFadeIn.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation paramAnimation) {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						dissmissPopup();
					}
				}, 3000);
			}

			public void onAnimationRepeat(Animation paramAnimation) {
			}

			public void onAnimationEnd(Animation paramAnimation) {

			}
		});*/

		//mPopupWindow.setAnimationStyle(android.R.anim.fade_in);
	}

/*	private void dissmissPopup() {
		Animation animationFadeOut = AnimationUtils.loadAnimation(
				getActivity(), android.R.anim.fade_out);
		animationFadeOut.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation paramAnimation) {
			}

			public void onAnimationRepeat(Animation paramAnimation) {
			}

			public void onAnimationEnd(Animation paramAnimation) {
				// without the post method, the main UI crashes if the view is
				// removed
				mRootView.post(new Runnable() {
					public void run() {
						// it works without the runOnUiThread, but all UI
						// updates must
						// be done on the UI thread
						getActivity().runOnUiThread(new Runnable() {
									public void run() {
										if (mPopupWindow != null) {
											mPopupWindow.dismiss();
										}
									}
						});
					}
				});
			}
		});
		popupView.startAnimation(animationFadeOut);
	}*/
	
	/**
     * take Screen Shot for sharing
     * @return
     */
    private  Bitmap takeScreenShot(){     
        //View是你需要截图的View     
        getView().setDrawingCacheEnabled(true);     
        getView().buildDrawingCache();     
        Bitmap b1 = getView().getDrawingCache();     
              
        //获取状态栏高度     
        Rect frame = new Rect();       
        //getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);       
        int statusBarHeight = frame.top;       
        System.out.println(statusBarHeight);     
              
        //获取屏幕长和高     
        int width = getView().getWidth();       
        int height = getView().getHeight();       
        //去掉标题栏     
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);     
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);     
        getView().destroyDrawingCache();     
        return b;     
    }
    
    private  void savePic(Bitmap b,String strFileName) throws IOException{     
        FileOutputStream fos = null;     
            fos = new FileOutputStream(strFileName);     
            if (null != fos)     
            {     
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);     
                fos.flush();     
                fos.close();     
            }     
          
    }
	
	/**
	 * @param listener the listener to set
	 */
	public void setOnSelectItemChangedListener(OnSelectItemChangedEvent listener) {
		this.listener = listener;
	}
	
	public interface OnSelectItemChangedEvent {
    	
    	public void onSelectItemChanged (Date time, float value);
    }

}
