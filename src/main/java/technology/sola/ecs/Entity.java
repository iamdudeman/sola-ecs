package technology.sola.ecs;

import java.util.ArrayList;
import java.util.List;

/**
 * An Entity is identified by its index in the {@link World} that ties a set of {@link Component}s together.
 */
public class Entity {
  private final int entityIndex;
  private final String uniqueId;
  private final World world;
  private final List<Class<? extends Component>> currentComponents = new ArrayList<>();
  private String name = null;
  private boolean isDisabled = false;

  /**
   * Gets the integer id of this Entity.
   *
   * @return the id of this Entity
   */
  public int getIndexInWorld() {
    return entityIndex;
  }

  /**
   * Gets the unique id of this entity that persists through serialization and deserialization.
   *
   * @return the unique id of the Entity
   */
  public String getUniqueId() {
    return uniqueId;
  }

  /**
   * Gets the name of this Entity. A name is optional and will be null if not manually set.
   *
   * @return the name of the Entity
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this Entity. A name can be used to look up an Entity via {@link World#findEntityByName(String)}.
   *
   * @param name the name of the Entity
   * @return this Entity
   */
  public Entity setName(String name) {
    String previousName = this.name;
    this.name = name;

    world.updateEntityNameCache(this, previousName);

    return this;
  }

  /**
   * Returns whether this Entity should be considered disabled or not.
   *
   * @return true if this Entity is disabled
   */
  public boolean isDisabled() {
    return isDisabled;
  }

  /**
   * Updates the disabled state of this Entity.
   *
   * @param disabled the new disabled state of this Entity
   * @return this
   */
  public Entity setDisabled(boolean disabled) {
    isDisabled = disabled;
    world.updateDisabledStateCache(this);

    return this;
  }

  /**
   * Queues this Entity for destruction. This typically should happen at the end of the current frame.
   */
  public void destroy() {
    world.queueEntityForDestruction(this);
  }

  /**
   * Adds a {@link Component} to this Entity.
   *
   * @param component the {@code Component} to add
   * @return this Entity
   */
  public Entity addComponent(Component component) {
    world.addComponentForEntity(entityIndex, component);

    Class<? extends Component> componentClass = component.getClass();

    if (!currentComponents.contains(componentClass)) {
      currentComponents.add(componentClass);
    }

    return this;
  }

  /**
   * Gets a {@link Component} instance for the class passed in.
   *
   * @param componentClass the class of the {@code Component} to get
   * @param <T>            the type of the {@code Component} class to get
   * @return the {@code Component} instance or null
   */
  public <T extends Component> T getComponent(Class<T> componentClass) {
    return world.getComponentForEntity(entityIndex, componentClass);
  }

  /**
   * Returns true if this {@link Entity} has the {@link Component}.
   *
   * @param <T>            the type extending {@code Component}
   * @param componentClass {@code Component} class to check for
   * @return true if {@code Entity} has the {@code Component}
   */
  public <T extends Component> boolean hasComponent(Class<T> componentClass) {
    return currentComponents.contains(componentClass);
  }

  /**
   * Removes a {@link Component} from this Entity.
   *
   * @param componentClassToRemove the Class of the {@code Component} to remove
   */
  public void removeComponent(Class<? extends Component> componentClassToRemove) {
    world.removeComponent(entityIndex, componentClassToRemove);
    currentComponents.remove(componentClassToRemove);
  }

  /**
   * Returns a list of the current {@link Component} classes that this entity has.
   *
   * @return the list of {@code Component} classes
   */
  public List<Class<? extends Component>> getCurrentComponents() {
    return currentComponents;
  }

  Entity(World world, int entityIndex, String uniqueId) {
    this.world = world;
    this.entityIndex = entityIndex;
    this.uniqueId = uniqueId;
  }
}
