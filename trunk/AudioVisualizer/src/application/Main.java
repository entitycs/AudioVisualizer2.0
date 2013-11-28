package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The AudioVisualizer main application object.
 */
public class Main extends Application
{

	/**
	 * Default Constructor for the AudioVisualizer main application.
	 */
	public Main()
	{

	}

	@Override
	/**
	 * Start the JavaFX application.
	 * 
	 */
	public void start(Stage primaryStage)
	{
		try
		{

			// Build the application manager
			// and show the primary stage
			buildApp (primaryStage);

		} catch (IOException e)
		{
			System.err.println ("Unknown error building application stage.");
			e.printStackTrace();
		}
	}

	/**
	 * AudioVisualizer main method.
	 * 
	 * Per JavaFX convention, main calls launch.
	 * 
	 * The overridden method 'start' provides the JavaFX primary stage
	 * 
	 * @param args
	 *            are not used in this version
	 */
	public static void main(String[] args)
	{
		launch (args);
	}

	/**
	 * Build App
	 * 
	 * Create the application manager and set the primary stage
	 * 
	 * The primary stage comes from the JavaFX application.
	 * 
	 * @param primaryStage
	 *            is the application stage (console,playlist,controls,etc.) and
	 *            does not include the visualization stage.
	 * @throws IOException
	 */
	private void buildApp(Stage primaryStage) throws IOException
	{
		TavApplicationManager.getInstance().setStage (primaryStage);

		primaryStage.addEventHandler (WindowEvent.WINDOW_CLOSE_REQUEST,
				TavApplicationManager.getInstance());
	}

}
