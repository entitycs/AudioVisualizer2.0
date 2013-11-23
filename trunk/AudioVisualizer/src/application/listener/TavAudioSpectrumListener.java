package application.listener;

import javafx.scene.media.AudioSpectrumListener;

public interface TavAudioSpectrumListener extends AudioSpectrumListener
{
	/**
	 * Constructor for magnitudes as a short array
	 * @param timestamp
	 * @param duration
	 * @param magnitudes
	 * @param phases
	 * @param offset
	 */
	public void spectrumDataUpdate(double timestamp, double duration,
			float[] magnitudes, short[] samples);
	
}
