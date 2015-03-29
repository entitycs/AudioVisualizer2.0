package stage.menuBar.event;

import java.io.File;
import java.util.List;

import stage.playlist.listener.TavAddToPlaylist;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class TavFileChooserEventHandler implements EventHandler<ActionEvent> {

	private Window mainWindow;
	private TavAddToPlaylist listener;
	
	public TavFileChooserEventHandler(Window main, TavAddToPlaylist listener){
		this.mainWindow = main;
		this.listener = listener;
	}
	
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		List<File> fileList = fileChooser.showOpenMultipleDialog(mainWindow);
		for (File file : fileList)
		{
			if (file.getPath().isEmpty())
				continue;
			
			String filePath = file.getAbsolutePath();

			if (filePath.trim().endsWith (".mp3"))
				this.listener.add (filePath.trim().replace ("%20", " "));
		}
	}

}
