package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View1;
import technology.sola.ecs.view.View2;

import java.util.*;

public class ViewCache {
  private final Map<Integer, View<?>> builtViews = new HashMap<>();
  private final World world;

  public ViewCache(World world) {
    this.world = world;
  }

  public <C1 extends Component> View1<C1> createView(Class<C1> c1Class) {
    int hash = Objects.hash(c1Class);
    var view = builtViews.get(hash);

    if (view == null) {
      view = new View1<>(c1Class);
      initializeView(view);

      builtViews.put(hash, view);
    }

    return (View1<C1>) view;
  }

  public <C1 extends Component, C2 extends Component> View2<C1, C2> createView(Class<C1> c1Class, Class<C2> c2Class) {
    int hash = Objects.hash(c1Class, c2Class);
    var view = builtViews.get(hash);

    if (view == null) {
      view = new View2<>(c1Class, c2Class);
      initializeView(view);

      builtViews.put(hash, view);
    }

    return (View2<C1, C2>) view;
  }

  public void updateForAddComponent(Entity entity, Component component) {
    for (var view : builtViews.values()) {
      view.updateForAddComponent(entity, component);
    }
  }

  public void updateCacheForRemoveComponent(Entity entity, Component component) {
    for (var view : builtViews.values()) {
      view.updateForRemoveComponent(entity, component);
    }
  }

  public void updateCacheForDeletedEntity(Entity entity) {
    for (var view : builtViews.values()) {
      view.updateForDeletedEntity(entity);
    }
  }

  private void initializeView(View<?> view) {
    for (Entity entity : world.getEntities()) {
      view.addEntityIfValidEntry(entity);
    }
  }
}
