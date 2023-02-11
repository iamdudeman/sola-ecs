package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.ArrayList;
import java.util.List;

public class ViewCache {
  private final List<View<?>> viewList = new ArrayList<>();
  private final List<View<?>> viewsToDestroy = new ArrayList<>();

  public void addViewToCache(View<?> view) {
    viewList.add(view);
  }

  public void queueViewForDestruction(View<?> view) {
    viewsToDestroy.add(view);
  }

  public void cleanupCache() {
    for (var view : viewsToDestroy) {
      viewList.remove(view);
      view.getEntries().clear();
    }

    viewsToDestroy.clear();
  }

  public void updateForAddComponent(Entity entity, Component component) {
    for (var view : viewList) {
      view.updateForAddComponent(entity, component);
    }
  }

  public void updateCacheForRemoveComponent(Entity entity, Component component) {
    for (var view : viewList) {
      view.updateForRemoveComponent(entity, component);
    }
  }

  public void updateCacheForDeletedEntity(Entity entity) {
    for (var view : viewList) {
      view.updateForDeletedEntity(entity);
    }
  }
}
