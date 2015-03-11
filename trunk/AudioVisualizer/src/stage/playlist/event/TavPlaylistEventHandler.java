package stage.playlist.event;

import java.io.File;

import stage.playlist.listener.TavAddToPlaylist;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * Handles playlist events enumerated in TavPlaylistEvent enumeration class.
 */
public class TavPlaylistEventHandler implements EventHandler<DragEvent>
{

	TavPlaylistEvent playlistEvent;
	private TavAddToPlaylist listener;

	public TavPlaylistEventHandler(TavPlaylistEvent playlistEvent,
			TavAddToPlaylist listener)
	{
		this.playlistEvent = playlistEvent;
		this.listener = listener;
	}

	@Override
	public void handle(DragEvent event)
	{
		if (this.playlistEvent == TavPlaylistEvent.DRAGOVER)
			handleDragOver (event);

		else if (this.playlistEvent == TavPlaylistEvent.DRAGDROP)
			handleDragDrop (event);
	}

	private void handleDragOver(DragEvent event)
	{
		Dragboard db = event.getDragboard();
		if (db.hasFiles())
		{
			event.acceptTransferModes (TransferMode.COPY);
		} else
		{
			event.consume();
		}
	}

	private void handleDragDrop(DragEvent event)
	{
		Dragboard db = event.getDragboard();
		boolean success = false;

		if ( !db.hasUrl())
		{
			event.setDropCompleted (success);
			event.consume();
			return;
		}
		success = true;
		String filePath = "";
		for (File file : db.getFiles())
		{
			if (file.getPath().isEmpty())
				continue;

			filePath = file.getAbsolutePath();

			if (filePath.trim().endsWith(".mp3") || filePath.trim().endsWith(".wav"))
				this.listener.add (filePath.trim().replace("%20", " "));
		}
	}
}
