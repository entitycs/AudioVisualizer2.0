package application.setting;


public class VisualizerDimensionSetting
{
	VisualizerDimension dimension;

	public VisualizerDimensionSetting(){
		dimension = new VisualizerDimension();
	}
	
	public double width(double width){
		return dimension.width();
	}
	
	public void setWidth(double width)
	{
		dimension.setWidth(width);
	}

	public void setHeight(double height){
		dimension.setHeight(height);
	}
	
	public void height(double height)
	{
		dimension.setHeight(height);
	}

}
