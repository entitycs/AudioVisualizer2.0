package application;

import stage.playlist.listener.TavPlaylistReadyListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import application.mediaPlayer.interfacing.TavComboMediaPlayer;
import application.mediaPlayer.listener.TavAudioSpectrumListener;
import application.visualizer.interfacing.TavVisualizationCustomizable;
import application.visualizer.interfacing.TavVisualizer;
import application.visualizer.listener.TavVisualizerControlsListener;
import audiovisualizer.fx.visualizer.TavVisualizerFX;

public class TavVisualizerManager implements TavPlaylistReadyListener,
		TavAudioSpectrumListener, TavVisualizerControlsListener
{
	// Visualizer
	private TavVisualizer visualizer = null;

	// Defaults for height and width (settings enabled)
	private double height = 500;
	private double width = 689;
	private int colorToChangeIndex = -1;

	private GridPane visualizationControls;

	public TavVisualizerManager()
	{
		init (-1);
	}

	private void init(int i)
	{
		// default standalone visualizer
		// does nothing
		// when mediaPlayerFX is not being used
		//if (this.visualizer != null )
		//	return;

		if (i < 0)
			this.visualizer = new TavVisualizerFX ();
		else
			this.visualizer = new TavVisualizerFX (i);

		this.visualizer.setHeight (this.height);
		this.visualizer.setWidth (this.width);
	}

	/**
	 * Set Visualizer
	 * 
	 * @param visualizer
	 *            is the visualizer to be used by the application.
	 */
	public void setVisualizer(TavVisualizer visualizer)
	{
		this.visualizer = visualizer;
	}

	/**
	 * Get Visualizer
	 * 
	 * @return the current visualizer in use by the application.
	 */
	public TavVisualizer getVisualizer()
	{
		return this.visualizer;
	}

	@Override
	public void playlistReady()
	{

		// If a combo player (media + visualizer) is being used
		if (TavApplicationManager.getInstance ().getMediaPlayerManager ()
				.usingComboPlayer ())
		{

			// do nothing (return)
			return;
		}

		// initialize the visualizer and visualization on every playlistReady
		// call
		if (this.visualizer == null)
			init (-1);

		// this will display the visualization again if it was closed
		if ( !visualizer.isShowing ()) // TODO setting
		{
			visualizer.show ();
		}

		// build involves setting listeners and event handlers
		visualizer.build ();
	}

	/**
	 * Spectrum data given float[] magnitudes
	 * 
	 * 
	 */
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset)
	{
		if ( !TavApplicationManager.getInstance ().getMediaPlayerManager ()
				.usingComboPlayer ())
			this.visualizer.getVisualization ().spectrumDataUpdate (timestamp,
					duration, magnitudes, phases, offset);
	}

	public void setWidth(double width)
	{
		// TODO Auto-generated method stub
		this.width = width;
		if (this.visualizer != null)
			this.visualizer.setWidth (width);
	}

	public void setHeight(double height)
	{
		// TODO Auto-generated method stub
		this.height = height;
		if (this.visualizer != null)
			this.visualizer.setHeight (height);
	}

	public void updateVisualizer()
	{
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

	public void mediaPlayerReady()
	{
		this.visualizer.getVisualization ().setMetaData (
				TavApplicationManager.getInstance ().getMediaPlayerManager ()
						.getMediaPlayer ().getMetaData ());
	}

	// deprecated?
	@Override
	public void changed(ObservableValue<? extends Number> arg0, Number arg1,
			Number arg2)
	{
	}

	@Override
	public void setColorItem(Number index)
	{
		this.colorToChangeIndex = index.intValue ();
	}

	@Override
	public void setVisualizationItem(Number index)
	{
		// If a combo player (media + visualizer) is being used

		if (TavApplicationManager.getInstance ().getMediaPlayerManager ()
				.usingComboPlayer ())
		{
			((TavComboMediaPlayer) TavApplicationManager.getInstance ()
					.getMediaPlayerManager ().getMediaPlayer ())
					.setVisualizationIndex (index);
		} else
		// media player and visualizer are disjoint
		{
			this.visualizer.setVisualizationIndex (index.intValue ());
			init(index.intValue ());
			initVisualizationControls();
			this.playlistReady ();
			
		}

	}

	@Override
	public void setColorItemColor(Color value)
	{
		// if no color selected
		if (this.colorToChangeIndex < 0)
			return;

		// if using combo player
		if (TavApplicationManager.getInstance ().getMediaPlayerManager ()
				.usingComboPlayer ())
		{
			if ( ((TavComboMediaPlayer) TavApplicationManager.getInstance ()
					.getMediaPlayerManager ().getMediaPlayer ())
					.getVisualization ().isCustomizable ())
			{
				TavVisualizationCustomizable t = (TavVisualizationCustomizable) ((TavComboMediaPlayer) TavApplicationManager
						.getInstance ().getMediaPlayerManager ()
						.getMediaPlayer ()).getVisualization ();
				t.changeColor (this.colorToChangeIndex, value);
			}
			return;
		}

		// if media player and visualizer are disjoint
		if (this.visualizer.getVisualization ().isCustomizable ())
		{
			TavVisualizationCustomizable t = (TavVisualizationCustomizable) this.visualizer
					.getVisualization ();
			t.changeColor (this.colorToChangeIndex, value);
		}
	}

	@Override
	public Color getColorAtIndex(Number arg2)
	{
		// if no color selected
		if (this.colorToChangeIndex < 0)
			return null;

		// if using combo player
		if (TavApplicationManager.getInstance ().getMediaPlayerManager ()
				.usingComboPlayer ())
		{
			if ( ((TavComboMediaPlayer) TavApplicationManager.getInstance ()
					.getMediaPlayerManager ().getMediaPlayer ())
					.getVisualization ().isCustomizable ())
			{
				TavVisualizationCustomizable t = (TavVisualizationCustomizable) ((TavComboMediaPlayer) TavApplicationManager
						.getInstance ().getMediaPlayerManager ()
						.getMediaPlayer ()).getVisualization ();
				return t.getCustomColor (arg2.intValue ());
			}
			return null;
		}
		if (this.visualizer.getVisualization ().isCustomizable ())
		{
			TavVisualizationCustomizable t = (TavVisualizationCustomizable) this.visualizer
					.getVisualization ();
			return t.getCustomColor (arg2.intValue ());
		}
		return null;
	}

	@Override
	public void setVisualizationControls(GridPane customizeVidSliders)
	{
		this.visualizationControls = customizeVidSliders;
		initVisualizationControls();
	}
	
	private void initVisualizationControls()
	{ 
		// if using combo player
		if (TavApplicationManager.getInstance ().getMediaPlayerManager ()
				.usingComboPlayer ())
		{
//			if ( ((TavComboMediaPlayer) TavApplicationManager.getInstance ()
//					.getMediaPlayerManager ().getMediaPlayer ())
//					.getVisualization ().isCustomizable ())
//			{
//				TavVisualizationCustomizable t = (TavVisualizationCustomizable) ((TavComboMediaPlayer) TavApplicationManager
//						.getInstance ().getMediaPlayerManager ()
//						.getMediaPlayer ()).getVisualization ();
//				t.setCustomizeLevels (this.visualizationControls);
//			}
		}
		if ( this.visualizer.getVisualization ().isCustomizable ())
		{
		TavVisualizationCustomizable t = (TavVisualizationCustomizable) this.visualizer
				.getVisualization ();
		t.setCustomizeLevels (this.visualizationControls);
		}
	}
}
