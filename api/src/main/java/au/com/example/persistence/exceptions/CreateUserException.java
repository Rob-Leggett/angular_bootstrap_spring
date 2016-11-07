package au.com.example.persistence.exceptions;

public class CreateUserException extends RuntimeException {
    private static final long serialVersionUID = -8712337910120088658L;

    public CreateUserException(String message) {
        super(message);
    }
}
