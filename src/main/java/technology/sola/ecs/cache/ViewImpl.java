package technology.sola.ecs.cache;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.ViewEntry;

import java.util.Collection;
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
@NullMarked
public abstract class ViewImpl<E extends ViewEntry> implements View<E> {
  private final List<Class<? extends Component>> componentClasses;
  private final Map<Integer, E> entryMap = new HashMap<>();
  private ViewCache viewCache;

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
  public Collection<E> getEntries() {
    return entryMap.values();
  }

  @Override
  public void destroy() {
    var componentClassesArray = new Class[componentClasses.size()];
    
    for (int i = 0; i < componentClassesArray.length; i++) {
      componentClassesArray[i] = componentClasses.get(i);
    }

    viewCache.destroyView(componentClassesArray);
    entryMap.clear();
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
  @Nullable
  protected abstract E createEntryFromEntity(Entity entity);

  void addEntryIfValidEntity(Entity entity) {
    E entry = createEntryFromEntity(entity);

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

  ViewImpl<E> setViewCache(ViewCache viewCache) {
    this.viewCache = viewCache;

    return this;
  }
}
