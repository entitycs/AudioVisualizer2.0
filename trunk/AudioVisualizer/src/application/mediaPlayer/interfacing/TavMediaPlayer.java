package application.mediaPlayer.interfacing;

import application.mediaPlayer.TavMetaData;
import application.mediaPlayer.event.TavEndOfMediaEventHandler;
import application.mediaPlayer.listener.TavAudioSpectrumListener;
import application.mediaPlayer.listener.TavMetaDataListener;

public interface TavMediaPlayer
{
	/**
	 * Get the AudioEqualizer provided by the media player in use.
	 * 
	 * @return the media player equalizer object or null if the media player in
	 *         use could not provide it.
	 */
	public Object getAudioEqualizer();

	/**
	 * Get the audio spectrum interval in use by the media player.
	 * 
	 * @return the interval in between audio spectrum updates or 0 if no
	 *         interval was given by the media player in use.
	 */
	public double getAudioSpectrumInterval();

	/**
	 * Get the number of bands in the audio spectrum.
	 * 
	 * @return the number of bands in the audio spectrum or 0 if no bands were
	 *         given by the media player in use.
	 */
	public int getAudioSpectrumNumBands();

	/**
	 * Get the threshold for spectrum data updates.
	 * 
	 * @return the audio spectrum threshold in use by the current media player
	 *         or 0 if not provided.
	 */
	public int getAudioSpectrumThreshold();

	/**
	 * Get the current time in seconds (media progress time).
	 * 
	 * @return the current time (progress) in the media loaded, or 0 if no media
	 *         is being played or if the media player in use otherwise does not
	 *         provide a time.
	 */
	public Double getCurrentTime();

	/**
	 * Get the media location in effect for the media player.
	 * 
	 * @return the current media location from the perspective of the media
	 *         player in use.
	 */
	public String getMediaLocation();

	/**
	 * Get the internal media player.
	 * 
	 * @return the 'wrapped' media player child or the top level media player
	 *         object itself if no inner implementation is used.
	 */
	public Object getPlayer();

	/**
	 * Pause the current media.
	 */
	public void pauseTrack();

	/**
	 * Inform the media player that the playlist is ready to be played from.
	 */
	public void playlistReady();

	/**
	 * Play the current media.
	 */
	public void playTrack();

	/**
	 * Stop the current media.
	 */
	public void stopTrack();

	/**
	 * Set the audio spectrum interval for the media player in use.
	 * 
	 * @param interval
	 *            is the new interval to be set.
	 * @return false if this feature is not a feature of the media player in
	 *         use.
	 */
	public boolean setAudioSpectrumInterval(double interval);

	/**
	 * Set the audio spectrum listener.
	 * 
	 * @param audioSpectrumListener
	 *            is the object to receive audio spectrum updates.
	 */
	public void setAudioSpectrumListener(
			TavAudioSpectrumListener audioSpectrumListener);

	/**
	 * Set the number of audio spectrum bands for the media player in use.
	 * 
	 * @param numBands
	 *            is the number of bands the media player in use shall provide
	 *            in audio spectrum updates.
	 * 
	 * @return false if this feature is not a feature of the media player in
	 *         use.
	 */
	public boolean setAudioSpectrumNumBands(int numBands);

	/**
	 * Set the audio spectrum threshold for the media player in use.
	 * 
	 * @param threshold
	 * @return false if this feature is not a feature of the media player in
	 *         use.
	 */
	public boolean setAudioSpectrumThreshold(int threshold);

	/**
	 * Set the media location for the media player in use.
	 * 
	 * @param MediaLocation
	 *            is the location of the media to be played.
	 */
	public void setMediaLocation(String MediaLocation);

	/**
	 * Set the end of media handler to be used by any and all media players.
	 * 
	 * @param tavEndOfMediaEventHandler
	 *            is the object to run when the media player has reached the end
	 *            of the current media.
	 */
	public void setOnEndOfMedia(
			TavEndOfMediaEventHandler tavEndOfMediaEventHandler);

	/**
	 * (not fully implemented) Get the metadata of the current media from the
	 * media player.
	 * 
	 * @return the metadata associated with the current media or null if no
	 *         metadata could be extracted.
	 */
	public TavMetaData getMetaData();

	/**
	 * Dispose of the media player in use.
	 */
	public void dispose();

	/**
	 * Set the listener for the event that metadata has been read for media
	 * being played.
	 * 
	 * @param tavMetaDataListener
	 */
	void setMetaDataListener(TavMetaDataListener tavMetaDataListener);

}
