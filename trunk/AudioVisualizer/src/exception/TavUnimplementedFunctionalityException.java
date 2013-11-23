package exception;

public class TavUnimplementedFunctionalityException extends Exception
{

	private static final long serialVersionUID = 1L;

	public TavUnimplementedFunctionalityException()
	{
		super();
	}

	public TavUnimplementedFunctionalityException(String message)
	{
		super(message);
	}

	public TavUnimplementedFunctionalityException(String message,
			Throwable cause)
	{
		super(message, cause);
	}

	public TavUnimplementedFunctionalityException(Throwable cause)
	{
		super(cause);
	}
}
