package visualizer.visualizerFX;

import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import application.mediaPlayer.interfacing.TavAudioSpectrumListener;
import application.mediaPlayer.interfacing.TavEndOfMediaEventHandler;
import application.mediaPlayer.interfacing.TavMediaPlayer;

public class TavMediaPlayerFX implements TavMediaPlayer
{

	// The media player in use by this media player wrapper
	private MediaPlayer mediaPlayer;

	private String mediaLocation;

	private TavEndOfMediaEventHandler endOfMediaHandler;

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
		Media media = new Media(mediaLocation.replace(" ", "%20"));

		this.mediaPlayer = new MediaPlayer(media);

		this.mediaPlayer.getAudioEqualizer().setEnabled(true);

		this.mediaPlayer.setOnError(new Runnable()
		{
			@Override
			public void run()
			{

			}
		});

		// Configure the wrapped media player for playing the given media

		// Use the application.manager's passed in event handler to manage all
		// events
		this.mediaPlayer.setOnPlaying(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println(mediaPlayer.getMedia().getMetadata());
			}
		});
		
		this.mediaPlayer.setOnEndOfMedia(this.endOfMediaHandler);

	}

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

	public double getCurrentTime()
	{
		return this.mediaPlayer.getCurrentTime().toSeconds();
	}

	@Override
	public String getMediaLocation()
	{
		return null;
	}

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
		if (this.mediaPlayer == null){
			
			if ( mediaLocation != null)
				playlistReady();
		}
		else
			this.mediaPlayer.play();
	}

	@Override
	public void playlistReady()
	{
		stopTrack(); System.err.println("PLAYLIST READY (FX)");
		if (this.mediaPlayer != null)
		{
			this.mediaPlayer.play();
		}
	}

	public void setAudioSpectrumInterval(double interval)
	{
		this.mediaPlayer.setAudioSpectrumInterval(interval);
	}

	@Override
	public void setAudioSpectrumListener(TavAudioSpectrumListener audioSpectrumListener)
	{
		this.mediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
	}

	public void setAudioSpectrumNumBands(int numBands)
	{
		this.mediaPlayer.setAudioSpectrumNumBands(numBands);
	}

	public void setAudioSpectrumThreshold(int threshold)
	{
		this.mediaPlayer.setAudioSpectrumThreshold(threshold);
	}

	@Override
	public void setMediaLocation(String mediaLocation)
	{
		stopTrack();
		this.mediaLocation = mediaLocation;
		build(mediaLocation);
	}

	@Override
	public void stopTrack()
	{
		if (this.mediaPlayer != null)
			this.mediaPlayer.stop();
	}

	@Override
	public void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler) {
		// TODO Auto-generated method stub
		this.endOfMediaHandler = tavEndOfMediaEventHandler;
	}

	@Override
	public void prevTrack()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTrack()
	{
		// TODO Auto-generated method stub
		
	}

}