package technology.sola.ecs.cache;

import technology.sola.ecs.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityNameCache {
  private final Map<String, Entity> nameToEntityMap = new HashMap<>();

  public Entity get(String name) {
    return nameToEntityMap.get(name);
  }

  public void update(Entity entity, String previousName) {
    remove(previousName);

    String name = entity.getName();

    if (name != null) {
      nameToEntityMap.put(name, entity);
    }
  }

  public void remove(String name) {
    if (name != null) {
      nameToEntityMap.remove(name);
    }
  }
}
