package audiovisualizer.fx.visualizer.visualization.deprecated;

import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import application.mediaPlayer.TavMetaData;
import application.visualizer.interfacing.TavVisualizationCustomizable;
import application.visualizer.interfacing.TavVisualizationHeight;
import application.visualizer.interfacing.TavVisualizationWidth;

//import javafx.scene.effect.Reflection;
public class TestVis2 implements TavVisualizationCustomizable
{
	private final TavVisualizationWidth initX;
	private final TavVisualizationHeight initY;

	// remove one of the 2s if not using octave

	private final StackPane visualizerPane = new StackPane();
	private AnchorPane root;
	private float[] prevMagnitudes;
	private GraphicsContext gc;

	float futureTop = 0;
	boolean running = false;
	private float barW;
	private final Stop[] stops = new Stop[] { new Stop (0, Color.DARKVIOLET),
			new Stop (1, Color.BLACK) };
	private final Stop[] stopsInner = new Stop[] { new Stop (0, Color.BLACK),
			new Stop (.2, Color.YELLOWGREEN), new Stop (.5, Color.BLACK),
			new Stop (.8, Color.YELLOWGREEN), new Stop (1, Color.BLACK) };
	private final Stop[] stopsInner2 = new Stop[] { new Stop (.2, Color.BLACK),
			new Stop (.5, Color.BLUE), new Stop (.9, Color.BLACK) };
	private final LinearGradient lg1 = new LinearGradient (0, 0, 0, .5, true,
			CycleMethod.REFLECT, stops);
	private final LinearGradient lg2 = new LinearGradient (0, 0, 0, .5, true,
			CycleMethod.REFLECT, stopsInner);
	private final LinearGradient lg3 = new LinearGradient (0, 0, 0, .5, true,
			CycleMethod.NO_CYCLE, stopsInner2);

	Canvas canvas;
	float canvasCenterY;
	float canvasCenterX;
	Stage stage;
	Scene scene;

	private final ArrayBlockingQueue<float[]> bartopStack = new ArrayBlockingQueue<float[]> (
			2000);

	private boolean built = false;
	private AnchorPane controlPane;

	private int numBands = 200;
	private int threshold = -90;
	private double interval = .025;

	private final float[] bartopArray = new float[numBands];
	private TavMetaData songMeta;
	private Slider slider;
	private Slider slider2;

	public TestVis2(Canvas canvas, GraphicsContext gc, Stage visualizerStage,
			AnchorPane controlPane)
	{
		// TODO Auto-generated constructor stub
		this.initX = new TavVisualizationWidth();
		this.initY = new TavVisualizationHeight();
		this.root = (AnchorPane) visualizerStage.getScene().getRoot();
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.controlPane = controlPane;
		this.stage = visualizerStage;
	}

	public AnchorPane getPane()
	{
		return this.controlPane;
	}

	// @Override
	public void build2(Number videoWidth)
	{

		videoWidth = 0;

		if (built)
			return;
		videoWidth = 0;

		initY.setValue ((float) root.getHeight());
		initX.setValue ((float) (root.getWidth()));
		barW = initX.intValue() / numBands;

		canvasCenterY = initY.floatValue() / 2.0f;
		canvasCenterX = initX.floatValue() / 2.0f;
		// gc2 = ...
		gc.setLineWidth (barW);
		prevMagnitudes = new float[numBands];

		// make sure prevMagnitudes are not greater than
		// the maximum of any future magnitude (i.e. 0)
		for (int i = 0; i < numBands; i++)
			prevMagnitudes[i] = threshold * 2;

		// motionBlur.setRadius(5);
		// gc.setEffect(motionBlur);
		slider = new Slider();
		slider.setLayoutX (50);
		slider.setLayoutY (canvasCenterY);
		slider.setScaleY (2.0);
		slider.setMin ( -100);
		slider.setMax (100);
		slider.setValue (40);
		slider.setShowTickLabels (true);
		slider.setShowTickMarks (true);
		slider.setMajorTickUnit (50);
		slider.setMinorTickCount (5);
		slider.setBlockIncrement (10);
		slider.setOrientation (Orientation.VERTICAL);
		slider.valueProperty().addListener (new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val)
			{
				// threshold = new_val.intValue();
				barW = new_val.intValue();
			}
		});
		slider2 = new Slider();
		slider2.setLayoutX (450);
		slider2.setLayoutY (50);
		slider2.setMin ( -150);
		slider2.setMax ( -1);
		slider2.setValue (threshold);
		slider2.setShowTickLabels (true);
		slider2.setShowTickMarks (true);
		slider2.setMajorTickUnit (50);
		slider2.setMinorTickCount (5);
		slider2.setBlockIncrement (10);
		slider2.setOrientation (Orientation.VERTICAL);
		slider2.valueProperty().addListener (new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val)
			{
				threshold = new_val.intValue();
			}
		});

		root.getChildren().add (slider2);
		root.getChildren().add (slider);
		// visualizerPane.getChildren().add(slider2);

		if ( ! (bartopStack.size() < 1999))
			bartopStack.clear();

		bartopStack.add (bartopArray);
		built = true;

	}

	public boolean built()
	{
		return built;
	}

	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset)
	{
		Platform.runLater (new HelloRunnable (magnitudes, this.gc));
	}

	// @Override
	// public void cleanup()
	// {
	// }

	public class HelloRunnable implements Runnable
	{

		private final float[] magnitudes;

		public HelloRunnable(final float[] magnitudes, GraphicsContext gc)
		{
			this.magnitudes = magnitudes;
			run();

		}

		public void main(String[] args)
		{
			run();
		}

		@Override
		public void run()
		{
			if ( !built || bartopStack.size() < 1500)
				return;

			final float conversion = initY.floatValue()
					/ (2 * (threshold * 2));

			final float canvasCenterY = initY.floatValue() / 2.0f - 30;

			final float canvasCenterX = initX.floatValue() / 2.0f;

			final float[] magnitudes = this.magnitudes;

			gc.fillRect (0, 0, initX.doubleValue(), initY.doubleValue());

			/** Clear the entire frame and paint one frame onto the canvas */
			gc.setFill (Color.BLACK);
			gc.setStroke (Color.YELLOWGREEN);
			for (int i = 0; i < numBands; i++)
			{

				final float xPos = barW * i;
				final float octalMagnitude = magnitudes[i] + magnitudes[2 * i];

				final boolean louderThanBefore = octalMagnitude > prevMagnitudes[i];
				// POSITIONING "CURRENT"
				final float currentTop = bartopStack.peek()[i];
				final float currentBottom = (float) (canvas.getHeight() - currentTop) - 60;
				final float currentRight = xPos + canvasCenterX;
				final float currentLeft = canvasCenterX - xPos;

				if (louderThanBefore)
				{
					gc.setStroke (lg1);
					gc.setLineWidth ( (barW / 2) + 200 % 3);
					prevMagnitudes[i] = octalMagnitude;
					gc.setLineWidth (barW / 2);
				} else
				{
					if (prevMagnitudes[i] - 2 >= 2 * threshold)
						prevMagnitudes[i] = prevMagnitudes[i] - 2;
					magnitudes[i] = prevMagnitudes[i];
					gc.setStroke (i % 2 == 0 ? lg2 : lg3);
					gc.setLineWidth (barW);
				}

				// POSITIONING "FUTURE"
				futureTop = (prevMagnitudes[i]) * conversion;

				if (canvasCenterY - futureTop < .5)
				{
					futureTop = canvasCenterY;

				} else if ( (canvasCenterY - futureTop > -threshold * 1.7))
				{
					GraphicsContext gc2 = canvas.getGraphicsContext2D();
					gc2.setLineWidth (5);
					gc2.strokeLine (currentLeft, currentBottom / 2,
							currentLeft, currentBottom / 2 - 15);
					gc2.strokeLine (currentRight, currentBottom / 2,
							currentRight, currentBottom / 2 - 15);
				}
				// gc = canvas.getGraphicsContext2D();

				bartopArray[i] = futureTop;

				if (louderThanBefore)
					gc.setLineWidth (barW / 2);

				if ( ! (bartopStack.size() > 1999))
				{
					gc.strokeLine (xPos + canvasCenterX, currentBottom, xPos
							+ canvasCenterX, bartopStack.peek()[i]);
					gc.strokeLine (canvasCenterX - xPos, currentBottom,
							canvasCenterX - xPos, bartopStack.peek()[i]);
					bartopStack.add (bartopArray);
				} else
				{
					bartopStack.remove();
					bartopStack.remove();
				}
			}

		}
	}

	public void build(Scene scene2, int i)
	{
		this.scene = scene2;
		build2 (0);
		initX.setValue ((float) this.scene.getWidth());
		initY.setValue ((float) this.scene.getHeight());
	}

	// @Override
	// public double getRuntime()
	// {
	// return 0;
	// }

	@Override
	public double getInterval()
	{
		return this.interval;
	}

	@Override
	public int getNumBands()
	{
		return this.numBands * 2;
	}

	@Override
	public int getThreshold()
	{
		return this.threshold;
	}

	@Override
	public void setWidth(double width)
	{
		if (stage != null)
			stage.setWidth (width);
	}

	@Override
	public void setHeight(double height)
	{
		if (stage != null)
			stage.setHeight (height);
	}

	@Override
	public void setMetaData(TavMetaData metaData)
	{
		this.songMeta = metaData;
	}

	@Override
	public TavVisualizationWidth widthProperty()
	{
		return initX;
	}

	@Override
	public TavVisualizationHeight heightProperty()
	{
		return initY;
	}

	@Override
	public boolean isCustomizable()
	{
		return true;
	}

	@Override
	public void changeColor(int index, Color value)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Color getCustomColor(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCustomizeLevels(GridPane levelControls)
	{
		// TODO Auto-generated method stub

	}

}
