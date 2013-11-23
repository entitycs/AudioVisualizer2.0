package application.playlist;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import application.playlist.event.PlaylistEvent;
import application.playlist.event.PlaylistEventHandler;
import application.playlist.interfacing.AddToPlaylist;
import application.playlist.interfacing.TavPlaylist;

public class Playlist implements TavPlaylist, AddToPlaylist
{
	private int currentTrack = -1;
	
	private ListView<String> content;
	
	private final AddToPlaylist playlistAddMediaListener = this;

	@SuppressWarnings("unchecked")
	public Playlist(Node content)
	{
		this.content = (ListView<String>) content;
		content.setOnDragOver(new PlaylistEventHandler(PlaylistEvent.DRAGOVER, this));

		content.setOnDragDropped(new PlaylistEventHandler(
				PlaylistEvent.DRAGDROP, this));
	}

	@Override
	public String current()
	{
		return locationOrNull();
	}

	@Override
	public String next()
	{
		// reset if at end of playlist
		if (!(++currentTrack < content.getItems().size()))
			currentTrack = 0;
		
		return locationOrNull();
	}
	
	@Override
	public String prev()
	{
		--currentTrack;

		return locationOrNull();	
	}

	@Override
	public int size()
	{
		return this.content.getItems().size();
	}
	
/**
 * Assure that the current track is in bounds 
 *
 * @param currentTrackIndex
 * @return true on success
 * @throws IndexOutOfBoundsException if the current track could not be found in the
 * bounds of the playlist.  
 */
	private boolean assureBounds() throws IndexOutOfBoundsException
	{
		if (currentTrack < 0){
			if (content.getItems().size() > 0)
				currentTrack = 0;
			else{
				throw new IndexOutOfBoundsException("Playlist out of bounds or empty.");
			}
		}
		else if (currentTrack > content.getItems().size()){
			if (content.getItems().size() > 0)
				currentTrack = content.getItems().size()-1;
			else{
				throw new IndexOutOfBoundsException("Playlist out of bounds or empty.");
			}
		}
		
		return true;
	}

	/**
	 * Location or null
	 * 
	 * @return the location of the media at the current index or null.
	 */
	private String locationOrNull(){
		// assure that current (now next) track is in bounds
		if (assureBounds ())
			// return the next track as an absolute path string
			return content.getItems().get(currentTrack);
		
		// note an exception may occur and this statement never gets reached
		return null;
	}

	/**
	 * Add a song to the playlist
	 * 
	 * @param path is an absolute path to a media file
	 */
	public void add(String path)
	{
		this.content.getItems().add(path);
	}

	/**
	 * Set on drag over 
	 * 
	 * This event must be handled in order for the dragdropped handler to work.
	 * 
	 * @param playlistEventHandler handles dragover events for this playlist
	 */
	public void setOnDragOver(PlaylistEventHandler playlistEventHandler)
	{
		this.content.setOnDragOver(playlistEventHandler);
	}

	/**
	 * This event can be handled after the dragover event is handled
	 * 
	 * @param playlistEventHandler handles dragdropped events for the playlist.
	 */
	public void setOnDragDropped(PlaylistEventHandler playlistEventHandler)
	{
		this.content.setOnDragDropped(playlistEventHandler);
	}
}
