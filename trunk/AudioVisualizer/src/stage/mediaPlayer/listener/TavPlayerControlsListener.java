package stage.mediaPlayer.listener;

public interface TavPlayerControlsListener
{
	/**
	 * Called when the player control 'previous' ( << ) event occurs
	 */
	public boolean prev();

	/**
	 * Called when the player control 'play' ( > ) event occurs
	 * 
	 * @return
	 */
	public boolean play();

	/**
	 * Called when the player control 'next' ( >> ) event occurs
	 */
	public boolean next();

	/**
	 * Called when the player control 'pause' ( || ) event occurs
	 */
	public void pause();

	/**
	 * <pre>
	 *                                         _
	 * Called when the player control 'stop' (|_|) event occurs
	 * </pre>
	 */
	public void stop();
}
