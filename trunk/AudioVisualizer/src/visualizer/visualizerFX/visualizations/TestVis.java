package visualizer.visualizerFX.visualizations;

import java.util.concurrent.ArrayBlockingQueue;

import application.visualizer.interfacing.TavVisualization;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

//import javafx.scene.effect.Reflection;
public class TestVis implements TavVisualization
{
	private float sliderCenterY;
	private float initX = 0;
	private float initY = 0;

	// remove one of the 2s if not using octave

	private final StackPane visualizerPane = new StackPane();
	private AnchorPane root;
	private float[] magnitudes;
	private float[] prevMagnitudes;
	private GraphicsContext gc;

	float futureTop = 0;
	boolean running = false;
	private float barW;
	private final Stop[] stops = new Stop[]
	{ new Stop(0, Color.DARKVIOLET), new Stop(1, Color.BLACK) };
	private final Stop[] stopsInner = new Stop[]
	{ new Stop(0, Color.BLACK), new Stop(.2, Color.YELLOWGREEN),
			new Stop(.5, Color.BLACK), new Stop(.8, Color.YELLOWGREEN),
			new Stop(1, Color.BLACK) };
	private final Stop[] stopsInner2 = new Stop[]
	{ new Stop(.2, Color.BLACK), new Stop(.5, Color.BLUE),
			new Stop(.9, Color.BLACK) };
	private final LinearGradient lg1 = new LinearGradient(0, 0, 0, .5, true,
			CycleMethod.REFLECT, stops);
	private final LinearGradient lg2 = new LinearGradient(0, 0, 0, .5, true,
			CycleMethod.REFLECT, stopsInner);
	private final LinearGradient lg3 = new LinearGradient(0, 0, 0, .5, true,
			CycleMethod.NO_CYCLE, stopsInner2);

	Canvas canvas;
	float canvasCenterY;
	float canvasCenterX;
	Stage stage;
	Scene scene;
	private final ArrayBlockingQueue<LinearGradient[]> gradientStack = new ArrayBlockingQueue<LinearGradient[]>(
			2000);
	private final ArrayBlockingQueue<float[]> bartopStack = new ArrayBlockingQueue<float[]>(
			2000);
	private final ArrayBlockingQueue<Integer[]> integerStack = new ArrayBlockingQueue<Integer[]>(
			2000);
	private boolean built = false;
	private AnchorPane root2;

	private int numBands = 200;
	private int threshold = -60;
	private double interval = .035;

	float[] bartopArray = new float[numBands];
	private final LinearGradient[] gradientArray = new LinearGradient[numBands];
	private final Integer[] integerArray = new Integer[numBands];
	
	public TestVis(AnchorPane root)
	{

		this.root = root;

	}

	public TestVis(Stage visualizerStage, AnchorPane controlPane)
	{
		// TODO Auto-generated constructor stub
		this((AnchorPane) visualizerStage.getScene().getRoot());
		this.root2 = controlPane;
		this.stage = visualizerStage;
	}

	public AnchorPane getPane()
	{
		return this.root2;
	}

	// @Override
	public void build2(Number videoWidth)
	{

		videoWidth = 0;

		root = (AnchorPane) scene.getRoot();
		initY = (float) root.getHeight();
		initX = (float) (root.getWidth());
		barW = initX / numBands;
		
		root.getChildren().remove(visualizerPane);
		root.getChildren().add(visualizerPane);

		canvas = new Canvas(initX, initY);
		// canvas.setLayoutY(25);
		canvasCenterY = initY / 2.0f;// -50;
		canvasCenterX = initX / 2.0f;
		gc = canvas.getGraphicsContext2D();
		scene.widthProperty().addListener(new visResizedWidthListener(canvas));
		scene.heightProperty()
				.addListener(new visResizedHeightListener(canvas));
		// gc2 = ...
		gc.setLineWidth(barW);
		prevMagnitudes = new float[numBands];
		// make sure prevMagnitudes are not greater than
		// the maximum of any future magnitude (i.e. 0)
		for (int i = 0; i < numBands; i++)
			prevMagnitudes[i] = threshold * 2;

		// motionBlur.setRadius(5);
		// gc.setEffect(motionBlur);

		Slider slider = new Slider();
		slider.setLayoutX(50);
		slider.setLayoutY(canvasCenterY);
		slider.setScaleY(2.0);
		slider.setMin(-100);
		slider.setMax(100);
		slider.setValue(40);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(50);
		slider.setMinorTickCount(5);
		slider.setBlockIncrement(10);
		slider.setOrientation(Orientation.VERTICAL);
		slider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val)
			{
				//threshold = new_val.intValue();
				barW = new_val.intValue();
			}
		});
		Slider slider2 = new Slider();
		slider2.setLayoutX(450);
		slider2.setLayoutY(50);
		slider2.setMin(-150);
		slider2.setMax(-1);
		slider2.setValue(threshold);
		slider2.setShowTickLabels(true);
		slider2.setShowTickMarks(true);
		slider2.setMajorTickUnit(50);
		slider2.setMinorTickCount(5);
		slider2.setBlockIncrement(10);
		slider2.setOrientation(Orientation.VERTICAL);
		slider2.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> ov,
					Number old_val, Number new_val)
			{
				threshold = new_val.intValue();
			}
		});
		visualizerPane.getChildren().add(canvas);

		visualizerPane.getChildren().add(new Pane());
		root.getChildren().add(slider2);
		root.getChildren().add(slider);
		//visualizerPane.getChildren().add(slider2);

		if (!(bartopStack.size() < 200))
			bartopStack.clear();

		bartopStack.add(bartopArray);
		gradientStack.add(gradientArray);
		// Reflection reflection = new Reflection();
		// canvas.setEffect(reflection);
		built = true;

	}

	public boolean built()
	{
		return built;
	}

	private double diffPlayerTime;
	private double diffTimeStamp;


	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset)
	{

//
//		diffTimeStamp = timestamp;
		this.magnitudes = magnitudes;
//
//		TavApplicationManager.getInstance().getMediaPlayerManager()
//				.getMediaPlayer().setAudioSpectrumInterval(interval);
//		TavApplicationManager.getInstance().getMediaPlayerManager()
//				.getMediaPlayer().setAudioSpectrumNumBands(numBands * 2);
//		TavApplicationManager.getInstance().getMediaPlayerManager()
//				.getMediaPlayer().setAudioSpectrumThreshold(threshold);

		Platform.runLater(new HelloRunnable());
	}

	// @Override
	// public void cleanup()
	// {
	// }

	public class HelloRunnable implements Runnable
	{

		@Override
		public void run()
		{
			if (!built)
				System.err.println("MAJOR ERROR");
			if ((bartopStack.size() > 50))
			{
				bartopStack.remove();
			}
			final float conversion = initY / (2 * (threshold * 2));

			canvasCenterY = initY / 2.0f + sliderCenterY - 30;
			// System.out.println(canvasCenterY);
			// System.err.println(initY);
			canvasCenterX = initX / 2.0f;
			gc.fillRect(0, 0, initX, initY);

			/** Clear the entire frame and paint one frame onto the canvas */
			gc.setFill(Color.BLACK);
			gc.setStroke(Color.YELLOWGREEN);
			for (int i = 0; i < numBands; i++)
			{
				final float xPos = barW * i;
				final float octalMagnitude = magnitudes[i] + magnitudes[2 * i];
				final boolean louderThanBefore = octalMagnitude > prevMagnitudes[i];
				// this magnitude greater than previous?
				if (louderThanBefore)
				{
					gc.setStroke(lg1);
					gradientArray[i] = lg1;
					gc.setLineWidth(barW / 2);
					integerArray[i] = (int) barW / 2;
					prevMagnitudes[i] = octalMagnitude;
				} else
				{ // gc.translate(0, -1);
					if (prevMagnitudes[i] - 2 >= 2 * threshold)
						prevMagnitudes[i] = prevMagnitudes[i] - 2;
					magnitudes[i] = prevMagnitudes[i];
					gc.setStroke(i % 2 == 0 ? lg2 : lg3);
					gradientArray[i] = (i % 2 == 0 ? lg2 : lg3);

					integerArray[i] = (int) barW;
				}
				// POSITIONING "CURRENT"
				final float currentTop = bartopStack.peek()[i] - threshold;
				final float currentBottom = (float) (canvas.getHeight() - currentTop);
				final float currentRight = xPos + canvasCenterX;
				final float currentLeft = canvasCenterX - xPos;

				// POSITIONING "FUTURE"
				futureTop = (prevMagnitudes[i]) * conversion;

				if (canvasCenterY - futureTop < .5)
					futureTop = canvasCenterY;

				else if ((canvasCenterY - futureTop > -threshold * 1.7))
				{
					// futureTop = futureTop - 0.1f*futureTop;
					final float multiTop = 2 * currentTop;

					GraphicsContext gc2 = canvas.getGraphicsContext2D();
					gc2.setLineWidth(5);
					gc2.strokeLine(currentLeft, currentBottom / 2, currentLeft,
							currentBottom / 2 - 15);
					gc2.strokeLine(currentRight, currentBottom / 2,
							currentRight, currentBottom / 2 - 15);

				}
				gc = canvas.getGraphicsContext2D();
				if (louderThanBefore)
					gc.setLineWidth(barW / 2);
				else
					gc.setLineWidth(barW);
				bartopArray[i] = futureTop;

				if (!(bartopStack.size() > 1999))
				{
					gc.strokeLine(xPos + canvasCenterX, currentBottom, xPos
							+ canvasCenterX, bartopStack.peek()[i]);
					gc.strokeLine(canvasCenterX - xPos, currentBottom,
							canvasCenterX - xPos, bartopStack.peek()[i]);

				}
				// System.out.println(bartopStack.size());
			}
			bartopStack.add(bartopArray);
		}
	}

	public void main(String args[])
	{
		// try {
		// Thread.sleep(55);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		(new Thread(new HelloRunnable())).start();
	}

	public class visResizedWidthListener implements ChangeListener<Number>
	{
		private Canvas acanvas;

		public visResizedWidthListener(Canvas acanvas)
		{
			this.acanvas = acanvas;
		}

		@Override
		public void changed(ObservableValue<? extends Number> observableValue,
				Number oldSceneWidth, Number newSceneWidth)
		{
			System.out.println("new width " + newSceneWidth.intValue());
			// acanvas.setWidth((Double) newSceneWidth);
			initX = newSceneWidth.floatValue();
			acanvas.setWidth(initX);
		}
	}

	public class visResizedHeightListener implements ChangeListener<Number>
	{
		private Canvas acanvas;

		public visResizedHeightListener(Canvas acanvas)
		{
			this.acanvas = acanvas;
		}

		@Override
		public void changed(ObservableValue<? extends Number> observableValue,
				Number oldSceneHeight, Number newSceneHeight)
		{
			System.out.println("New height" + newSceneHeight);
			// acanvas.setHeight((Double) newSceneHeight);
			initY = (newSceneHeight.floatValue());
			acanvas.setHeight(initY);
		}
	}

	public void build(Scene scene2, int i)
	{
		// TODO Auto-generated method stub
		this.scene = scene2;
		build2(0);
		initX = (float) this.scene.getWidth();
		initY = (float) this.scene.getHeight();

	}

	@Override
	public double getRuntime()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getInterval()
	{
		// TODO Auto-generated method stub
		return this.interval;
	}

	@Override
	public int getNumBands()
	{
		// TODO Auto-generated method stub
		return this.numBands * 2;
	}

	@Override
	public int getThreshold()
	{
		// TODO Auto-generated method stub
		return this.threshold;
	}

	@Override
	public void setWidth(double width)
	{
		// TODO Auto-generated method stub
		if (stage != null)
			stage.setWidth(width);
	}

	@Override
	public void setHeight(double height)
	{
		// TODO Auto-generated method stub
		if (stage != null)
		stage.setHeight(height);
	}

}
