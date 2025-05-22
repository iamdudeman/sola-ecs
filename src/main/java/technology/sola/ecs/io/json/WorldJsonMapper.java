package technology.sola.ecs.io.json;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link JsonMapper} implementation for {@link World}.
 */
@NullMarked
public class WorldJsonMapper implements JsonMapper<World> {
  private final Map<String, JsonMapper<? extends Component>> componentJsonMappers;

  /**
   * Creates a new JsonWorldIo instance with desired {@link JsonMapper}s.
   *
   * @param componentJsonMappers the {@code JsonMapper}s to be used during mapping between JSON and {@link World}
   */
  public WorldJsonMapper(List<JsonMapper<? extends Component>> componentJsonMappers) {
    this.componentJsonMappers = new HashMap<>();

    for (JsonMapper<? extends Component> componentJsonMapper : componentJsonMappers) {
      this.componentJsonMappers.put(componentJsonMapper.getObjectClass().getName(), componentJsonMapper);
    }
  }

  @Override
  public Class<World> getObjectClass() {
    return World.class;
  }

  @Override
  public JsonObject toJson(World world) {
    JsonArray entityArray = new JsonArray();

    world.getEntities().forEach(entity -> {
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

      JsonObject entityObject = new JsonObject();

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
      var nameJsonElement = entityObject.get(FieldKeys.NAME);
      Entity entity = world.createEntity(
        entityObject.getString(FieldKeys.UNIQUE_ID),
        nameJsonElement.isNull() ? null : nameJsonElement.asString()
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
