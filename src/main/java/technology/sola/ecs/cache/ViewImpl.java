package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.ViewEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The View class is a container of {@link ViewEntry} that are updated whenever a {@link technology.sola.ecs.World}
 * updates an {@link Entity}'s components that now satisfies or no longer satisfies the {@link List} of watched
 * {@link Component}s.
 *
 * @param <E> the {@link ViewEntry} implementation
 */
public abstract class ViewImpl<E extends ViewEntry> implements View<E> {
  private final List<Class<? extends Component>> componentClasses;
  private final Map<Integer, E> entryMap = new HashMap<>();

  /**
   * Creates a new view watching desired components.
   *
   * @param componentClasses the {@link Component} classes to watch
   */
  public ViewImpl(List<Class<? extends Component>> componentClasses) {
    this.componentClasses = componentClasses;
  }

  /**
   * @return the {@link List} of {@link ViewEntry} in this View
   */
  public List<E> getEntries() {
    return new ArrayList<>(entryMap.values());
  }

  /**
   * Checks if this view responds to an {@link Entity} adding or removing the component type
   * or if an entity is deleted while having the component type.
   *
   * @param componentClassToCheck the {@link Component} class to check
   * @return true if view will update
   */
  public boolean isWatchingComponent(Class<? extends Component> componentClassToCheck) {
    for (var componentClass : componentClasses) {
      if (componentClass.equals(componentClassToCheck)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Creates an {@link ViewEntry} from a {@link Entity}.
   *
   * @param entity the {@code Entity }to create a {@code ViewEntry} from
   * @return the {@code ViewEntry}
   */
  protected abstract E createEntryFromEntity(Entity entity);

  void addEntryIfValidEntity(Entity entity) {
    var entry = createEntryFromEntity(entity);

    if (entry != null) {
      entryMap.put(entity.getIndexInWorld(), entry);
    }
  }

  void updateForAddComponent(Entity entity, Class<? extends Component> componentClass) {
    if (isWatchingComponent(componentClass)) {
      // remove old one first in case component instance was updated
      updateForDeletedEntity(entity);
      addEntryIfValidEntity(entity);
    }
  }

  void updateForRemoveComponent(Entity entity, Class<? extends Component> componentClass) {
    if (isWatchingComponent(componentClass)) {
      updateForDeletedEntity(entity);
    }
  }

  void updateForDisabledStateChange(Entity entity) {
    if (entity.isDisabled()) {
      updateForDeletedEntity(entity);
    } else {
      addEntryIfValidEntity(entity);
    }
  }

  void updateForDeletedEntity(Entity entity) {
    entryMap.remove(entity.getIndexInWorld());
  }
}
