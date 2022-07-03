package technology.sola.ecs.io;

import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.json.SolaJson;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;

/**
 * A reflection free JSON implementation of {@link WorldIo}.
 */
public class JsonWorldIo implements WorldIo {
  private final SolaJson solaJson;
  private final WorldJsonMapper worldJsonMapper;

  public JsonWorldIo(List<JsonMapper<? extends Component>> componentJsonMappers) {
    solaJson = new SolaJson();
    worldJsonMapper = new WorldJsonMapper(componentJsonMappers);
  }

  @Override
  public String stringify(World world) {
    return solaJson.stringify(world, worldJsonMapper);
  }

  @Override
  public World parse(String worldString) {
    World world = solaJson.parse(worldString, worldJsonMapper);

    WorldIo.processWorldAfterDeserialize(world);

    return world;
  }
}
