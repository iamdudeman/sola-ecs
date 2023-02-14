package technology.sola.ecs.cache;

import technology.sola.ecs.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityNameCache {
  private final Map<String, Entity> nameToEntityMap = new HashMap<>();

  public void add(Entity entity) {
    nameToEntityMap.put(entity.getName(), entity);
  }

  public void remove(Entity entity) {
    nameToEntityMap.remove(entity.getName());
  }

  public Entity get(String name) {
    return nameToEntityMap.get(name);
  }
}
