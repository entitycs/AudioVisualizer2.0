package application.setting;


public class MediaPlayerSetting
{		
	private		String usePlayerSetting;
		public MediaPlayerSetting(){
			useDefault();
	}

	private void useDefault()
	{
		this.usePlayerSetting = "Default";
	}
	
	public void setPlayer(String text)
	{
		if (text.equals("Default"))
		{
			this.usePlayerSetting = text;
		} else if (text.equals("Alternate"))
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
