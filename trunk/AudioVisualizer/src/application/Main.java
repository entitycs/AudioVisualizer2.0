package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{

	// JavaFX application starts here (see overridden method in JavaDoc)
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			buildApp(primaryStage);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Main
	 * 
	 * Per JavaFX convention, main calls launch The overridden method 'start' is
	 * called behind the scenes and the primary stage is given as the parameter
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		launch(args);
	}

	/**
	 * Build App
	 * 
	 * Before a visualization begins, the remaining visual objects are
	 * displayed. Included is a playlist, play controls (play, pause, etc.), a
	 * console and a visualization list.
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
