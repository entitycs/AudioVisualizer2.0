package application;

import java.io.File;
import java.io.IOException;

import application.event.PlaylistEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class PlaylistEventHandler implements EventHandler<DragEvent>
{

	PlaylistEvent playlistEvent;

	public PlaylistEventHandler(PlaylistEvent playlistEvent)
	{
		this.playlistEvent = playlistEvent;
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

	@SuppressWarnings("unchecked")
	private void handleDragDrop(DragEvent event)
	{
		ListView<String> playlist = TavApplicationStage.getInstance()
				.getPlaylist();

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
				try
				{
					filePath = file.getCanonicalPath();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				playlist.getItems().add(filePath.trim().replace("%20", " "));
			}
		}
		event.setDropCompleted(success);
		event.consume();
	}
}
