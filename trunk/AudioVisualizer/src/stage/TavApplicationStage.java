package stage;

import java.io.IOException;

import stage.mediaPlayer.TavPlayerControlsGroup;
import stage.mediaPlayer.listener.TavPlayerControlsListener;
import stage.menuBar.event.TavRemoveSelectedMenuItemEventHandler;
import stage.playlist.TavPlaylist;
import stage.playlist.listener.TavPlaylistReadyListener;
import stage.visualizer.TavItemToColorChosenEventHandler;
import stage.visualizer.TavItemToColorColorSetEventHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import application.mediaPlayer.TavMetaData;
import application.mediaPlayer.listener.TavMetaDataListener;
import application.setting.event.TavMediaPlayerChoiceSettingEventHandler;
import application.setting.event.TavSettingsMenuItemEventHandler;
import application.setting.listener.TavMediaPlayerChoiceSettingListener;
import application.setting.listener.TavVisualizerDimensionSettingListener;
import application.visualizer.listener.TavVisualizerControlsListener;

/**
 * The application stage
 * 
 * All visual GUI components are contained in the application stage. The
 * visualization window is dependent on the adapted visualizer
 */
public class TavApplicationStage extends VBox implements TavMetaDataListener
{

	private static Scene appScene;

	private static AnchorPane console;

	private TavPlayerControlsGroup playerControls;

	private TavPlaylist playlist;

	private TavVisualizerDimensionSettingListener visualizerDimensionSettingListener;

	private TavMediaPlayerChoiceSettingListener playerChoiceSettingListener;

	private MenuBar menuBar;

	private Label leftLabel;

	// private Label rightLabel;

	private ChoiceBox<String> colorItemChoiceBox;

	// private ChoiceBox<String> visualizationChoiceBox;

	private ColorPicker colorPicker;

	private ToolBar colorChooserToolbar;

	private GridPane customizeVidSliders;

	private String playerChoice;

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

	@SuppressWarnings("unchecked")
	private TavApplicationStage()
	{
		FXMLLoader loader = new FXMLLoader (getClass().getResource (
				"AudioVisXML.fxml"));
		VBox root;
		loader.setController (this);
		AnchorPane playerControlPane;
		try
		{
			root = (VBox) loader.load();

			// file menu, edit menu, etc.
			this.menuBar = (MenuBar) root.lookup ("#menuBar");

			// re-sizable split panes (horizontal)
			SplitPane splitH = (SplitPane) root.lookup ("#splitH");

			// re-sizable split pane (vertical)
			SplitPane splitV = (SplitPane) splitH.getItems().get (0);

			this.leftLabel = (Label) root.lookup ("#HBox").lookup (
					"#LeftStatus");

			// this.leftLabel = (Label) root.lookup ("#leftLabel");

			// this.rightLabel = (Label) root.lookup ("#rightLabel");

			// the console is wrapped in a scroll pane
			ScrollPane consoleScroller = (ScrollPane) splitH.getItems()
					.get (1);
			console = (AnchorPane) consoleScroller.getContent();

			// all player controls (play, pause, etc.) descend from this pane
			playerControlPane = (AnchorPane) ((AnchorPane) splitV.getItems()
					.get (0)).getChildren().get (0);

			// the images for the buttons already exist, but they are not
			// buttons so we will create buttons out of them and replace the
			// existing images (or shapes within AnchorPanes) with the buttons
			playerControls = new TavPlayerControlsGroup (createButtonFromPane (
					playerControlPane, 0), createButtonFromPane (
					playerControlPane, 1), createButtonFromPane (
					playerControlPane, 2), createButtonFromPane (
					playerControlPane, 3), createButtonFromPane (
					playerControlPane, 4));

			// the playlist is wrapped in a scroll pane
			ScrollPane playlistScroll = (ScrollPane) ((AnchorPane) ((AnchorPane) splitV
					.getItems().get (0)).getChildren().get (1))
					.lookup ("#playlistScrollPane");

			AnchorPane visListPane = (AnchorPane) (splitV.getItems().get (1));

			AnchorPane colorChooserAnchor = (AnchorPane) visListPane
					.lookup ("#ColorChooser");
			// this.visualizationChoiceBox = (ChoiceBox<String>) visListPane
			// .lookup ("#visualizationChoiceBox");
			this.colorChooserToolbar = (ToolBar) colorChooserAnchor
					.lookup ("#ColorChooserToolBar");
			this.customizeVidSliders = (GridPane) visListPane
					.lookup ("#CustomizeVisSliders");

			this.colorItemChoiceBox = (ChoiceBox<String>) colorChooserToolbar
					.getItems().get (0);
			this.colorPicker = (ColorPicker) colorChooserToolbar.getItems()
					.get (1);

			playlist = new TavPlaylist (
					(ListView<String>) playlistScroll.getContent());

			appScene = new Scene (root, 1024, 668);

		} catch (IOException e)
		{
			System.err.println ("Error creating main application stage");
			e.printStackTrace();
		}
	}

	private Button createButtonFromPane(AnchorPane playControlPane, int index)
	{
		Button button = new Button();
		button.setId ("Button" + index);
		button.setGraphic (playControlPane.getChildren().get (index));
		button.setMaxSize (50, 70);
		button.setMinSize (50, 70);

		// and replace the existing image with the button
		playControlPane.getChildren().remove (index);
		playControlPane.getChildren().add (index, button);
		AnchorPane.setTopAnchor (button, -10.0);
		AnchorPane.setLeftAnchor (button, 52.0 * index + 2);
		return button;
	}

	private void browseMenuItem(MenuItem i)
	{
		if (i.getId() != null)
		{
			if (i.getId().equals ("Settings"))
			{
				i.setOnAction (new TavSettingsMenuItemEventHandler (
						this.visualizerDimensionSettingListener));

			} else if (i.getId().equals ("ChoosePlayer"))
			{
				browseSubmenuItems ((Menu) i);
			}
			else if (i.getId ().equals ("RemoveSelected")){
				i.setOnAction (new TavRemoveSelectedMenuItemEventHandler(this.playlist));
			}
		}
	}

	private void browseSubmenuItems(Menu m)
	{
		for (MenuItem mi : m.getItems())
		{
			RadioMenuItem rmi = (RadioMenuItem) mi;

			if (rmi.getId() == null)
			{
				continue;
			}

			// the default is set in the corresponding .xml file
			if (rmi.getId().equals ("Default"))
			{
				if (this.playerChoice != null && this.playerChoice == "Default")
					//
					rmi.setSelected (true);
			} else if (rmi.getId().equals ("Alternate"))
			{
				if (this.playerChoice != null
						&& this.playerChoice == "Alternate")
					//
					rmi.setSelected (true);
			}
			mi.setOnAction (new TavMediaPlayerChoiceSettingEventHandler (
					this.playerChoiceSettingListener));

		}
	}

	/**
	 * Initialize Handlers
	 * 
	 * Any events coming from the primary application stage have handlers which
	 * are set in this function. This method should be called after the first
	 * initialization of the object.
	 * 
	 * This function is necessary in order to let the event handlers refer
	 * statically to the application manager.
	 * 
	 * post-revision: 623 - moved setting of event handlers outside of
	 * constructor.
	 * 
	 * @param playlistReadyListener
	 *            is the object which decides what to do in the event that the
	 *            playlist is ready (i.e. new media is about to be played).
	 * 
	 * @param playerControlsListener
	 *            is the object which decides what to do with player control
	 *            events in application.event (next, play, pause, etc.).
	 * 
	 * @param visualizerControlsListener
	 *            is the object which decides what to do with events originating
	 *            from the visualizer (e.g. change in visualizer width).
	 */
	public void initHandlers(
			final TavPlaylistReadyListener playlistReadyListener,
			final TavPlayerControlsListener playerControlsListener,
			final TavVisualizerControlsListener visualizerControlsListener)
	{
		// THIS IS A STUB - not to be considered implemented
		for (Menu m : menuBar.getMenus())
		{
			for (MenuItem i : m.getItems())
			{
				browseMenuItem (i);
			}
		}
		this.playerControls.initHandlers (playlistReadyListener,
				playerControlsListener);
		// this.visualizationChoiceBox.getSelectionModel().selectedIndexProperty().addListener
		// (new ColorItemChoiceListener(visualizerControlsListener,
		// colorPicker));
		this.colorItemChoiceBox
				.getSelectionModel()
				.selectedIndexProperty()
				.addListener (
						new TavItemToColorColorSetEventHandler (
								visualizerControlsListener,
								(Rectangle) this.colorChooserToolbar
										.getItems().get (2)));
		this.colorPicker.setOnAction (new TavItemToColorChosenEventHandler (
				visualizerControlsListener));
		visualizerControlsListener
				.setVisualizationControls (this.customizeVidSliders);
	}

	/**
	 * The primary stage is actually given by JavaFX, but from the perspective
	 * of the user and any developer creating an instance of a media player and
	 * visualizer, this object is considered to be the primary stage.
	 * 
	 * This function lets the application manager set the JavaFX primary stage
	 * with the scene of this "stage." When the application manager calls
	 * primaryStage.show(), the scene of this stage is shown in the javaFX
	 * primary stage.
	 * 
	 * @return the application Scene to be displayed on the primary stage.
	 */
	public static Scene getAppScene()
	{
		return appScene;
	}

	/**
	 * Get the playlist from the application stage
	 * 
	 * @return
	 */
	public TavPlaylist getPlaylist()
	{
		return playlist;
	}

	/**
	 * Set the visualizer dimension listener for the dimension settings. This
	 * setting is found in the menu bar's list of menu items so if we get
	 * another chance to refactor, the menu bar and associated data perhaps
	 * should exists in a separate class file.
	 * 
	 * @param listener
	 */
	public void setVisualizerDimensionSettingListener(
			TavVisualizerDimensionSettingListener listener)
	{
		this.visualizerDimensionSettingListener = listener;
	}

	/**
	 * Set the listener for handling the player choice event. This setting is
	 * found in the menu bar's list of menu items.
	 * 
	 * @param listener
	 *            is the listener for the player choice event.
	 */
	public void setPlayerChoiceSettingListener(
			TavMediaPlayerChoiceSettingListener listener)
	{
		this.playerChoiceSettingListener = listener;
	}

	/**
	 * 
	 * @param array
	 *            is the spectrum array
	 */
	public void consoleFloatArray(final float[] array)
	{
		Platform.runLater (new Runnable()
		{
			@Override
			public void run()
			{
				int used_length = array.length / 2;
				if (array != null && array.length > 1)
					((Label) console.getChildren().get (0)).setText ("0: "
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

	// /**
	// *
	// * @return the choicebox used to choose which color referenced by the
	// * visualizer (if applicable) the selected color will apply to.
	// */
	// public ChoiceBox getColorItemChoiceBox()
	// {
	// return this.colorItemChoiceBox;
	// }

	/**
	 * 
	 * @return the group of player controls (prev, play, next, etc.).
	 */
	public TavPlayerControlsGroup getPlayerControls()
	{
		return playerControls;
	}

	/**
	 * Called when the player setting has been loaded from the settings manager.
	 * 
	 * @param playerChoice
	 *            is the choice of media player to be used with the system.
	 */
	public void loadedPlayerSetting(String playerChoice)
	{
		this.playerChoice = playerChoice;
	}

	/**
	 * Get the left status label.
	 * 
	 * @return the left status label.
	 */
	public Label getLeftStatusLabel()
	{
		return this.leftLabel;
	}

	@Override
	public void metaDataUpdate(TavMetaData metaData)
	{
		this.leftLabel.setText (metaData.flatString());
	}
}
