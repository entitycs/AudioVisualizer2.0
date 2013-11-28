package application.setting;

public class TavVisualizerDimension
{
	double width;
	double height;

	public TavVisualizerDimension()
	{
		revertToMinimalDimension();
	}

	TavVisualizerDimension(double width, double height)
	{
		this.width = width;
		this.height = height;

		if (this.width < 30 || this.height < 30)
			revertToMinimalDimension();
	}

	private void revertToMinimalDimension()
	{
		this.width = 100;
		this.height = 100;
	}

	public double width()
	{
		return this.width;
	}

	public double height()
	{
		return this.height;
	}

	public void setWidth(double width2)
	{
		this.width = width2;
	}

	public void setHeight(double height2)
	{
		this.height = height2;
	}
}
