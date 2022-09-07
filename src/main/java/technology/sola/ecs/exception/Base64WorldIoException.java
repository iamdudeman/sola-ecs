package technology.sola.ecs.exception;

import java.io.IOException;
import java.io.Serial;

/**
 * Exception thrown when an unexpected exception happens in {@link technology.sola.ecs.io.Base64WorldIo}.
 */
public class Base64WorldIoException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -8012323785643741126L;

  /**
   * Creates a new instance of this exception when an IO issue happens.
   *
   * @param ioException the {@link IOException} cause
   */
  public Base64WorldIoException(IOException ioException) {
    super(ioException.getMessage(), ioException);
  }

  /**
   * Creates a new instance of this exception when a class is not found during deserialization.
   *
   * @param classNotFoundException the {@link ClassNotFoundException} cause
   */
  public Base64WorldIoException(ClassNotFoundException classNotFoundException) {
    super(classNotFoundException.getMessage(), classNotFoundException);
  }
}
