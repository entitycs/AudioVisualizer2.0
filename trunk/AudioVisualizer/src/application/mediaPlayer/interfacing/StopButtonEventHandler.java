/**
 * 
 */
package application.mediaPlayer.interfacing;

import javafx.event.Event;
import javafx.event.EventHandler;
import listeners.PlayerControlsEventListener;

public class StopButtonEventHandler implements EventHandler<Event>
{
	private PlayerControlsEventListener playerControlsListener;
	
	public StopButtonEventHandler(final PlayerControlsEventListener pauseHandler){
		this.playerControlsListener = pauseHandler;
	}
	
	@Override
	public void handle(Event event)
	{	
			this.playerControlsListener.stop();
	}

	public void simulateStop(){
		this.playerControlsListener.stop();
	}
	
}
