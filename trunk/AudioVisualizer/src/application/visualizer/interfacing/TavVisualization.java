package application.visualizer.interfacing;

import javafx.scene.Scene;
import application.mediaPlayer.TavMetaData;

public interface TavVisualization
{

	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset);

	/**
	 * (Unimplemented) Get the runtime in animating a frame. This may be used
	 * later (much later) for benchmarking.
	 * 
	 * @return
	 */
	// public double getRuntime();

	/**
	 * 
	 * @param scene
	 *            is the Scene on which the visualization is to appear
	 * @param videoWidth
	 *            is the videoWidth (unimplemented
	 */
	public void build(Scene scene, int videoWidth);

	public double getInterval();

	public int getNumBands();

	public int getThreshold();

	public void setWidth(double width);

	public void setHeight(double height);

	public void setMetaData(TavMetaData metaData);

	public TavVisualizationWidth widthProperty();

	public TavVisualizationHeight heightProperty();

	public boolean isCustomizable();

}
