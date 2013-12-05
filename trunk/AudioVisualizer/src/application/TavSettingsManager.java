package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import stage.TavApplicationStage;
import application.mediaPlayer.interfacing.TavComboMediaPlayer;
import application.setting.TavMediaPlayerChoiceSetting;
import application.setting.TavVisualizerDimensionSetting;
import application.setting.interfacing.TavSettingsApplier;
import application.setting.listener.TavMediaPlayerChoiceSettingListener;
import application.setting.listener.TavVisualizerDimensionSettingListener;

/**
 * Manages the system settings
 * 
 */
public class TavSettingsManager implements TavSettingsApplier,
		TavMediaPlayerChoiceSettingListener,
		TavVisualizerDimensionSettingListener
{
	// the following may be better suited in its own object which can load
	// settings from a file
	private final String settingsFilePath = getClass().getProtectionDomain()
			.getCodeSource().getLocation().toString()
			.replaceAll ("[/][^/]*[.]jar", "/").replace ("file:", "")
			+ "opt/settings.tav";

	private final TavMediaPlayerChoiceSetting mediaPlayerSetting;
	private final TavVisualizerDimensionSetting visualizerSetting;
	private Properties settings;

	private final int PLAYER_CHOICE = 0;
	private final int VIS_WIDTH = 1;
	private final int VIS_HEIGHT = 2;
	private final int NUM_SETTINGS = 3;

	private String[] availableSettings;

	public TavSettingsManager()
	{
		// TODO save custom visualization settings
		availableSettings = new String[NUM_SETTINGS];
		availableSettings[PLAYER_CHOICE] = "PlayerChoice";
		availableSettings[VIS_WIDTH] = "VisWidth";
		availableSettings[VIS_HEIGHT] = "VisHeight";
		mediaPlayerSetting = new TavMediaPlayerChoiceSetting();
		visualizerSetting = new TavVisualizerDimensionSetting();
		this.settings = new Properties();
	}

	private void writeToSettingsFile(String settingName, String settingValue)
	{
		try (FileOutputStream fos = new FileOutputStream (this.settingsFilePath))
		{
			settings.setProperty (settingName, settingValue);
			settings.store (fos, null);
		} catch (IOException e)
		{
			System.err.println ("Unable to write to settings file");
			e.printStackTrace();
		}
	}

	// Deprecated. The settings file should already be created on build.

	// private void createNewSettingsFile() throws FileNotFoundException,
	// IOException
	// {
	// try (FileOutputStream fos = new FileOutputStream (this.settingsFilePath))
	// {
	// settings.setProperty ("PlayerChoice", "Default");
	// settings.setProperty ("VisWidth", "500");
	// settings.setProperty ("VisHeight", "350");
	// settings.store (fos, "Settings File Created");
	// }
	// }

	public void widthSetting(double width)
	{
		writeToSettingsFile (availableSettings[1], ("" + width));

		if (TavApplicationManager.getInstance().getMediaPlayerManager()
				.usingComboPlayer())
		{
			((TavComboMediaPlayer) TavApplicationManager.getInstance()
					.getMediaPlayerManager().getMediaPlayer())
					.setWidth (width);
		} else
		{
			visualizerSetting.setWidth (width);
			TavApplicationManager.getInstance().getVisualizerManager()
					.setWidth (width);
		}
	}

	public void heightSetting(double height)
	{
		writeToSettingsFile (availableSettings[2], ("" + height));

		if (TavApplicationManager.getInstance().getMediaPlayerManager()
				.usingComboPlayer())
		{
			((TavComboMediaPlayer) TavApplicationManager.getInstance()
					.getMediaPlayerManager().getMediaPlayer())
					.setHeight (height);
		} else
		{
			visualizerSetting.setHeight (height);
			TavApplicationManager.getInstance().getVisualizerManager()
					.setHeight (height);
		}
	}

	public void setPlayerSetting(String text)
	{ 
		writeToSettingsFile (availableSettings[0], (text));
		// if there is a media player in action, stop it.
		if (TavApplicationManager.getInstance().getMediaPlayerManager()
				.getMediaPlayer() != null)
			TavApplicationManager.getInstance().getMediaPlayerManager()
					.stop();

		mediaPlayerSetting.setPlayer (text);
		TavApplicationStage.getInstance().loadedPlayerSetting (text);
		TavApplicationManager.getInstance().getMediaPlayerManager()
				.updatePlayer();
	}

	public String getPlayerSetting()
	{
		return mediaPlayerSetting.getPlayerSetting();
	}

	public void loadFromSettingsFile() throws IOException
	{
		try (FileInputStream fish = new FileInputStream (this.settingsFilePath))
		{
			settings.load (fish);
			for (int i = 0; i < availableSettings.length; i++)
			{
				if (settings.containsKey (availableSettings[i]))
				{ 
					applySettingsFromFile (availableSettings[i],
							settings.getProperty (availableSettings[i]));
				}
			}
		}
	}

	private void applySettingsFromFile(String setting, String value)
	{
		if (setting == availableSettings[PLAYER_CHOICE])
		{   
			setPlayerSetting (value);
		} else if (setting == availableSettings[VIS_WIDTH])
		{
			// TODO
		} else if (setting == availableSettings[VIS_HEIGHT])
		{
			// TODO
		}

	}
}
