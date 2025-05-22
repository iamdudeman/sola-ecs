package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.cache.EntityNameCache;
import technology.sola.ecs.cache.ViewCache;
import technology.sola.ecs.exception.WorldEntityLimitException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * World contains arrays of {@link Component}s and methods for creating {@link Entity} instances and searching for
 * entities.
 */
@NullMarked
public class World {
  private final EntityNameCache entityNameCache = new EntityNameCache();
  private final ViewCache viewCache;
  private final ViewBuilder viewBuilder;
  private final int maxEntityCount;
  private final @Nullable Entity[] entities;
  private final Map<Class<? extends Component>, @Nullable Component[]> components = new HashMap<>();
  private final Function<Class<? extends Component>, @Nullable Component[]> componentsMappingFunction = (key) -> new Component[World.this.maxEntityCount];
  private final List<Entity> entitiesToDestroy = new ArrayList<>();
  private final String baseUuid = UUID.randomUUID().toString().substring(0, 8);
  private int currentEntityIndex = 0;
  private int totalEntityCount = 0;

  /**
   * Creates a new World instance with specified max {@link Entity} count.
   *
   * @param maxEntityCount the maximum number of {@code Entity} in this World, must be greater than 0
   */
  public World(int maxEntityCount) {
    if (maxEntityCount < 1) {
      throw new IllegalArgumentException("maxEntityCannot must be a positive integer greater than 0");
    }

    this.maxEntityCount = maxEntityCount;
    entities = new Entity[maxEntityCount];
    viewCache = new ViewCache(this);
    viewBuilder = new ViewBuilder(viewCache);
  }

  /**
   * Removes entities that were queued for destruction. Should be called at the end of a frame.
   */
  public void cleanupDestroyedEntities() {
    for (Entity entity : entitiesToDestroy) {
      destroyEntity(entity);
      entityNameCache.remove(entity.getName());
      viewCache.updateForDeletedEntity(entity);
    }

    entitiesToDestroy.clear();
  }

  /**
   * Gets the maximum {@link Entity} count for this world.
   *
   * @return the max {@code Entity} count
   */
  public int getMaxEntityCount() {
    return maxEntityCount;
  }

  /**
   * Gets the current number of {@link Entity} in this World.
   *
   * @return the current number of {@code Entity} in this World
   */
  public int getEntityCount() {
    return totalEntityCount;
  }

  /**
   * Creates a new {@link Entity} inside this World with a random unique id. It is initialized with a set of components.
   * <p>
   * If the total entity count goes above the max number specified in this world then an exception will be thrown.
   *
   * @param components the {@link Component}s to initialize the Entity with
   * @return a new {@code Entity}
   */
  public Entity createEntity(Component... components) {
    return createEntity(null, components);
  }

  /**
   * Creates a new {@link Entity} inside this World with a random unique id. It is initialized with name and components.
   * <p>
   * If the total entity count goes above the max number specified in this world then an exception will be thrown.
   *
   * @param name       the name to initialize this Entity with
   * @param components the {@link Component}s to initialize the Entity with
   * @return a new {@code Entity}
   */
  public Entity createEntity(@Nullable String name, Component... components) {
    return createEntity(null, name, components);
  }

  /**
   * Creates a new {@link Entity} inside this World with a set unique id. It is initialized with name and components. If
   * the provided unique id is null then one will be generated.
   * <p>
   * If the total entity count goes above the max number specified in this world then an exception will be thrown.
   *
   * @param uniqueId   the unique id to initialize this Entity with or null to generate one automatically
   * @param name       the name to initialize this Entity with
   * @param components the {@link Component}s to initialize the Entity with
   * @return a new {@code Entity}
   */
  public Entity createEntity(@Nullable String uniqueId, @Nullable String name, Component... components) {
    totalEntityCount++;

    var entityIndex = nextOpenEntityIndex();
    String entityUniqueId = uniqueId == null ? baseUuid + entityIndex : uniqueId;
    Entity entity = new Entity(this, entityIndex, entityUniqueId);

    entities[entity.getIndexInWorld()] = entity;
    entity.setName(name);

    for (Component component : components) {
      entity.addComponent(component);
    }

    return entity;
  }

  /**
   * Gets an {@link Entity} by index or null if one is not present.
   *
   * @param index the index of the {@code Entity} to retrieve
   * @return the {@code Entity}
   */
  @Nullable
  public Entity getEntityAtIndex(int index) {
    return entities[index];
  }

  /**
   * Searches for an {@link Entity} by its name. Returns null if not ofund
   *
   * @param name the name of the {@code Entity}
   * @return the {@code Entity} with desired name or null if not found
   */
  @Nullable
  public Entity findEntityByName(String name) {
    return entityNameCache.get(name);
  }

  /**
   * Searches for an {@link Entity} by its unique id. Returns null if not found.
   *
   * @param uniqueId the unique id of the {@code Entity}
   * @return the {@code Entity} with desired uniqueId or null if not found
   */
  @Nullable
  public Entity findEntityByUniqueId(String uniqueId) {
    for (Entity entity : entities) {
      if (entity == null) continue;

      if (uniqueId.equals(entity.getUniqueId())) {
        return entity;
      }
    }

    return null;
  }

  /**
   * Gets a {@link List} of all {@link Entity} in the world even if they are disabled.
   *
   * @return a {@code List} of all {@code Entity}
   */
  public List<Entity> getEntities() {
    List<Entity> entityList = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity != null) {
        entityList.add(entity);
      }
    }

    return entityList;
  }

  /**
   * Gets a {@link List} of all {@link Entity} in the world that are not disabled.
   *
   * @return a {@code List} of all enabled {@code Entity}
   */
  public List<Entity> getEnabledEntities() {
    List<Entity> entityList = new ArrayList<>(entities.length);

    for (Entity entity : entities) {
      if (entity != null && !entity.isDisabled()) {
        entityList.add(entity);
      }
    }

    return entityList;
  }

  /**
   * Gets a {@link List} of {@link Entity} where each {@code Entity} has all of the {@link Component} classes searched for
   * and is not disabled.
   *
   * @param componentClasses array of {@code Component} classes each {@code Entity} will have
   * @return a {@code List} of {@code Entity} each having the desired {@code Component}s
   */
  @SafeVarargs
  public final List<Entity> findEntitiesWithComponents(Class<? extends Component>... componentClasses) {
    List<Entity> entitiesWithAllComponents = new ArrayList<>();

    for (Entity entity : entities) {
      if (entity == null || entity.isDisabled()) {
        continue;
      }

      boolean hasAllClasses = true;

      for (Class<? extends Component> componentClass : componentClasses) {
        if (getComponentForEntity(entity.getIndexInWorld(), componentClass) == null) {
          hasAllClasses = false;
          break;
        }
      }

      if (hasAllClasses) {
        entitiesWithAllComponents.add(entity);
      }
    }

    return entitiesWithAllComponents;
  }

  /**
   * Returns a {@link ViewBuilder} instance for creating a {@link technology.sola.ecs.view.View} of
   * {@link technology.sola.ecs.view.ViewEntry} with desired {@link Component}s
   *
   * @return the {@code ViewBuilder} instance
   */
  public ViewBuilder createView() {
    return viewBuilder;
  }

  void addComponentForEntity(int entityIndex, Component component) {
    Class<? extends Component> componentClass = component.getClass();
    var entity = entities[entityIndex];

    if (entity != null) {
      @Nullable Component[] componentsOfType = components.computeIfAbsent(
        componentClass,
        componentsMappingFunction
      );

      componentsOfType[entityIndex] = component;

      viewCache.updateForAddComponent(entity, componentClass);
    }
  }

  @Nullable
  <T extends Component> T getComponentForEntity(int entityIndex, Class<T> componentClass) {
    @Nullable Component[] componentsOfType = components.computeIfAbsent(
      componentClass,
      componentsMappingFunction
    );

    return componentClass.cast(componentsOfType[entityIndex]);
  }

  List<Class<? extends Component>> getCurrentComponents(int entityIndex) {
    List<Class<? extends Component>> currentComponents = new ArrayList<>();

    for (var entry : components.entrySet()) {
      if (entry.getValue()[entityIndex] != null) {
        currentComponents.add(entry.getKey());
      }
    }

    return currentComponents;
  }

  boolean hasComponent(int entityIndex, Class<? extends Component> componentClass) {
    @Nullable Component[] componentsOfType = components.computeIfAbsent(
      componentClass,
      componentsMappingFunction
    );

    return componentsOfType[entityIndex] != null;
  }

  void removeComponent(int entityIndex, Class<? extends Component> componentClass, boolean updateViewCache) {
    var entity = entities[entityIndex];

    if (entity != null) {
      components.computeIfPresent(componentClass, (key, componentArray) -> {
        if (updateViewCache) {
          viewCache.updateForRemoveComponent(entity, componentClass);
        }
        componentArray[entityIndex] = null;

        return componentArray;
      });
    }
  }

  void queueEntityForDestruction(Entity entity) {
    // todo this check is REALLY expensive!
    if (!entitiesToDestroy.contains(entity)) {
      entitiesToDestroy.add(entity);
    }
  }

  void updateEntityNameCache(Entity entity, @Nullable String previousName) {
    entityNameCache.update(entity, previousName);
  }

  void updateDisabledStateCache(Entity entity) {
    viewCache.updateForDisabledStateChange(entity);
  }

  private void destroyEntity(Entity entity) {
    // todo should only fire if entities[entityIndex] is not null (can then probably remove the contains check in queueEntityForDestruction

    totalEntityCount--;

    var entityIndex = entity.getIndexInWorld();

    for (var componentClass : getCurrentComponents(entityIndex)) {
      removeComponent(entityIndex, componentClass, false);
    }

    entities[entityIndex] = null;
  }

  private int nextOpenEntityIndex() {
    int totalEntityCounter = 1; // Starting at 1 for this entity being created

    while (entities[currentEntityIndex] != null) {
      currentEntityIndex = (currentEntityIndex + 1) % maxEntityCount;
      totalEntityCounter++;

      if (totalEntityCounter > maxEntityCount) {
        throw new WorldEntityLimitException(totalEntityCounter, maxEntityCount);
      }
    }

    return currentEntityIndex;
  }
}
