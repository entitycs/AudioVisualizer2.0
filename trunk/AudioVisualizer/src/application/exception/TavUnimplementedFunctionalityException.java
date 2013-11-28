package application.exception;

/**
 * This exception is thrown when a media player or visualizer fails to implement
 * ADVANCED functionality beyond the minimum functionality these objects are
 * required to have. The application should not exit but the management layer
 * looks to catch this exception and act appropriately (e.g. do not attempt to
 * use the advanced functionality as it may cause null pointer exceptions).
 * 
 */
public class TavUnimplementedFunctionalityException extends Exception
{

	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor.
	 */
	public TavUnimplementedFunctionalityException()
	{
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            is the message to send with the exception
	 */
	public TavUnimplementedFunctionalityException(String message)
	{
		super (message);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            is the message to send with the exception
	 * @param cause
	 *            is the exception which caused this exception to occur
	 */
	public TavUnimplementedFunctionalityException(String message,
			Throwable cause)
	{
		super (message, cause);
	}

	/**
	 * Constructor.
	 * 
	 * @param cause
	 *            is the exception which caused this exception to occur
	 */
	public TavUnimplementedFunctionalityException(Throwable cause)
	{
		super (cause);
	}
}
