package stage.playlist.listener;

public interface TavAddToPlaylist
{
	/**
	 * Add a song to the playlist.
	 * 
	 * @param mediaLocation
	 *            is an absolute path to a media file
	 */
	public void add(String mediaLocation);

}
