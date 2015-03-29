package application.setting;

public class TavMediaPlayerChoiceSetting
{
	private String usePlayerSetting;

	public TavMediaPlayerChoiceSetting()
	{
		useDefault();
	}

	private void useDefault()
	{
		this.usePlayerSetting = "Alternative";
	}

	public void setPlayer(String text)
	{
		if (text.equals ("Default"))
		{
			this.usePlayerSetting = text;
		} else if (text.equals ("Alternate"))
		{
			this.usePlayerSetting = text;
		}
	}

	public String getPlayerSetting()
	{
		// TODO Auto-generated method stub
		return usePlayerSetting;
	}
}
