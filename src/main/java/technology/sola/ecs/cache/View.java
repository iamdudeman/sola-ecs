package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.ViewEntry;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class View<E extends ViewEntry> {
  private final ViewCache viewCache;
  private final List<Class<? extends Component>> componentClasses;
  private final List<E> entries = new LinkedList<>();

  public View(ViewCache viewCache, List<Class<? extends Component>> componentClasses) {
    this.viewCache = viewCache;
    this.componentClasses = componentClasses;
  }

  public List<E> getEntries() {
    return entries;
  }

  public void delete() {
    viewCache.queueViewForDestruction(this);
  }

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

  protected void initializeView(World world) {
    for (Entity entity : world.getEntities()) {
      addEntityIfValidEntry(entity);
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
