package application.mediaPlayer.interfacing;

import listeners.PlaylistReadyListener;

public class TavEndOfMediaEventHandler implements Runnable //extends TavEndOfMediaHandler
{

	// since we are using the same media
	private PlaylistReadyListener manager;
	
	public TavEndOfMediaEventHandler(PlaylistReadyListener manager)
	{
		this.manager = manager;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.manager.playlistReady();
	}

}
