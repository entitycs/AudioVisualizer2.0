package application.playlist.event;

import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import application.playlist.interfacing.AddToPlaylist;

public class PlaylistEventHandler implements EventHandler<DragEvent>
{

	PlaylistEvent playlistEvent;
	private AddToPlaylist listener;

	public PlaylistEventHandler(PlaylistEvent playlistEvent, AddToPlaylist listener)
	{
		this.playlistEvent = playlistEvent;
		this.listener = listener;
	}

	@Override
	public void handle(DragEvent event)
	{
		if (this.playlistEvent == PlaylistEvent.DRAGOVER)
			handleDragOver(event);

		else if (this.playlistEvent == PlaylistEvent.DRAGDROP)
			handleDragDrop(event);
	}

	private void handleDragOver(DragEvent event)
	{
		Dragboard db = event.getDragboard();
		if (db.hasFiles())
		{
			event.acceptTransferModes(TransferMode.COPY);
		} else
		{
			event.consume();
		}
	}


	private void handleDragDrop(DragEvent event)
	{
		Dragboard db = event.getDragboard();
		boolean success = false;

		if (db.hasUrl())
		{
			success = true;
			String filePath = "";
			for (File file : db.getFiles())
			{
				if (file.getPath().isEmpty())
					continue;

				filePath = file.getAbsolutePath();
				
				if (filePath.trim().endsWith(".mp3"))
					this.listener.add(filePath.trim().replace("%20", " "));
			}
		}
		event.setDropCompleted(success);
		event.consume();
	}
}
