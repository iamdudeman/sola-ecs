package technology.sola.ecs.exception;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * Exception thrown when the max entity limit is reached for a {@link technology.sola.ecs.World}.
 */
@NullMarked
public class WorldEntityLimitException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -2660810517587662465L;

  /**
   * Creates a new instance of this exception.
   *
   * @param totalEntities the total entities created
   * @param maxEntities   the maximum allowed entities
   */
  public WorldEntityLimitException(int totalEntities, int maxEntities) {
    super("World Entity limit of [" + maxEntities + "] exceeded (total: " + totalEntities + ")");
  }
}
