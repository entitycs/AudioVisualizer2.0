/**
 * 
 */
package stage.mediaPlayer.event;

import stage.mediaPlayer.listener.TavPlayerControlsListener;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Handles stop button events and provides a simulateStop() method in the case a
 * combination media player and visualizer object either wishes to provide a
 * method to stop the media without using a player control on the application
 * stage, or to provide a method to stop the media when the window holding the
 * combination player is closed.
 * 
 */
public class TavStopButtonEventHandler implements EventHandler<Event>
{
	private TavPlayerControlsListener playerControlsListener;

	public TavStopButtonEventHandler(
			final TavPlayerControlsListener pauseHandler)
	{
		this.playerControlsListener = pauseHandler;
	}

	@Override
	public void handle(Event event)
	{
		this.playerControlsListener.stop();
	}

	public void simulateStop()
	{
		this.playerControlsListener.stop();
	}

}
