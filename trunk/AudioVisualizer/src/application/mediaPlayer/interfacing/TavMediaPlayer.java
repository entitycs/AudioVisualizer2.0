package application.mediaPlayer.interfacing;

public interface TavMediaPlayer
{

	public abstract Object getAudioEqualizer();

	public abstract double getAudioSpectrumInterval();

	public abstract int getAudioSpectrumNumBands();

	public abstract int getAudioSpectrumThreshold();

	public abstract double getCurrentTime();

	public abstract String getMediaLocation();

	public abstract Object getPlayer();

	public abstract void pauseTrack();

	public abstract void playlistReady();

	public abstract void playTrack();

	public abstract void setAudioSpectrumInterval(double interval);

	public abstract void setAudioSpectrumNumBands(int numBands);

	public abstract void setAudioSpectrumThreshold(int threshold);

	public abstract void setMediaLocation(String MediaLocation);
//
//	public abstract void setAudioSpectrumLlistener(
//			TavAudioSpectrumListener spectrumListener);

	public abstract void stopTrack();

	public abstract void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler);

	public abstract void prevTrack();
	
	public abstract void nextTrack();

	public abstract void setAudioSpectrumListener(TavAudioSpectrumListener audioSpectrumListener);

}
