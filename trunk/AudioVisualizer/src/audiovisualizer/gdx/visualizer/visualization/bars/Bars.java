package audiovisualizer.gdx.visualizer.visualization.bars;

import audiovisualizer.gdx.AudioVisualizer;
import audiovisualizer.gdx.visualizer.visualization.Visualization;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Bars visualizes the spectrum data using solid bars.
 * 
 * @author Eric
 */
public abstract class Bars extends Visualization
{
	protected Texture colors;

	protected float[] topValues = new float[AudioVisualizer.SAMPLE_SIZE];
	protected float[] maxValues = new float[AudioVisualizer.SAMPLE_SIZE];

	protected int NUM_BARS = 128;
	protected int numSamplesPerBar = (spectrum.length / NUM_BARS);

	protected float barWidth = ((float) AudioVisualizer.WIDTH / (float) NUM_BARS);

	protected float FALLING_SPEED = (1.0f / 3.0f);

	/**
	 * Constructor
	 * 
	 * @param batch
	 * @param spectrum
	 */
	public Bars(SpriteBatch batch, float[] spectrum)
	{
		super (batch, spectrum);

		colors = new Texture (new FileHandle ("res/colors-borders.png"));
	}

	/**
	 * Visualizes spectrum data n the form of bars and draws it to the screen.
	 */
	public abstract void visualize();

	/**
	 * Draws a single bar to the screen.
	 * 
	 * @param i
	 * @param barNum
	 */
	public void drawBar(int i, int barNum)
	{
		if (avg (barNum, numSamplesPerBar) > maxValues[barNum])
		{
			maxValues[barNum] = avg (barNum, numSamplesPerBar);
		}

		if (avg (barNum, numSamplesPerBar) > topValues[barNum])
		{
			topValues[barNum] = avg (barNum, numSamplesPerBar);
		}

		// drawing spectrum (in blue)
		batch.draw (colors, i * barWidth, 0, barWidth,
				scale (avg (barNum, numSamplesPerBar)), 0, 0, 16, 5, false,
				false);
		// drawing top values (in red)
		batch.draw (colors, i * barWidth, scale (topValues[barNum]), barWidth,
				4, 0, 5, 16, 5, false, false);
		// drawing max values (in yellow)
		batch.draw (colors, i * barWidth, scale (maxValues[barNum]), barWidth,
				2, 0, 10, 16, 5, false, false);

		topValues[barNum] -= FALLING_SPEED;
	}

	/**
	 * Scales a bar's value (x) to fit the screen.
	 * 
	 * @param x
	 * @return scaled x
	 */
	private float scale(float x)
	{
		return x / 256 * AudioVisualizer.HEIGHT * 1.5f;
	}

	/**
	 * Computes the average sample data for a certain bar.
	 * 
	 * @param barNum
	 * @param numSamplesPerBar
	 * @return average sample data for a certain bar
	 */
	private float avg(int barNum, int numSamplesPerBar)
	{
		int sum = 0;

		for (int i = 0; i < numSamplesPerBar; i++)
		{
			sum += spectrum[barNum + i];
		}

		return (float) (sum / numSamplesPerBar);
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < AudioVisualizer.SAMPLE_SIZE; i++)
		{
			spectrum[i] = 0;
			topValues[i] = 0;
			maxValues[i] = 0;
		}
	}
}
