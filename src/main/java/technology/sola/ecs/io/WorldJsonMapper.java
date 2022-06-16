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

        componentObject.put(FieldKeys.COMPONENT_CLASS, componentClassName);

        componentsArray.add(componentObject);
      });

      entityObject.put(FieldKeys.UNIQUE_ID, entity.getUniqueId());
      entityObject.put(FieldKeys.NAME, entity.getName());
      entityObject.put(FieldKeys.COMPONENTS, componentsArray);

      entityArray.add(entityObject);
    });

    JsonObject worldObject = new JsonObject();
    worldObject.put(FieldKeys.MAX_ENTITY_COUNT, world.getMaxEntityCount());
    worldObject.put(FieldKeys.ENTITIES, entityArray);

    return worldObject;
  }

  @Override
  public World toObject(JsonObject jsonObject) {
    World world = new World(
      jsonObject.getInt(FieldKeys.MAX_ENTITY_COUNT)
    );

    jsonObject.getArray(FieldKeys.ENTITIES).forEach(entityJson -> {
      JsonObject entityObject = entityJson.asObject();
      Entity entity = world.createEntity(
        entityObject.getString(FieldKeys.UNIQUE_ID),
        entityObject.isNull(FieldKeys.NAME) ? null : entityObject.getString(FieldKeys.NAME)
      );

      entityObject.getArray(FieldKeys.COMPONENTS).forEach(componentJson -> {
        JsonObject componentObject = componentJson.asObject();
        String componentClassName = componentObject.getString(FieldKeys.COMPONENT_CLASS);
        JsonMapper<? extends Component> componentMapper = componentJsonMappers.get(componentClassName);

        if (componentMapper == null) {
          throw new ComponentJsonMapperNotFoundException(componentClassName);
        }

        entity.addComponent(componentMapper.toObject(componentObject));
      });
    });

    return world;
  }

  private static class FieldKeys {
    static final String COMPONENT_CLASS = "_class";
    static final String COMPONENTS = "components";
    static final String UNIQUE_ID = "uniqueId";
    static final String NAME = "name";
    static final String ENTITIES = "entities";
    static final String MAX_ENTITY_COUNT = "maxEntityCount";
  }
}
