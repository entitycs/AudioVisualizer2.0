package application.visualizer.interfacing;

/**
 * A visualization height which is used so that an object may set the
 * visualization height without having any other information about the
 * visualization
 * 
 * @author entitycs
 * 
 */
public class TavVisualizationHeight extends Number
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Number height;

	public TavVisualizationHeight()
	{
		height = 360;
	}

	@Override
	public int intValue()
	{
		// TODO Auto-generated method stub
		return height.intValue();
	}

	@Override
	public long longValue()
	{
		// TODO Auto-generated method stub
		return height.longValue();
	}

	@Override
	public float floatValue()
	{
		// TODO Auto-generated method stub
		return height.floatValue();
	}

	@Override
	public double doubleValue()
	{
		// TODO Auto-generated method stub
		return height.doubleValue();
	}

	public void setValue(Number newheight)
	{
		height = newheight;
	}
}
