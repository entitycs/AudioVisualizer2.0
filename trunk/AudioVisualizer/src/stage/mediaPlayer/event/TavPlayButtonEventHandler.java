/**
 * 
 */
package stage.mediaPlayer.event;

import stage.mediaPlayer.listener.TavPlayerControlsListener;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 */
public class TavPlayButtonEventHandler implements EventHandler<Event>
{

	private TavPlayerControlsListener playerControlsListener;

	public TavPlayButtonEventHandler(
			final TavPlayerControlsListener resumeHandler)
	{
		this.playerControlsListener = resumeHandler;
	}

	@Override
	public void handle(Event event)
	{
		this.playerControlsListener.play();
	}

}
