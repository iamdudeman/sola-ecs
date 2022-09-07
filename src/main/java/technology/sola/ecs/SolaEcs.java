package technology.sola.ecs;

import technology.sola.ecs.exception.EcsSystemNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SolaEcs is a wrapper for a collection of {@link EcsSystem}.
 */
public class SolaEcs {
  private final List<EcsSystem> ecsSystems = new ArrayList<>();
  private World world = new World(1);

  /**
   * Creates an empty {@link SolaEcs}.
   */
  public SolaEcs() {
  }

  /**
   * Creates a new {@link SolaEcs} initialized with a {@link World} and {@link EcsSystem}s.
   *
   * @param initialWorld   the {@code World} to initialize with
   * @param initialSystems the {@code EcsSystem}s to initialize with
   */
  public SolaEcs(World initialWorld, EcsSystem... initialSystems) {
    setWorld(initialWorld);
    addSystems(initialSystems);
  }

  /**
   * Returns the {@link World} the collection of {@link EcsSystem}s is currently acting on.
   *
   * @return the {@code World} that will be updated
   */
  public World getWorld() {
    return world;
  }

  /**
   * Update the {@link World} the collection of {@link EcsSystem}s acts on.
   *
   * @param world the {@code World} to update
   */
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
    for (EcsSystem ecsSystem : ecsSystems) {
      if (ecsSystem.isActive()) {
        ecsSystem.update(world, deltaTime);
      }
    }

    world.cleanupDestroyedEntities();
  }

  /**
   * Adds {@link EcsSystem}s to the collection.
   *
   * @param ecsSystems the {@code EcsSystem}s to add
   */
  public void addSystems(EcsSystem... ecsSystems) {
    for (EcsSystem ecsSystem : ecsSystems) {
      addSystem(ecsSystem);
    }
  }

  /**
   * Adds an {@link EcsSystem} to the collection.
   *
   * @param ecsSystem the {@code EcsSystem} to add
   */
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

  /**
   * Removed an {@link EcsSystem} from the collection.
   *
   * @param ecsSystem the {@code EcsSystem} to remove
   */
  public void removeSystem(EcsSystem ecsSystem) {
    ecsSystems.remove(ecsSystem);
  }

  /**
   * Returns the {@link EcsSystem} corresponding to the passed class.
   *
   * @param <T>            the type extending {@code EcsSystem}
   * @param ecsSystemClass the class for the desired {@code EcsSystem}
   * @return the {@code EcsSystem}
   * @throws EcsSystemNotFoundException when not found
   */
  public <T extends EcsSystem> T getSystem(Class<T> ecsSystemClass) {
    for (EcsSystem ecsSystem : ecsSystems) {
      if (ecsSystemClass.isInstance(ecsSystem)) {
        return ecsSystemClass.cast(ecsSystem);
      }
    }

    throw new EcsSystemNotFoundException(ecsSystemClass);
  }

  /**
   * @return a {@link List} of the current {@link EcsSystem}s
   */
  public List<EcsSystem> getSystems() {
    return ecsSystems;
  }

  /**
   * @return an {@link Iterator} for the {@link EcsSystem}s
   */
  public Iterator<EcsSystem> systemIterator() {
    return ecsSystems.iterator();
  }

  /**
   * @return an {@link Iterator} for the {@link EcsSystem}s that are currently active
   */
  public Iterator<EcsSystem> activeSystemIterator() {
    return ecsSystems.stream()
      .filter(EcsSystem::isActive)
      .iterator();
  }
}
