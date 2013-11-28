package application.setting;

public class TavVisualizerDimensionSetting
{
	TavVisualizerDimension dimension;

	public TavVisualizerDimensionSetting()
	{
		dimension = new TavVisualizerDimension();
	}

	public double width(double width)
	{
		return dimension.width();
	}

	public void setWidth(double width)
	{
		dimension.setWidth (width);
	}

	public void setHeight(double height)
	{
		dimension.setHeight (height);
	}

	public double height(double height)
	{
		return dimension.height();
	}

}
