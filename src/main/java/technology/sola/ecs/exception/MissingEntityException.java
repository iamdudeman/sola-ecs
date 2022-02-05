package technology.sola.ecs.exception;

import java.io.Serial;

public class MissingEntityException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 8699650228232847952L;

  public MissingEntityException(int id) {
    super("There is no Entity with id of [" + id + "] in this World");
  }
}
