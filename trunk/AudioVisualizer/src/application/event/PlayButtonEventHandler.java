/**
 * 
 */
package application.event;

import application.listener.PlayerControlsEventListener;
import javafx.event.Event;
import javafx.event.EventHandler;

/**
 */
public class PlayButtonEventHandler implements EventHandler<Event>
{

	private PlayerControlsEventListener playerControlsListener;
	
	public PlayButtonEventHandler(final PlayerControlsEventListener resumeHandler){
		this.playerControlsListener = resumeHandler;
	}
	
	@Override
	public void handle(Event event)
	{	System.err.println("PLAY BUTTON (NOT PLAYLISTREADY)");
			this.playerControlsListener.play();
	}

}
