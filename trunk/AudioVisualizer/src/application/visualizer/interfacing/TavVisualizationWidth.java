package application.visualizer.interfacing;

/**
 * A visualization width which is used so that an object may set the
 * visualization width without having any other information about the
 * visualization
 * 
 * @author entitycs
 * 
 */
public class TavVisualizationWidth extends Number
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Number width;

	public TavVisualizationWidth()
	{
		width = 500;
	}

	@Override
	public int intValue()
	{
		// TODO Auto-generated method stub
		return width.intValue();
	}

	@Override
	public long longValue()
	{
		// TODO Auto-generated method stub
		return width.longValue();
	}

	@Override
	public float floatValue()
	{
		// TODO Auto-generated method stub
		return width.floatValue();
	}

	@Override
	public double doubleValue()
	{
		// TODO Auto-generated method stub
		return width.doubleValue();
	}

	public void setValue(Number newWidth)
	{
		width = newWidth;
	}
}
