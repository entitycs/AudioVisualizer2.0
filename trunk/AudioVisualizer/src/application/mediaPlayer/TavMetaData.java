package application.mediaPlayer;

/**
 * Not implemented.
 * 
 * MetaData can be shared throughout the application if the playlist is given a
 * method of extracting metadata as items are added to the playlist. This could
 * cause loading songs into the playlist to take a while. If this is
 * implemented, use the console to display a "loading [N] songs..." message.
 * 
 */
public class TavMetaData
{

	private String metaData;

	public TavMetaData(String metaData)
	{
		this.metaData = metaData;
	}

	public String flatString()
	{
		return metaData.replace ("\n", "  |  ");
	}

}
