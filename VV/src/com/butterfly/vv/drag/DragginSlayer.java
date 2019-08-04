package com.butterfly.vv.drag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/*
 Demonstration of one way to put a set of draggable symbols on screen.
 Adapted loosely from material discussed in
 http://android-developers.blogspot.com/2010/06/making-sense-of-multitouch.html
 See also
 http://android-developers.blogspot.com/2010/07/how-to-have-your-cupcake-and-eat-it-too.html
 This example requires only API 3 (Android 1.5).   
 */
public class DragginSlayer extends View {
	// Colors for background and text
	static final int BACKGROUND_COLOR = Color.argb(255, 0, 0, 0);
	static final int HEADER_COLOR = Color.argb(255, 30, 30, 30);
	static final int TEXT_COLOR = Color.argb(255, 255, 255, 0);
	private int numberSymbols; // Total number of symbols to use
	private int numberInstances; // Total number of symbol instances onstage
	private int maxInstances; // Maximum number of symbol instances permitted
								// onstage
	private Drawable[] symbol; // Array of symbols (dimension numberSymbols)
	private int[] symbolIndex; // Index of drawable resource (R.drawable.symbol)
	private Drawable[] symbol0; // Array of symbols (dimension numberSymbols)
	private float[] X0; // Initial x coordinate, upper left corner of symbol i
	private float[] Y0; // Initial y coordinate, upper left corner of symbol i
	private float[] X; // Current x coordinate, upper left corner of symbol i
	private float[] Y; // Current y coordinate, upper left corner of symbol i
	private int[] symbolWidth; // Width of symbol i
	private int[] symbolHeight; // Height of symbol i
	private float[] lastTouchX; // x coordinate of symbol i at last touch
	private float[] lastTouchY; // y coordinate of symbol i at last touch
	private int symbolSelected; // Index of symbol last touched (-1 if none
								// selected)
	private int instanceSelected; // Index of symbol instance last touched (-1
									// if none)
	private Paint paint; // Paint object holding draw formatting information
	// Following define upper left and lower right corners of display stage
	// rectangle
	private int stageX1 = 0;
	private int stageY1 = 45;
	private int stageX2 = 500;
	private int stageY2 = 700;
	private boolean isDragging = false; // True if any symbol is currently being
										// dragged
	private Context context;
	private Bitmap lastGoodBitmap;
	Canvas myCanvas;

	public DragginSlayer(Context context, float[] XX, float[] YY,
			int[] symbolIndex) {
		// Call through to simplest constructor of View superclass
		super(context);
		this.context = context;
		// Initialize instance counters
		maxInstances = 12;
		numberInstances = 0;
		// Set up local arrays defining symbols and their positions
		this.X0 = XX;
		this.Y0 = YY;
		this.symbolIndex = symbolIndex;
		numberSymbols = X0.length;
		this.X = new float[maxInstances];
		this.Y = new float[maxInstances];
		symbol0 = new Drawable[numberSymbols];
		symbol = new Drawable[maxInstances];
		symbolWidth = new int[numberSymbols];
		symbolHeight = new int[numberSymbols];
		lastTouchX = new float[maxInstances];
		lastTouchY = new float[maxInstances];
		// Fill the symbol arrays with data
		for (int i = 0; i < numberSymbols; i++) {
			symbol0[i] = context.getResources().getDrawable(symbolIndex[i]);
			symbolWidth[i] = symbol0[i].getIntrinsicWidth();
			symbolHeight[i] = symbol0[i].getIntrinsicHeight();
			symbol0[i].setBounds(0, 0, symbolWidth[i], symbolHeight[i]);
		}
		// Set up the Paint object that will control format of screen draws
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(13);
		paint.setStrokeWidth(0);
	}

	ArrayList<Drawable> drawables;

	public DragginSlayer(Context context, float[] XX, float[] YY,
			ArrayList<Drawable> drawables) {
		// Call through to simplest constructor of View superclass
		super(context);
		this.context = context;
		// Initialize instance counters
		maxInstances = 12;
		numberInstances = 0;
		// Set up local arrays defining symbols and their positions
		this.X0 = XX;
		this.Y0 = YY;
		// this.symbolIndex = symbolIndex;
		this.drawables = drawables;
		numberSymbols = X0.length;
		this.X = new float[maxInstances];
		this.Y = new float[maxInstances];
		symbol0 = new Drawable[numberSymbols];
		symbol = new Drawable[maxInstances];
		symbolWidth = new int[numberSymbols];
		symbolHeight = new int[numberSymbols];
		lastTouchX = new float[maxInstances];
		lastTouchY = new float[maxInstances];
		// Fill the symbol arrays with data
		for (int i = 0; i < numberSymbols; i++) {
			// symbol0[i] = context.getResources().getDrawable(symbolIndex[i]);
			symbol0[i] = drawables.get(i);
			symbolWidth[i] = symbol0[i].getIntrinsicWidth();
			symbolHeight[i] = symbol0[i].getIntrinsicHeight();
			symbol0[i].setBounds(0, 0, symbolWidth[i], symbolHeight[i]);
		}
		// Set up the Paint object that will control format of screen draws
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(13);
		paint.setStrokeWidth(0);
	}
	/*
	 * Process MotionEvents corresponding to screen touches and drags. MotionEvent reports movement
	 * (mouse, pen, finger, trackball) events. The MotionEvent method getAction() returns the kind
	 * of action being performed as an integer constant of the MotionEvent class, with possible
	 * values ACTION_DOWN, ACTION_MOVE, ACTION_UP, and ACTION_CANCEL. Thus we can switch on the
	 * returned integer to determine the kind of event and the appropriate action.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		switch (action) {
		// MotionEvent class constant signifying a finger-down event
			case MotionEvent.ACTION_DOWN: {
				isDragging = false;
				// Get coordinates of touch event
				final float x = ev.getX();
				final float y = ev.getY();
				// Initialize symbol and instance indices
				symbolSelected = -1; // -1 if touch not within current bounds of
										// symbol source
				instanceSelected = -1; // -1 if touch not within bounds of symbol
										// instance
				// Determine if touch within bounds of one of the symbol sources
				// offstage
				for (int i = 0; i < numberSymbols; i++) {
					if ((x > X0[i] && x < (X0[i] + symbolWidth[i]))
							&& (y > Y0[i] && y < (Y0[i] + symbolHeight[i]))) {
						symbolSelected = i; // Index of symbol source touched
						break;
					}
					// Warn if max number of instances has been reached (it won't
					// create any more)
					if (numberInstances == maxInstances) {
						String toaster = "Maximum number of instances ";
						toaster += "(" + maxInstances + ") has been reached.";
						/*Toast.makeText(context, toaster, Toast.LENGTH_LONG)
								.show();*/
					}
				}
				// Determine if touch within bounds of one of the symbol instances
				// onstage
				for (int i = 0; i < numberInstances; i++) {
					if ((x > X[i] && x < (X[i] + symbol[i].getIntrinsicWidth()))
							&& (y > Y[i] && y < (Y[i] + symbol[i]
									.getIntrinsicHeight()))) {
						instanceSelected = i; // Index of symbol instance touched
						break;
					}
				}
				// If touch within bounds of a symbol source or instance, remember
				// start
				// position for this symbol
				if (symbolSelected > -1 || instanceSelected > -1) {
					if (instanceSelected > -1)
						lastTouchX[instanceSelected] = x;
					if (instanceSelected > -1)
						lastTouchY[instanceSelected] = y;
				}
				break;
			}
			// MotionEvent class constant signifying a finger-drag event
			case MotionEvent.ACTION_MOVE: {
				// Only process if initial touch selected a symbol and not
				// background
				// If touch and drag were on symbol source, and this hasn't yet been
				// processed,
				// first create a
				// new symbol instance (but only if the max number of instances will
				// not be
				// exceeded). Do it
				// here rather than in ACTION_DOWN so that just pressing the source
				// symbol without a
				// drag will
				// not create a new instance.
				if (symbolSelected > -1 && instanceSelected == -1
						&& numberInstances < maxInstances) {
					// symbol[numberInstances] =
					// context.getResources().getDrawable(symbolIndex[symbolSelected]);
					symbol[numberInstances] = drawables.get(symbolSelected);
					symbol[numberInstances].setBounds(0, 0,
							symbolWidth[symbolSelected],
							symbolHeight[symbolSelected]);
					instanceSelected = numberInstances;
					numberInstances++;
				}
				// Drag the instance, if selected (either an old instance or one
				// just created)
				if (instanceSelected > -1) {
					isDragging = true;
					final float x = ev.getX();
					final float y = ev.getY();
					// Calculate the distance moved
					final float dx = x - lastTouchX[instanceSelected];
					final float dy = y - lastTouchY[instanceSelected];
					// Move the instance selected
					X[instanceSelected] += dx;
					Y[instanceSelected] += dy;
					// Remember this touch position for the next move event of this
					// object
					lastTouchX[instanceSelected] = x;
					lastTouchY[instanceSelected] = y;
					// Request a redraw
					invalidate();
				}
				break;
			}
			// MotionEvent class constant signifying a finger-up event
			case MotionEvent.ACTION_UP:
				isDragging = false;
				invalidate(); // Request redraw
				//
				// Log.i("VV", "**"+instanceSelected);
				if (instanceSelected != -1) {
					// Log.i("VV", instanceSelected+ " -- "+
					// numberInstances+"--**"+ev.getX()+","+ev.getY()+">>"+X[instanceSelected]+" "+Y[instanceSelected]);
					myCanvas.save();
					myCanvas.translate(X[instanceSelected], Y[instanceSelected]);
					symbol[instanceSelected].draw(myCanvas);
					myCanvas.restore();
					// saveImage(getContext());
					foruri = saveImage();
					//
				}
				break;
		}
		return true;
	}

	String foruri = null;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		lastGoodBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		myCanvas = new Canvas(lastGoodBitmap);
		// Log.i("VV", "onSizeChanged");
	}
	// This method will be called each time the screen is redrawn. The draw is
	// on the Canvas object, with formatting controlled by the Paint object.
	// When to redraw is under Android control, but we can request a redraw
	// using the method invalidate() inherited from the View superclass.
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// Log.i("VV", "onDraw");
		// lastGoodBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
		// Bitmap.Config.ARGB_8888);
		// // myCanvas = new Canvas(lastGoodBitmap);
		// canvas.setBitmap(lastGoodBitmap);
		// Draw backgrounds
		drawBackground(paint, canvas);
		// Draw all draggable symbols at their current dragged locations
		for (int i = 0; i < numberInstances; i++) {
			canvas.save();
			canvas.translate(X[i], Y[i]);
			symbol[i].draw(canvas);
			canvas.restore();
		}
		// canvas.drawBitmap(lastGoodBitmap, 0, 0, paint);
		// myCanvas.drawBitmap(lastGoodBitmap, 0, 0, paint);
		// for(int i=0; i<numberInstances; i++){
		// myCanvas.save();
		// myCanvas.translate(X[i],Y[i]);
		// symbol[i].draw(myCanvas);
		// myCanvas.restore();
		// }
		// canvas.drawBitmap(lastGoodBitmap, 0, 0, null);
		// canvas.setBitmap(lastGoodBitmap);
	}
	public Boolean saveImage(Context context) {
		String newFileName = null;
		Calendar c = Calendar.getInstance();
		String sDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-"
				+ (c.get(Calendar.DAY_OF_MONTH) + 1) + "_"
				+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);
		newFileName = "stamps-" + sDate + ".png";
		// now save out the file holmes!
		OutputStream outStream = null;
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		if (newFileName != null) {
			File file = new File(extStorageDirectory, newFileName);
			try {
				outStream = new FileOutputStream(file);
				lastGoodBitmap.compress(Bitmap.CompressFormat.PNG, 100,
						outStream);
				try {
					outStream.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				/*Toast.makeText(context, "Saved " + newFileName,
						Toast.LENGTH_LONG).show();*/
			} catch (FileNotFoundException e) {
				// do something if errors out?
				return false;
			}
		}
		return true;
	}
	public String saveImage() {
		String newFileName = null;
		String path = null;
		Calendar c = Calendar.getInstance();
		String sDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-"
				+ (c.get(Calendar.DAY_OF_MONTH) + 1) + "_"
				+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);
		newFileName = "stamps-" + sDate + ".png";
		// now save out the file holmes!
		OutputStream outStream = null;
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		if (newFileName != null) {
			File file = new File(extStorageDirectory, newFileName);
			try {
				outStream = new FileOutputStream(file);
				lastGoodBitmap.compress(Bitmap.CompressFormat.PNG, 100,
						outStream);
				try {
					outStream.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
			}
		}
		return path = extStorageDirectory + "/" + newFileName;
	}
	// Method to draw the background for the screen. Invoked from onDraw each
	// time
	// the screen is redrawn.
	private void drawBackground(Paint paint, Canvas canvas) {
		// Draw header bar background
		// paint.setColor(HEADER_COLOR);
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, stageX2, stageY2, paint);
		// Draw main stage background
		// paint.setColor(BACKGROUND_COLOR);
		// canvas.setBitmap(lastGoodBitmap);
		paint.setColor(Color.RED);
		canvas.drawRect(stageX1, stageY1 + 100, stageX2, stageY2, paint);
		// Draw image of symbols at their original locations to denote source
		for (int i = 0; i < numberSymbols; i++) {
			canvas.save();
			canvas.translate(X0[i], Y0[i]);
			symbol0[i].draw(canvas);
			canvas.restore();
		}
		// While a symbol is being dragged, display its x and y coordinates in a
		// readout
		if (isDragging) {
			paint.setColor(TEXT_COLOR);
			canvas.drawText("Instance " + instanceSelected, 200, 12, paint);
			canvas.drawText("X = " + X[instanceSelected], 200, 27, paint);
			canvas.drawText("Y = " + Y[instanceSelected], 200, 42, paint);
		}
	}
	public Bitmap getLastGoodBitmap() {
		return lastGoodBitmap;
	}
	public String getForuri() {
		return foruri;
	}
}
