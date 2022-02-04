package technology.sola.ecs.io;

import technology.sola.ecs.World;

public interface WorldIo {
  String stringify(World world);

  World parse(String worldString);
}
