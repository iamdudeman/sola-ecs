package technology.sola.ecs.exception;

import java.io.Serial;

/**
 * Exception thrown when the max entity limit is reached for a {@link technology.sola.ecs.World}.
 */
public class WorldEntityLimitException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -2660810517587662465L;

  public WorldEntityLimitException(int totalEntities, int maxEntities) {
    super("World Entity limit of [" + maxEntities + "] exceeded (total: " + totalEntities + ")");
  }
}
