package audiovisualizer.fx.mediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import application.mediaPlayer.TavMetaData;
import application.mediaPlayer.event.TavEndOfMediaEventHandler;
import application.mediaPlayer.interfacing.TavMediaPlayer;
import application.mediaPlayer.listener.TavAudioSpectrumListener;
import application.mediaPlayer.listener.TavMetaDataListener;

public class TavMediaPlayerFX implements TavMediaPlayer, Runnable
{

	// The media player in use by this media player wrapper
	private MediaPlayer mediaPlayer;
	
	private String metadata = "";

	private TavEndOfMediaEventHandler endOfMediaHandler;

	private TavMetaDataListener metaDataListener;

	/**
	 * Default constructor -- used in generic instantiation You may add another
	 * constructor, but only this one will be used for generic type building
	 */
	public TavMediaPlayerFX()
	{

	}

	/**
	 * Build this media player
	 * 
	 * @param mediaLocation
	 *            - the location of the media to play
	 */

	public void build(String mediaLocation)
	{

		File file = new File (mediaLocation);

		try
		{
			mediaLocation = file.toURI().toURL().toExternalForm();

		} catch (MalformedURLException e)
		{
			System.err
					.println ("There was an error in translating the absolute media location to a URL (JavaFX)");
			e.printStackTrace();
		}

		Media media = new Media (mediaLocation);

		this.mediaPlayer = new MediaPlayer (media);

		this.mediaPlayer.getAudioEqualizer().setEnabled (true);

		this.mediaPlayer.setOnError (new Runnable()
		{
			@Override
			public void run()
			{

			}
		});

		// Configure the wrapped media player for playing the given media

		// Use the application.manager's passed in event handler to manage all
		// events
		this.mediaPlayer.setOnPlaying (this);

		this.mediaPlayer.setOnEndOfMedia (this.endOfMediaHandler);

	}

	@Override
	public AudioEqualizer getAudioEqualizer()
	{
		return this.mediaPlayer.getAudioEqualizer();
	}

	@Override
	public double getAudioSpectrumInterval()
	{
		return this.mediaPlayer.getAudioSpectrumInterval();
	}

	@Override
	public int getAudioSpectrumNumBands()
	{
		return this.mediaPlayer.getAudioSpectrumNumBands();
	}

	@Override
	public int getAudioSpectrumThreshold()
	{
		return this.mediaPlayer.getAudioSpectrumThreshold();
	}

	@Override
	public Double getCurrentTime()
	{
		return new Double (this.mediaPlayer.getCurrentTime().toSeconds());
	}

	@Override
	public String getMediaLocation()
	{
		return null;
	}

	@Override
	public MediaPlayer getPlayer()
	{
		return this.mediaPlayer;
	}

	@Override
	public void pauseTrack()
	{
		this.mediaPlayer.pause();

	}

	@Override
	public void playTrack()
	{
		if (this.mediaPlayer != null)
		{
			this.mediaPlayer.play();
		}
	}

	@Override
	public void playlistReady()
	{
		stopTrack();

		if (this.mediaPlayer != null)
		{
			this.mediaPlayer.play();
		}
	}

	@Override
	public boolean setAudioSpectrumInterval(double interval)
	{
		this.mediaPlayer.setAudioSpectrumInterval (interval);
		return true;
	}

	@Override
	public boolean setAudioSpectrumNumBands(int numBands)
	{
		this.mediaPlayer.setAudioSpectrumNumBands (numBands);
		return true;
	}

	@Override
	public boolean setAudioSpectrumThreshold(int threshold)
	{
		this.mediaPlayer.setAudioSpectrumThreshold (threshold);
		return true;
	}

	@Override
	public void setMediaLocation(String mediaLocation)
	{
		if (this.mediaPlayer != null)
			if (this.mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED)
				return;

		build (mediaLocation);
	}

	@Override
	public void stopTrack()
	{
		if (this.mediaPlayer != null)
			this.mediaPlayer.stop();
	}

	@Override
	public void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler)
	{
		// TODO Auto-generated method stub
		this.mediaPlayer.setOnEndOfMedia (tavEndOfMediaEventHandler);
	}

	@Override
	public void setAudioSpectrumListener(
			TavAudioSpectrumListener audioSpectrumListener)
	{
		this.mediaPlayer.setAudioSpectrumListener (audioSpectrumListener);
	}

	@Override
	public TavMetaData getMetaData()
	{
		return null;
	}

	@Override
	public void dispose()
	{
		mediaPlayer.stop ();
		System.out.println(mediaPlayer.statusProperty ());
	}

	@Override
	public void setMetaDataListener(TavMetaDataListener tavMetaDataListener)
	{
		this.metaDataListener = tavMetaDataListener;
	}

	@Override
	public void run()
	{
		for (String key : new String[] { "artist", "title", "album",
				"genre" })
		{
			if (this.mediaPlayer.getMedia().getMetadata()
					.containsKey (key))
			{
				this.metadata += "\n"
						+ key
						+ ": "
						+ this.mediaPlayer.getMedia().getMetadata()
								.get (key);
			}
		}
		this.metaDataListener.metaDataUpdate (new TavMetaData(this.metadata));
	}
}