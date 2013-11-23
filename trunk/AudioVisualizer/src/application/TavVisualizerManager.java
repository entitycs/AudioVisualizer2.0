package application;

import visualizer.visualizerFX.TavVisualizer;
import visualizer.visualizerFX.TavVisualizerFX;
import visualizer.visualizerFX.visualizations.TestVis;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import application.listener.PlaylistReadyListener;
import application.listener.TavAudioSpectrumListener;
import application.visualizer.interfacing.*;

public class TavVisualizerManager implements PlaylistReadyListener,
		TavAudioSpectrumListener, VisualizerControlsEventListener
{
	// Visualizer
	private TavVisualizer visualizer = null;
	private TavVisualization visualization = null;
	Stage visualizerStage = null;

	// Defaults for height and width (settings enabled)
	private double height = 500;
	private double width = 689;

	public TavVisualizerManager()
	{
		init();
	}

	private void init(){
		// default standalone visualizer
		// does nothing
		// when mediaPlayerFX is not being used
		this.visualizer = new TavVisualizerFX();

		
		// test setting for a second time
		// (visualization set once as default in TavVisualizerFX)
		AnchorPane visualizationPane = new AnchorPane();
		AnchorPane controlPane = new AnchorPane();
		visualizerStage = new Stage();
		visualizerStage.setScene(new Scene(visualizationPane, this.width,
				this.height));
		this.visualization = new TestVis(visualizerStage, controlPane);
		visualizer.setVisualization(visualization);
	}
	
	/**
	 * Set Visualizer
	 * 
	 * @param visualizer
	 */
	public void setVisualizer(TavVisualizer visualizer)
	{
		this.visualizer = visualizer;
	}

	/**
	 * Get Visualizer
	 * 
	 * @return
	 */
	public TavVisualizer getVisualizer()
	{
		return this.visualizer;
	}

	@Override
	public void playlistReady()
	{
		
		// If a combo player (media + visualizer) is being used
		if (TavApplicationManager.getInstance().getMediaPlayerManager().usingComboPlayer())
			
			// do nothing (return)
			return;
		
		
		// initialize the visualizer and visualization on every playlistReady call
		if (this.visualization == null)
			init();

		// this will display the visualization again if it was closed
		if (! visualizerStage.isShowing()) // TODO setting
		{
			visualizerStage.show();
		}
		
		visualization.build(visualizerStage.getScene(), 0);
	}

	/**
	 *  Spectrum data given float[] magnitudes
	 *  
	 *  
	 */
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset)
	{
		if (!TavApplicationManager.getInstance().getMediaPlayerManager()
				.usingComboPlayer())
			this.visualization.spectrumDataUpdate(timestamp, duration,
					magnitudes, phases, offset);
	}

	public void setWidth(double width)
	{
		// TODO Auto-generated method stub
		this.width = width;
		if (this.visualizer != null)
			this.visualizer.setWidth(width);
	}

	public void setHeight(double height)
	{
		// TODO Auto-generated method stub
		this.height = height;
		if (this.visualizer != null)
			this.visualizer.setHeight(height);
	}

	public void updateVisualizer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, short[] samples)
	{
		// TODO Auto-generated method stub
		
	}

}
