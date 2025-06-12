package cz.muni.fi.pb162.hw03.cmd;

/**
 * Exception used on cmd line processing errors
 */
public class CommandLineException extends RuntimeException {

    /**
     * Creates new exception
     */
    public CommandLineException() {
    }

    /**
     * Creates new exception
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public CommandLineException(String message) {
        super(message);
    }

    /**
     * Creates new exception
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public CommandLineException(String message, Throwable cause) {
        super(message, cause);
    }
}
