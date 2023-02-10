package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class EcsView<EcsViewEntry extends ViewEntry> {
  private final List<Class<? extends Component>> componentClasses;
  private final List<EcsViewEntry> entries = new LinkedList<>();

  public EcsView(List<Class<? extends Component>> componentClasses) {
    this.componentClasses = componentClasses;
  }

  public abstract EcsViewEntry createEntryFromEntity(Entity entity);

  public List<Class<? extends Component>> getComponentClasses() {
    return componentClasses;
  }

  public List<EcsViewEntry> getEntries() {
    return entries;
  }

  public void updateCacheForAddComponent(Entity entity, Component component) {
    if (isCached(entity)) {
      return;
    }

    if (isViewWatchingComponent(component.getClass())) {
      for (var componentClass : componentClasses) {
        if (!entity.hasComponent(componentClass)) {
          return;
        }
      }

      entries.add(createEntryFromEntity(entity));
    }
  }

  public void updateCacheForRemoveComponent(Entity entity, Component component) {
    if (isViewWatchingComponent(component.getClass())) {
      updateCacheForDeletedEntity(entity);
    }
  }

  public void updateCacheForDeletedEntity(Entity entity) {
    Iterator<EcsViewEntry> entryIterator = entries.iterator();

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
