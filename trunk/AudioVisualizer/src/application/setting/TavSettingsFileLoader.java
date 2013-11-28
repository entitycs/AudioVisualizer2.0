package application.setting;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TavSettingsFileLoader
{
	private String path;
	private InputStream is;

	private Properties properties = new Properties();

	public TavSettingsFileLoader(String path)
	{
		this.path = path;
	}

	public TavSettingsFileLoader(InputStream resourceAsStream)
	{
		this.is = resourceAsStream;
	}

	public boolean load()
	{
		if (this.path != null)
			return loadFromFile();

		else if (this.is != null)
			return loadFromJar();

		return false;
	}

	private boolean loadFromFile()
	{
		try (FileInputStream fish = new FileInputStream (this.path))
		{
			properties.load (fish);
		} catch (IOException e)
		{
			return false;
		}
		return true;
	}

	private boolean loadFromJar()
	{
		try
		{
			properties.load (is);
		} catch (IOException e)
		{
			return false;
		}
		return true;
	}

	public Properties retrieve()
	{
		try
		{
			if (properties == null)
				throw new NullPointerException (
						"Properties file could not be retrieved");
		} catch (Exception e)
		{
			System.err.println (e.getMessage());
			e.printStackTrace();
		}
		return properties;
	}
}
