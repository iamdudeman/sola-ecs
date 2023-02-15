package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View1;
import technology.sola.ecs.view.View2;
import technology.sola.ecs.view.View3;

import java.util.*;
import java.util.function.Supplier;

public class ViewCache {
  private final Map<Integer, View<?>> builtViews = new HashMap<>();
  private final World world;

  public ViewCache(World world) {
    this.world = world;
  }

  @SuppressWarnings("unchecked")
  public <C1 extends Component> View1<C1> createView(Class<C1> c1Class) {
    return (View1<C1>) getCachedViewOrCreate(() -> new View1<>(c1Class), new Class[]{
      c1Class
    });
  }

  @SuppressWarnings("unchecked")
  public <C1 extends Component, C2 extends Component> View2<C1, C2> createView(Class<C1> c1Class, Class<C2> c2Class) {
    return (View2<C1, C2>) getCachedViewOrCreate(() -> new View2<>(c1Class, c2Class), new Class[]{
      c1Class, c2Class
    });
  }

  @SuppressWarnings("unchecked")
  public <C1 extends Component, C2 extends Component, C3 extends Component> View3<C1, C2, C3> createView(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class) {
    return (View3<C1, C2, C3>) getCachedViewOrCreate(() -> new View3<>(c1Class, c2Class, c3Class), new Class[]{
      c1Class, c2Class, c3Class
    });
  }

  public void updateForAddComponent(Entity entity, Class<? extends Component> componentClass) {
    for (var view : builtViews.values()) {
      view.updateForAddComponent(entity, componentClass);
    }
  }

  public void updateForRemoveComponent(Entity entity, Class<? extends Component> componentClass) {
    for (var view : builtViews.values()) {
      view.updateForRemoveComponent(entity, componentClass);
    }
  }

  public void updateForDeletedEntity(Entity entity) {
    for (var view : builtViews.values()) {
      view.updateForDeletedEntity(entity);
    }
  }

  private void initializeView(View<?> view) {
    for (Entity entity : world.getEntities()) {
      view.addEntryIfValidEntity(entity);
    }
  }

  private View<?> getCachedViewOrCreate(Supplier<View<?>> newViewSupplier, Class<? extends Component>[] classes) {
    if (classes == null) {
      return null;
    }

    int hash = Arrays.hashCode(classes);
    var view = builtViews.get(hash);

    if (view == null) {
      view = newViewSupplier.get();
      initializeView(view);
      builtViews.put(hash, view);
    }

    return view;
  }
}
