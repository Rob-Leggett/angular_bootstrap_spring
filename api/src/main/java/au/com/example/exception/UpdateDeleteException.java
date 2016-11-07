package au.com.example.exception;

public class UpdateDeleteException extends Exception {
	private static final long serialVersionUID = -2751999676085328962L;

	public UpdateDeleteException(String message) {
		super(message);
	}

    public UpdateDeleteException(String message, Throwable e) {
        super(message, e);
    }
}
