package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{

	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// Build the application manager
			// and show the primary stage
			buildApp(primaryStage);
		} 
		catch (IOException e)
		{
			System.err.println("Unknown error building application stage.");
			e.printStackTrace();
		}
	}

	/**
	 * Main
	 * 
	 * Per JavaFX convention, main calls launch.
	 * 
	 * The overridden method 'start' provides the JavaFX primary stage
	 * 
	 * @param args are not used in this version
	 */
	public static void main(String[] args)
	{	
		launch();
	}

	/**
	 * Build App
	 * 
	 * Create the application manager and set the primary stage
	 * 
	 * The primary stage comes from the JavaFX application.
	 * 
	 * @param primaryStage
	 *            is the application stage minus t
	 *            (console,playlist,controls,etc.)
	 * @throws IOException
	 */
	private void buildApp(Stage primaryStage) throws IOException
	{
		TavApplicationManager.getInstance().setStage(primaryStage);
	}

}
