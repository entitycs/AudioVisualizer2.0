package application.mediaPlayer.interfacing;

import application.event.TavEndOfMediaEventHandler;
import application.listener.TavAudioSpectrumListener;

public interface TavMediaPlayer
{

	public  Object getAudioEqualizer();

	public  double getAudioSpectrumInterval();

	public  int getAudioSpectrumNumBands();

	public  int getAudioSpectrumThreshold();

	public  Double getCurrentTime();

	public  String getMediaLocation();

	public  Object getPlayer();

	public  void pauseTrack();

	public  void playlistReady();

	public  void playTrack();

	public  void stopTrack();
	
	public  boolean setAudioSpectrumInterval(double interval);

	public  void setAudioSpectrumListener(TavAudioSpectrumListener audioSpectrumListener);
	
	public  boolean setAudioSpectrumNumBands(int numBands);

	public  boolean setAudioSpectrumThreshold(int threshold);

	public  void setMediaLocation(String MediaLocation);
	
	public  void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler);
}
