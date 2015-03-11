package audiovisualizer.fx.visualizer.visualization.deprecated;

import java.util.concurrent.ArrayBlockingQueue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import application.visualizer.interfacing.TavVisualizationHeight;
import application.visualizer.interfacing.TavVisualizationWidth;

public class RenderTestVis2Frame implements Runnable
{
	private boolean built = false;

	private final TavVisualizationWidth initX = new TavVisualizationWidth();
	private final TavVisualizationHeight initY = new TavVisualizationHeight();

	private final Canvas canvas;

	private int threshold = -80;
	private int numBands = 200;

	float[] bartopArray = new float[numBands];
	private float[] magnitudes;
	private float[] prevMagnitudes = new float[numBands];

	private final ArrayBlockingQueue<float[]> bartopStack = new ArrayBlockingQueue<float[]> (
			2000);

	private GraphicsContext gc;
	private GraphicsContext gc2;

	private Number barW;

	private Color[] colors = { Color.DARKVIOLET, Color.WHITE, Color.DARKORANGE,
			Color.THISTLE, Color.BLACK, Color.ANTIQUEWHITE, Color.BLUE,
			Color.BLACK, Color.DARKOLIVEGREEN };

	// three paint Stop objects
	private final Stop[] stops = new Stop[] { new Stop (0, colors[0]),
			new Stop (0.1, Color.BLACK), new Stop (.15, colors[0]),
			new Stop (0.2, Color.BLACK), new Stop (.25, colors[1]),
			new Stop (.3, Color.BLACK) };

	private final Stop[] stopsInner = new Stop[] { new Stop (0, Color.BLACK),
			new Stop (0.2, colors[2]), new Stop (.5, colors[3]),
			new Stop (.8, colors[4]), new Stop (1, colors[5]) };

	private final Stop[] stopsInner2 = new Stop[] { new Stop (.2, colors[6]),
			new Stop (.5, colors[7]), new Stop (.9, colors[8]) };

	// three paint LinearGradients using the three paint Stop objects
	private LinearGradient lg1 = new LinearGradient (0, 0, 0, .5, true,
			CycleMethod.REFLECT, stops);

	private LinearGradient lg2 = new LinearGradient (0, 0, 0, .5, true,
			CycleMethod.REFLECT, stopsInner);

	private LinearGradient lg3 = new LinearGradient (0, 0, 0, .5, true,
			CycleMethod.NO_CYCLE, stopsInner2);

	private int testVar1;

	private int testVar2;

	public RenderTestVis2Frame(Canvas canvas, GraphicsContext gc)
	{
		this.canvas = canvas;
		this.gc = gc;
		initX.setValue (1200);
		barW = initX.intValue() / numBands;

		// set initial line width for the graphics context
		gc.setLineWidth (barW.doubleValue());

		// make sure prevMagnitudes are not greater than
		// the maximum of any future magnitude (i.e. 0)
		for (int i = 0; i < numBands; i++)
			prevMagnitudes[i] = threshold * 2;

		bartopStack.add (bartopArray);
	}

	/**
	 * Update the magnitudes for the next frame
	 * 
	 * @param magnitudes
	 */
	public void updateMagnitudes(float[] magnitudes)
	{
		this.magnitudes = magnitudes;
	}

	@Override
	public void run()
	{
		if ( !built)
			return;

		if ( (bartopStack.size() > 1900))
		{
			bartopStack.remove();
		}
		final float conversion = initY.floatValue() / (2 * (threshold * 2));
		final float canvasCenterY = initY.floatValue() / 2.0f - 30;
		final float canvasCenterX = initX.floatValue() / 2.0f;

		gc.fillRect (0, 0, initX.doubleValue(), initY.doubleValue());

		/** Clear the entire frame and paint one frame onto the canvas */
		gc.setFill (Color.BLACK);
		gc.setStroke (Color.YELLOWGREEN);
		for (int i = 0; i < numBands; i++)
		{
			if (i < numBands / 10)
				// magnitudes[i] += (5.0 - (double) i / 5);
				magnitudes[i] -= ( (numBands / 10) - i);

			final float xPos = (barW.floatValue() + 1) * i;
			final float octalMagnitude = magnitudes[i] + magnitudes[2 * i];
			final boolean louderThanBefore = octalMagnitude > (prevMagnitudes[i]);

			// POSITIONING "CURRENT"
			final float currentTop = bartopStack.peek()[i];
			final float currentBottom = (float) (canvas.getHeight() - currentTop) - 60;
			final float currentRight = xPos + canvasCenterX;
			final float currentLeft = canvasCenterX - xPos;
			// this magnitude greater than previous?
			if (louderThanBefore)
			{
				prevMagnitudes[i] = octalMagnitude;
			} else
			{ // gc.translate(0, -1);
				if (prevMagnitudes[i] - 2 >= 2 * threshold)
					prevMagnitudes[i] = prevMagnitudes[i] - 2;
				magnitudes[i] = prevMagnitudes[i];
				gc.setStroke (i % 2 == 0 ? lg2 : lg3);

			}

			// POSITIONING "FUTURE"
			float futureTop = (prevMagnitudes[i]) * conversion;
			if (canvasCenterY - futureTop < .5)
			{
				futureTop = canvasCenterY;
			} else
			{

				if ( (canvasCenterY - futureTop > -threshold
						* new Double (testVar1) / 10.0))
				{

					gc2 = canvas.getGraphicsContext2D();
					gc2.setLineWidth (barW.doubleValue());
					gc2.strokeLine (currentLeft, currentBottom / 2 - testVar2,
							currentLeft, currentBottom / 2 - 15 - testVar2);
					gc2.strokeLine (currentRight, currentBottom / 2 - testVar2,
							currentRight, currentBottom / 2 - 15 - testVar2);
				}
			}

			if (louderThanBefore)
			{
				gc2 = canvas.getGraphicsContext2D();
				gc2.setStroke (lg1);
				gc2.setLineWidth (Math.abs ( (barW.doubleValue() / 2) % 3));

				gc2.strokeLine (xPos + canvasCenterX, currentBottom, xPos
						+ canvasCenterX, bartopStack.peek()[i]);
				gc2.strokeLine (canvasCenterX - xPos, currentBottom,
						canvasCenterX - xPos, bartopStack.peek()[i]);

			} else
			{
				gc = canvas.getGraphicsContext2D();
				gc.setLineWidth (barW.doubleValue());

			}

			bartopArray[i] = futureTop;

			gc.strokeLine (xPos + canvasCenterX, currentBottom, xPos
					+ canvasCenterX, bartopStack.peek()[i]);
			gc.strokeLine (canvasCenterX - xPos, currentBottom, canvasCenterX
					- xPos, bartopStack.peek()[i]);
		}
		bartopStack.add (bartopArray);
	}

	public TavVisualizationWidth widthProperty()
	{
		return initX;
	}

	public TavVisualizationHeight heightProperty()
	{
		return initY;
	}

	public void setBarWidth(Number width)
	{
		barW = width.doubleValue();
	}

	public int getNumBands()
	{
		return this.numBands * 2;
	}

	public int getThreshold()
	{
		return this.threshold;
	}

	public void setThreshold(int threshold)
	{
		this.threshold = threshold;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public void setBuilt(boolean b)
	{
		this.built = b;
	}

	private void changeGradient1Color(int index, Color color)
	{
		if (index == 0)
		{
			stops[0] = new Stop (0.0, color);
		} else if (index == 1)
		{
			stops[1] = new Stop (1.0, color);
		}
		// reset the gradient containing the given color
		lg1 = new LinearGradient (0, 0, 0, .5, true, CycleMethod.REFLECT, stops);
	}

	private void changeGradient2Color(int index, Color color)
	{
		if (index == 0)
		{
			stopsInner[index] = new Stop (0, color);
		} else if (index == 1)
		{
			stopsInner[index] = new Stop (.1, color);
		} else if (index == 2)
		{
			stopsInner[index] = new Stop (.8, color);
		} else if (index == 3)
		{
			stopsInner[index] = new Stop (1, color);
		}
		// reset the gradient containing the given color
		lg2 = new LinearGradient (0, 0, 0, .5, true, CycleMethod.REFLECT,
				stopsInner);
	}

	private void changeGradient3Color(int index, Color color)
	{
		if (index == 0)
		{
			stopsInner2[index] = new Stop (.2, color);
		} else if (index == 1)
		{
			stopsInner2[index] = new Stop (.5, color);
		} else if (index == 2)
		{
			stopsInner2[index] = new Stop (.9, color);
		}
		lg3 = new LinearGradient (0, 0, 0, .5, true, CycleMethod.NO_CYCLE,
				stopsInner2);
	}

	public void changeColor(int index, Color color)
	{
		this.colors[index] = color;
		if (index < 2)
		{
			changeGradient1Color (index, color);

		} else if (index < 6)
		{
			changeGradient2Color (index - 2, color);

		} else
		{
			changeGradient3Color (index - 6, color);
		}

	}

	public Color getCustomColor(int index)
	{
		return colors[index];
	}

	public void setTestVar1(int intValue)
	{
		this.testVar1 = intValue;

	}

	public void setTestVar2(int intValue)
	{
		this.testVar2 = intValue;
	}
}
