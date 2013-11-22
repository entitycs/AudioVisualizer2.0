package application;

public class TavSpectrumDataUpdater implements Runnable {

	private double timestamp;
	private float[] magnitudes;
	private double duration;
	private float[] phases;
	private double offset;

	/**
	 * Constructor for magnitudes as a float array
	 * @param timestamp
	 * @param duration
	 * @param magnitudes
	 * @param phases
	 */
	TavSpectrumDataUpdater(double timestamp, double duration,
			float[] magnitudes, float[] phases, double offset){
		this.timestamp = timestamp;
		this.duration = duration;
		this.magnitudes = magnitudes;
		this.phases = phases;
		this.offset = offset;
	}
	
	/**
	 * Constructor for magnitudes as a short array
	 * @param timestamp
	 * @param duration
	 * @param magnitudes
	 * @param phases
	 */
	TavSpectrumDataUpdater(double timestamp, double duration,
			float[] magnitudes, short[] samples, double offset){
		float[] floatPhases = new float[samples.length];

		for (int i = 0; i < magnitudes.length; i++)
			floatPhases[i] = (float) samples[i];
		
		this.timestamp = timestamp;
		this.duration = duration;
		
		this.magnitudes = magnitudes;
		this.phases = floatPhases;
		
		this.offset = offset;
		
	}
	
	@Override
	public void run() {
		TavApplicationManager.getInstance().getVisualizerManager().spectrumDataUpdate(timestamp, duration, magnitudes, phases, offset);
		TavApplicationStage.getInstance().consoleFloatArray(magnitudes);
	}

}
