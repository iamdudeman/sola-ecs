package technology.sola.ecs.io;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.exception.ComponentJsonMapperNotFoundException;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonMapper;
import technology.sola.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

class WorldJsonMapper implements JsonMapper<World> {
  private final Map<String, JsonMapper<? extends Component>> componentJsonMappers;

  WorldJsonMapper(Map<Class<? extends Component>, JsonMapper<? extends Component>> componentJsonMappers) {
    this.componentJsonMappers = new HashMap<>();
    componentJsonMappers.forEach((key, value) -> this.componentJsonMappers.put(key.getName(), value));
  }

  @Override
  public JsonObject toJson(World world) {
    JsonArray entityArray = new JsonArray();

    world.getEntities().forEach(entity -> {
      JsonObject entityObject = new JsonObject();
      JsonArray componentsArray = new JsonArray();

      entity.getCurrentComponents().forEach(componentClass -> {
        String componentClassName = componentClass.getName();
        Component component = entity.getComponent(componentClass);
        @SuppressWarnings("unchecked")
        JsonMapper<Component> componentMapper = (JsonMapper<Component>) componentJsonMappers.get(componentClassName);

        if (componentMapper == null) {
          throw new ComponentJsonMapperNotFoundException(componentClassName);
        }

        JsonObject componentObject = componentMapper.toJson(component);

        componentObject.put("_class", componentClassName);

        componentsArray.add(componentObject);
      });

      entityObject.put("uniqueId", entity.getUniqueId());
      entityObject.put("name", entity.getName());
      entityObject.put("components", componentsArray);

      entityArray.add(entityObject);
    });

    JsonObject worldObject = new JsonObject();
    worldObject.put("maxEntityCount", world.getMaxEntityCount());
    worldObject.put("entities", entityArray);

    return worldObject;
  }

  @Override
  public World toObject(JsonObject jsonObject) {
    World world = new World(
      jsonObject.getInt("maxEntityCount")
    );

    jsonObject.getArray("entities").forEach(entityJson -> {
      JsonObject entityObject = entityJson.asObject();
      Entity entity = world.createEntity(
        entityObject.getString("uniqueId"),
        entityObject.isNull("name") ? null : entityObject.getString("name")
      );

      entityObject.getArray("components").forEach(componentJson -> {
        JsonObject componentObject = componentJson.asObject();
        String componentClassName = componentObject.getString("_class");
        JsonMapper<? extends Component> componentMapper = componentJsonMappers.get(componentClassName);

        if (componentMapper == null) {
          throw new ComponentJsonMapperNotFoundException(componentClassName);
        }

        entity.addComponent(componentMapper.toObject(componentObject));
      });
    });

    return world;
  }
}
