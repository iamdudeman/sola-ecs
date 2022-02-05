package technology.sola.ecs.exception;

import java.io.Serial;

public class WorldEntityLimitException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -2660810517587662465L;

  public WorldEntityLimitException(int totalEntities, int maxEntities) {
    super("World Entity limit of [" + maxEntities + "] exceeded (total: " + totalEntities + ")");
  }
}
