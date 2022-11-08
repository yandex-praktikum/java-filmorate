package filmorate.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("ClassWithoutNoArgConstructor")
public class ResourceException extends RuntimeException {

    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ResourceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
