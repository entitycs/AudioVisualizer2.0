package stage.playlist;

import java.util.ArrayList;
import java.util.List;

import stage.menuBar.listener.TavRemoveSelectedMenuItemListener;
import stage.playlist.event.TavPlaylistEvent;
import stage.playlist.event.TavPlaylistEventHandler;
import stage.playlist.interfacing.TavPlaylistControls;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class TavPlaylist implements TavPlaylistControls, TavRemoveSelectedMenuItemListener
		
{
	private int currentTrack = -1;

	private ListView<String> content;

	private ObservableList<Integer> selected;

	/**
	 * Constructor
	 * 
	 * @param content
	 *            is the ListView object serving as the base of the playlist.
	 */
	public TavPlaylist(ListView<String> content)
	{
		this.content = content;
		this.content.setOnDragOver (new TavPlaylistEventHandler (
				TavPlaylistEvent.DRAGOVER, this));

		this.content.setOnDragDropped (new TavPlaylistEventHandler (
				TavPlaylistEvent.DRAGDROP, this));

		this.content.getSelectionModel().setSelectionMode (SelectionMode.MULTIPLE);

	}

	@Override
	public String current()
	{
		if (content.getItems().size() < 1)
			return null;
		return locationOrNull();
	}

	@Override
	public String next()
	{
		if (content.getItems().size() < 1)
			return null;
		// reset if at end of playlist
		if ( ! (++currentTrack < content.getItems().size()))
			currentTrack = 0;

		return locationOrNull();
	}

	@Override
	public String prev()
	{
		if (content.getItems().size() < 1)
			return null;

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
	 * @throws IndexOutOfBoundsException
	 *             if the current track could not be found in the bounds of the
	 *             playlist.
	 */
	private boolean assureBounds() throws IndexOutOfBoundsException
	{
		// if current track falls below 0
		if (currentTrack < 0)
		{ // and if there is media in the playlist
			if (content.getItems().size() > 0)
				// set current track to the minimal allowed value
				currentTrack = 0;
			else
			{ // otherwise, throw exception
				throw new IndexOutOfBoundsException (
						"Playlist out of bounds or empty.");
			}

			// if the current track falls above the playlist size
		} else if (currentTrack > content.getItems().size())
		{ // and if there is media in the playlist
			if (content.getItems().size() > 0)
				// set current track to the maximum allowed value
				currentTrack = content.getItems().size() - 1;
			else
			{ // otherwise, throw exception
				throw new IndexOutOfBoundsException (
						"Playlist out of bounds or empty.");
			}
		}

		content.getSelectionModel().clearSelection();
		content.getSelectionModel().select (currentTrack);

		return true;
	}

	/**
	 * Location or null
	 * 
	 * @return the location of the media at the current index or null if no
	 *         media location is set at the current index.
	 */
	private String locationOrNull()
	{
		// assure the track is in bounds
		if ( !assureBounds())
			return null;

		// return the media's location as an absolute path (String)
		return content.getItems().get (currentTrack);
	}

	@Override
	public void add(String path)
	{
		this.content.getItems().add (path);
		if (currentTrack == -1)
			currentTrack = 0;
	}

	/**
	 * Set on drag over
	 * 
	 * This event must be handled in order for the dragdropped handler to work.
	 * 
	 * @param playlistEventHandler
	 *            handles dragover events for this playlist
	 */
	public void setOnDragOver(TavPlaylistEventHandler playlistEventHandler)
	{
		this.content.setOnDragOver (playlistEventHandler);
	}

	/**
	 * This event can be handled after the dragover event is handled
	 * 
	 * @param playlistEventHandler
	 *            handles dragdropped events for the playlist.
	 */
	public void setOnDragDropped(TavPlaylistEventHandler playlistEventHandler)
	{
		this.content.setOnDragDropped (playlistEventHandler);
	}

	/**
	 * Get the list of currently selected media in the playlist
	 * via the selected item's index.
	 * 
	 * @return
	 */
	public ObservableList<Integer> getSelected(){
		return this.selected;
	}

	@Override
	public void removeSelected()
	{
		 List<String> items =  new ArrayList<String> (content.getSelectionModel().getSelectedItems());  
         content.getItems ().removeAll(items);
         content.getSelectionModel().clearSelection();	
	}
	
	@Override
	public void deselectAll(){
		content.getSelectionModel().clearSelection ();
	}

	@Override
	public void selectAll(){
		content.getSelectionModel ().selectAll ();
	}
}
