package technology.sola.ecs.exception;

import java.io.Serial;

public class EcsException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -5303251140826169872L;

  public EcsException(String message) {
    super(message);
  }
}
