package application.setting.interfacing;

public interface ApplicationSetting
{
	// the following may be better suited in its own object which can load 
	// settings from a file
	
	public void widthSetting(double width);

	public void heightSetting(double height);

	public void playerSetting(String text);

	public String getPlayerSetting();
}
