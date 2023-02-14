package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.ViewEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The View class is a container of {@link ViewEntry} that are updated whenever a {@link technology.sola.ecs.World}
 * updates an {@link Entity}'s components that now satisfies or no longer satisfies the {@link List} of watched
 * {@link Component}s.
 *
 * @param <E> the {@link ViewEntry} implementation
 */
public abstract class View<E extends ViewEntry> {
  private final List<Class<? extends Component>> componentClasses;
  private final List<E> entries = new ArrayList<>();

  /**
   * Creates a new view watching desired components.
   *
   * @param componentClasses the {@link Component} classes to watch
   */
  public View(List<Class<? extends Component>> componentClasses) {
    this.componentClasses = componentClasses;
  }

  /**
   * @return the {@link List} of {@link ViewEntry} in this View
   */
  public List<E> getEntries() {
    return entries;
  }

  /**
   * Creates an {@link ViewEntry} from a {@link Entity}.
   *
   * @param entity the {@code Entity }to create a {@code ViewEntry} from
   * @return the {@code ViewEntry}
   */
  protected abstract E createEntryFromEntity(Entity entity);

  void addEntityIfValidEntry(Entity entity) {
    var entry = createEntryFromEntity(entity);

    if (entry != null) {
      entries.add(entry);
    }
  }

  void updateForAddComponent(Entity entity, Component component) {
    if (isCached(entity)) {
      return;
    }

    if (isViewWatchingComponent(component.getClass())) {
      addEntityIfValidEntry(entity);
    }
  }

  void updateForRemoveComponent(Entity entity, Component component) {
    if (isViewWatchingComponent(component.getClass())) {
      updateForDeletedEntity(entity);
    }
  }

  void updateForDeletedEntity(Entity entity) {
    Iterator<E> entryIterator = entries.iterator();

    while (entryIterator.hasNext()) {
      var entry = entryIterator.next();

      if (entry.entity() == entity) {
        entryIterator.remove();
        break;
      }
    }
  }

  private boolean isViewWatchingComponent(Class<? extends Component> componentClassToCheck) {
    for (var componentClass : componentClasses) {
      if (componentClass.equals(componentClassToCheck)) {
        return true;
      }
    }

    return false;
  }

  private boolean isCached(Entity entity) {
    for (var entry : entries) {
      if (entry.entity() == entity) {
        return true;
      }
    }

    return false;
  }
}
