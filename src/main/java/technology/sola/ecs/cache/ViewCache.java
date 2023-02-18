package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View1;
import technology.sola.ecs.view.View2;
import technology.sola.ecs.view.View3;
import technology.sola.ecs.view.View4;

import java.util.*;
import java.util.function.Supplier;

/**
 * The ViewCache classes handles caching and updating {@link ViewImpl} instances when {@link Entity} modifications are made
 * in a {@link World}.
 */
public class ViewCache {
  private final Map<Integer, ViewImpl<?>> builtViews = new HashMap<>();
  private final World world;

  /**
   * Creates a ViewCache instance for a {@link World}
   *
   * @param world the {@code World} instance
   */
  public ViewCache(World world) {
    this.world = world;
  }

  /**
   * Gets a {@link View1} for the desired component class from the cache or creates it if it does not exist.
   *
   * @param c1Class the {@link Component} class
   * @param <C1>    the component type
   * @return the {@code View1}
   */
  @SuppressWarnings("unchecked")
  public <C1 extends Component> View1<C1> createView(Class<C1> c1Class) {
    return (View1<C1>) getCachedViewOrCreate(() -> new View1<>(c1Class), new Class[]{
      c1Class
    });
  }

  /**
   * Gets a {@link View2} for the desired component classes from the cache or creates it if it does not exist.
   *
   * @param c1Class the first {@link Component} class
   * @param c2Class the second {@code Component} class
   * @param <C1>    the first component type
   * @param <C2>    the second component type
   * @return the {@code View2}
   */
  @SuppressWarnings("unchecked")
  public <C1 extends Component, C2 extends Component> View2<C1, C2> createView(Class<C1> c1Class, Class<C2> c2Class) {
    return (View2<C1, C2>) getCachedViewOrCreate(() -> new View2<>(c1Class, c2Class), new Class[]{
      c1Class, c2Class
    });
  }

  /**
   * Gets a {@link View3} for the desired component classes from the cache or creates it if it does not exist.
   *
   * @param c1Class the first {@link Component} class
   * @param c2Class the second {@code Component} class
   * @param c3Class the third {@code Component} class
   * @param <C1>    the first component type
   * @param <C2>    the second component type
   * @param <C3>    the third component type
   * @return the {@code View3}
   */
  @SuppressWarnings("unchecked")
  public <C1 extends Component, C2 extends Component, C3 extends Component> View3<C1, C2, C3> createView(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class) {
    return (View3<C1, C2, C3>) getCachedViewOrCreate(() -> new View3<>(c1Class, c2Class, c3Class), new Class[]{
      c1Class, c2Class, c3Class
    });
  }

  /**
   * Gets a {@link View4} for the desired component classes from the cache or creates it if it does not exist.
   *
   * @param c1Class the first {@link Component} class
   * @param c2Class the second {@code Component} class
   * @param c3Class the third {@code Component} class
   * @param c4Class the fourth {@code Component} class
   * @param <C1>    the first component type
   * @param <C2>    the second component type
   * @param <C3>    the third component type
   * @param <C4>    the fourth component type
   * @return the {@code View3}
   */
  @SuppressWarnings("unchecked")
  public <C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component> View4<C1, C2, C3, C4> createView(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class) {
    return (View4<C1, C2, C3, C4>) getCachedViewOrCreate(() -> new View4<>(c1Class, c2Class, c3Class, c4Class), new Class[]{
      c1Class, c2Class, c3Class, c4Class
    });
  }

  /**
   * Updates cached {@link ViewImpl}s for an {@link Entity}'s added {@link Component}.
   *
   * @param entity         the {@code Entity} updated
   * @param componentClass the class of the {@code Component} added to the entity
   */
  public void updateForAddComponent(Entity entity, Class<? extends Component> componentClass) {
    for (var view : builtViews.values()) {
      view.updateForAddComponent(entity, componentClass);
    }
  }

  /**
   * Updates cached {@link ViewImpl}s for an {@link Entity}'s removed {@link Component}.
   *
   * @param entity         the {@code Entity} updated
   * @param componentClass the class of the {@code Component} removed from the entity
   */
  public void updateForRemoveComponent(Entity entity, Class<? extends Component> componentClass) {
    for (var view : builtViews.values()) {
      view.updateForRemoveComponent(entity, componentClass);
    }
  }

  /**
   * Updates cached {@link ViewImpl}s for a deleted {@link Entity}.
   *
   * @param entity the {@code Entity} deleted
   */
  public void updateForDeletedEntity(Entity entity) {
    for (var view : builtViews.values()) {
      view.updateForDeletedEntity(entity);
    }
  }

  private ViewImpl<?> getCachedViewOrCreate(Supplier<ViewImpl<?>> newViewSupplier, Class<? extends Component>[] classes) {
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

  private void initializeView(ViewImpl<?> view) {
    for (Entity entity : world.getEntities()) {
      view.addEntryIfValidEntity(entity);
    }
  }
}
