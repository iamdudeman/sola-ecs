package technology.sola.ecs;

/**
 * Holds data for an {@link Entity} that can be acted on via an {@link EcsSystem}.
 */
public interface Component {
  /**
   * Called after a {@link World} has been deserialized via {@link technology.sola.ecs.io.WorldIo#parse(String)}.
   *
   * @param world the {@code World} after deserialization
   */
  default void afterDeserialize(World world) {
    // Does nothing by default
  }
}
