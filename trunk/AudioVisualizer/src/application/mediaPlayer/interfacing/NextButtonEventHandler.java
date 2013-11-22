/**
 * 
 */
package application.mediaPlayer.interfacing;

import javafx.event.Event;
import javafx.event.EventHandler;
import listeners.PlayerControlsEventListener;


public class NextButtonEventHandler implements EventHandler<Event>
{
	private PlayerControlsEventListener playerControlsListener;
	
	public NextButtonEventHandler(final PlayerControlsEventListener pauseHandler){
		this.playerControlsListener = pauseHandler;
	}
	
	@Override
	public void handle(Event event)
	{	
			this.playerControlsListener.next();
	}

}
