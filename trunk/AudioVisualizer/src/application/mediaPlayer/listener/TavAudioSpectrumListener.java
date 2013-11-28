package application.mediaPlayer.listener;

import javafx.scene.media.AudioSpectrumListener;

public interface TavAudioSpectrumListener extends AudioSpectrumListener
{
	/**
	 * Constructor for magnitudes as a short array. For floats, see
	 * AudioSpectrumListener.
	 * 
	 * @param timestamp
	 * @param duration
	 * @param magnitudes
	 * @param samples
	 */
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, short[] samples);

}
