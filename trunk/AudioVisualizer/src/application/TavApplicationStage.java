package application;

import java.io.IOException;











import application.event.NextButtonEventHandler;
import application.event.PauseButtonEventHandler;
import application.event.PlayButtonEventHandler;
//import application.event.PlayerControlEvent;
import application.event.PlaylistReadyButtonEventHandler;
import application.event.PrevButtonEventHandler;
import application.event.StopButtonEventHandler;
import application.listener.PlayerControlsEventListener;
import application.listener.PlaylistReadyListener;
import application.playlist.Playlist;
import application.playlist.event.PlaylistEvent;
import application.setting.event.PlayerChoiceSettingEventHandler;
import application.setting.event.SettingsMenuItemEventHandler;
import application.setting.listener.PlayerChoiceSettingListener;
import application.visualizer.interfacing.VisualizerControlsEventListener;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * The application stage
 * 
 * All visual GUI components are contained in the application stage. The
 * visualization window is dependent on the adapted visualizer
 */
public class TavApplicationStage extends VBox
{

	private static Scene appScene;

	private static AnchorPane console;

	private AnchorPane prevButton;

	private AnchorPane playButton;

	private AnchorPane nextButton;

	private AnchorPane pauseButton;

	private AnchorPane stopButton;
	
	private StopButtonEventHandler stopHandler;
	private PlaylistReadyButtonEventHandler playHandler;
	private PlayButtonEventHandler resumeHandler;
	
	private Playlist playlist;



	

	/**
	 * A static, randomly named singleton inner class implementation
	 */
	private static class _dr30_93s56ga
	{
		private static final TavApplicationStage INSTANCE = new TavApplicationStage();
	}

	/**
	 * Get Instance
	 * 
	 * @return the singleton instance of the TavApplicationStage object
	 */
	public static TavApplicationStage getInstance()
	{
		return _dr30_93s56ga.INSTANCE;
	}

	private TavApplicationStage()
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"AudioVisXML.fxml"));
		VBox root;
		loader.setController(this);
		AnchorPane playControlPane;
		try
		{
			root = (VBox) loader.load();

			// file menu, edit menu, etc.
			MenuBar menuBar = (MenuBar) root.lookup("#menuBar");

			// THIS IS A STUB - not to be considered implemented
			for (Menu m : menuBar.getMenus())
			{
				for (MenuItem i : m.getItems())
				{
					browseMenuItem(i);
				}
			}
			
			// re-sizable split panes (horizontal)
			SplitPane splitH = (SplitPane) root.lookup("#splitH");

			// re-sizable split pane (vertical)
			SplitPane splitV = (SplitPane) splitH.getItems().get(0);

			// the console is wrapped in a scroll pane
			ScrollPane consoleScroller = (ScrollPane) splitH.getItems().get(1);
			console = (AnchorPane) consoleScroller.getContent();

			// all player controls (play, pause, etc.) descend from this pane
			playControlPane = (AnchorPane) ((AnchorPane) splitV.getItems().get(
					0)).getChildren().get(0);

			prevButton = (AnchorPane) playControlPane.getChildren().get(0);
			playButton = (AnchorPane) playControlPane.getChildren().get(1);
			nextButton = (AnchorPane) playControlPane.getChildren().get(2);
			pauseButton = (AnchorPane) playControlPane.getChildren().get(3);
			stopButton = (AnchorPane) playControlPane.getChildren().get(4);

			// the playlist is wrapped in a scroll pane
			ScrollPane playlistScroll = (ScrollPane) ((AnchorPane) ((AnchorPane) splitV
					.getItems().get(0)).getChildren().get(1)).getChildren()
					.get(0);

			playlist = new Playlist(playlistScroll.getContent());

			appScene = new Scene(root, 1024, 668);

		} catch (IOException e)
		{
			System.err.println("Error creating main application stage");
			e.printStackTrace();
		}
	}
	
	private void browseMenuItem(MenuItem i){
		if (i.getText() != null)
		{
			if ( i.getText().equals("Settings…"))
			{
				i.setOnAction(new SettingsMenuItemEventHandler(TavApplicationManager.getInstance().getSettingManager()));
			}
			else if (i.getText().equals("AudioVisualizer"))
			{
				browseSubmenuItems((Menu)i);
			}
		}
	}
	
	private void browseSubmenuItems(Menu m){
		PlayerChoiceSettingListener listener = TavApplicationManager.getInstance().getSettingManager();
		for (MenuItem mi : m.getItems())
		{
			RadioMenuItem rmi = (RadioMenuItem) mi;
			
			if (rmi.getText() != null)
			{
				// the default is set in the corresponding .xml file
				if (rmi.getText().equals("Default")){
					// TODO check setting in file, and if a match..
					if (false)
						// TODO set the setting appropriately
						rmi.setSelected(true);
				}
				else if (rmi.getText().equals("Alternate"))
				{
					// TODO check setting in file
					if (false)
						// TODO set the setting appropriately
						rmi.setSelected(true);
				}
				mi.setOnAction(new PlayerChoiceSettingEventHandler(listener));
			}
		}
	}
	

	/**
	 * Initialize Handlers
	 * 
	 * Any events coming from the primary application stage have handlers which
	 * are set in this function. This method should be called after the first
	 * initialization of the object
	 * 
	 * This function is necessary in order to let the event handlers refer
	 * statically to the application manager.
	 * 
	 * post-revision: 623 - moved setting of event handlers outside of
	 * constructor
	 * @param visualizerManager 
	 * @param mediaPlayerManager 
	 */
	public void initHandlers(final PlaylistReadyListener playlistReadyListener,
			final PlayerControlsEventListener playerControlsListener,
			final VisualizerControlsEventListener visualizerControlsListener)
	{

		this.resumeHandler = new PlayButtonEventHandler(playerControlsListener);
		
		this.playHandler = new PlaylistReadyButtonEventHandler(playlistReadyListener,
				 resumeHandler);
		
		prevButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new PrevButtonEventHandler(playerControlsListener));

		playButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				this.playHandler);

		nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new NextButtonEventHandler(playerControlsListener));

		pauseButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new PauseButtonEventHandler(playerControlsListener));

		this.stopHandler = new StopButtonEventHandler(playerControlsListener);
		
		stopButton.addEventHandler(MouseEvent.MOUSE_CLICKED, stopHandler);

	}

	public static Scene getAppScene()
	{
		return appScene;
	}

	public Playlist getPlaylist()
	{
		return playlist;
	}

	public  StopButtonEventHandler getStopHandler(){
		return stopHandler;
	}
	
	/**
	 * 
	 * @param array
	 *            is the spectrum array
	 */
	public void consoleFloatArray(final float[] array)
	{
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				int used_length = array.length / 2;
				if (array != null && array.length > 1)
					((Label) console.getChildren().get(0)).setText("0: "
							+ array[0] + "\n" + (used_length / 5) + ": "
							+ array[used_length / 5] + "\n"
							+ (used_length * 2 / 5) + ": "
							+ array[used_length * 2 / 5] + "\n"
							+ (used_length * 3 / 5) + ": "
							+ array[used_length * 3 / 5] + "\n"
							+ (used_length * 4 / 5) + ": "
							+ array[used_length * 4 / 5] + "\n"
							+ (used_length - 1) + ": " + array[used_length - 1]
							+ "\n");
			}
		});
	}

	/**
	 * Reset Play Handler
	 * 
	 * This method is called when the play button is to function not as 'resume'
	 * but as 'load media and play from beginning after updating from settings'
	 */
	public void resetPlayHandler()
	{
		playButton.removeEventHandler(
				MouseEvent.MOUSE_CLICKED, resumeHandler);

		playButton.addEventHandler(
				MouseEvent.MOUSE_CLICKED, playHandler);
	}
}
