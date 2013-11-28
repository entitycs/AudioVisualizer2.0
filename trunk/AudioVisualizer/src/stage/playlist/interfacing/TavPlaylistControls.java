package stage.playlist.interfacing;

import stage.playlist.listener.TavAddToPlaylist;

public interface TavPlaylistControls extends TavAddToPlaylist
{
	/**
	 * @return the absolute path of the current song
	 */
	public String current();

	/**
	 * Provides the absolute path of the next song in the playlist and sets the
	 * song as the playlist's current song.
	 * 
	 * @return the absolute path of the next song in the playlist or of the
	 *         current song if the next song does not exist.
	 */
	public String next();

	/**
	 * Provides the absolute path of the previous song in the playlist and sets
	 * the song as the playlist's current song.
	 * 
	 * @return the absolute path of the previous song in the playlist or of the
	 *         current song if no previous song exists.
	 */
	public String prev();

	/**
	 * @return the size of the playlist.
	 */
	public int size();

	/**
	 * Deselect any and all selected items in the playlist.
	 */
	void deselectAll();

	/**
	 * Select any and all items in the playlist.
	 */
	void selectAll();

}
