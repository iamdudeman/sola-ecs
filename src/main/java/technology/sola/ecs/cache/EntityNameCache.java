package technology.sola.ecs.cache;

import technology.sola.ecs.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * The EntityNameCache class handles caching {@link Entity} by their name for quicker searching by name.
 */
public class EntityNameCache {
  private final Map<String, Entity> nameToEntityMap = new HashMap<>();

  /**
   * Returns the {@link Entity} with desired name or null if not found.
   *
   * @param name the name to check the cache for
   * @return the desired {@code Entity} or null
   */
  public Entity get(String name) {
    return nameToEntityMap.get(name);
  }

  /**
   * Updates the cache for an {@link Entity} using its previous name as reference. This will remove the entity from its
   * previous name and add it for its new one.
   *
   * @param entity       the {@code Entity} to update the cache for
   * @param previousName the previous name of the {@code Entity}
   */
  public void update(Entity entity, String previousName) {
    remove(previousName);

    String name = entity.getName();

    if (name != null) {
      nameToEntityMap.put(name, entity);
    }
  }

  /**
   * Removes an {@link Entity} from the cache by its name.
   *
   * @param name the name of the {@code Entity} to remove
   */
  public void remove(String name) {
    if (name != null) {
      nameToEntityMap.remove(name);
    }
  }
}
