package technology.sola.ecs;

import technology.sola.ecs.exception.EcsSystemNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SolaEcs {
  private final List<EcsSystem> ecsSystems = new ArrayList<>();
  private World world = new World(1);

  public SolaEcs() {
  }

  public SolaEcs(World initialWorld, EcsSystem... initialSystems) {
    setWorld(initialWorld);
    addSystems(initialSystems);
  }

  public World getWorld() {
    return world;
  }

  public void setWorld(World world) {
    this.world = world;
  }

  /**
   * Called when the {@link World} should be updated. All active {@link EcsSystem#update} methods will be called.
   * After these updates any {@link Entity} that were queued to be destroyed will be cleaned up.
   *
   * @param deltaTime the delta time between updates
   */
  public void updateWorld(float deltaTime) {
    activeSystemIterator().forEachRemaining(updateSystem -> updateSystem.update(world, deltaTime));
    world.cleanupDestroyedEntities();
  }

  public void addSystems(EcsSystem... ecsSystems) {
    for (EcsSystem ecsSystem : ecsSystems) {
      addSystem(ecsSystem);
    }
  }

  public void addSystem(EcsSystem ecsSystem) {
    int insertIndex = 0;

    for (EcsSystem orderedUpdateSystem : ecsSystems) {
      if (ecsSystem.getOrder() <= orderedUpdateSystem.getOrder()) {
        break;
      }

      insertIndex++;
    }

    ecsSystems.add(insertIndex, ecsSystem);
  }

  public <T extends EcsSystem> T getSystem(Class<T> ecsSystemClass) {
    return ecsSystems.stream()
      .filter(ecsSystemClass::isInstance)
      .map(ecsSystemClass::cast)
      .findFirst()
      .orElseThrow(() -> new EcsSystemNotFoundException(ecsSystemClass));
  }

  public Iterator<EcsSystem> systemIterator() {
    return ecsSystems.iterator();
  }

  public Iterator<EcsSystem> activeSystemIterator() {
    return ecsSystems.stream()
      .filter(EcsSystem::isActive)
      .iterator();
  }
}
