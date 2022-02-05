package technology.sola.ecs.exception;

public class WorldEntityLimitException extends RuntimeException {
  public WorldEntityLimitException(int totalEntities, int maxEntities) {
    super("World Entity limit of [" + maxEntities + "] exceeded (total: " + totalEntities + ")");
  }
}
