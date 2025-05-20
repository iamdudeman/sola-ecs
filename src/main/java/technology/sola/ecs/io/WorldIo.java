package technology.sola.ecs.io;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;

/**
 * Defines the API contract for serializing and deserializing a {@link World}.
 */
@NullMarked
public interface WorldIo {
  /**
   * Serializes a {@link World} into a string.
   *
   * @param world the {@code World} to serialize
   * @return the serialized {@code World}
   */
  String stringify(World world);

  /**
   * Deserializes a string into a {@link World}.
   *
   * @param worldString the string to deserialize
   * @return the deserialized {@code World}
   */
  World parse(String worldString);

  /**
   * Loops through each {@link technology.sola.ecs.Entity} in a {@link World} and calls
   * {@link technology.sola.ecs.Component#afterDeserialize(World)} for each {@link technology.sola.ecs.Component} of the
   * {@code Entity}.
   *
   * @param world the {@code World} to process
   */
  static void processWorldAfterDeserialize(World world) {
    for (Entity entity : world.getEntities()) {
      for (Class<? extends Component> componentClass : entity.getCurrentComponents()) {
        entity.getComponent(componentClass).afterDeserialize(world);
      }
    }
  }
}
