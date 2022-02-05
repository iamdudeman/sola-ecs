package technology.sola.ecs.io;

import technology.sola.ecs.World;

public interface WorldIo {
  /**
   * Serializes a {@link World} into a string.
   *
   * @param world  the {@code World} to serialize
   * @return the serialized {@code World}
   */
  String stringify(World world);

  /**
   * Deserializes a string into a {@link World}.
   *
   * @param worldString  the string to deserialize
   * @return the deserialized {@code World}
   */
  World parse(String worldString);
}
