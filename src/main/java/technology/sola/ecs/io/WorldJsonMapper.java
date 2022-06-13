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
        Component component = entity.getComponent(componentClass);
        JsonMapper componentMapper = componentJsonMappers.get(componentClass.getName());

        if (componentMapper == null) {
          throw new ComponentJsonMapperNotFoundException(componentClass.getName());
        }

        JsonObject componentObject = componentMapper.toJson(component);

        componentObject.put("_class", componentClass.getName());

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
        JsonMapper<? extends Component> componentMapper = componentJsonMappers.get(componentObject.getString("_class"));

        if (componentMapper == null) {
          throw new ComponentJsonMapperNotFoundException(componentObject.getString("class"));
        }

        entity.addComponent(componentMapper.toObject(componentObject));
      });
    });

    return world;
  }
}
