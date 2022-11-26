package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// todo consider a mechanism where views are cached
//  when entities are created or destroyed view updates
//  when components added/removed view updates
//  might instead be at the EcsSystem level instead of view


/**
 * EcsViewFactory handles creating views of {@link technology.sola.ecs.Entity} that have desired {@link Component}s.
 */
public class EcsViewFactory implements Serializable {
  @Serial
  private static final long serialVersionUID = -8949269697037114181L;
  private final World world;

  /**
   * Creates an {@link EcsViewFactory} for a {@link World}
   *
   * @param world the {@code World} to create an {@code EcsViewFactory} for
   */
  public EcsViewFactory(World world) {
    this.world = world;
  }

  /**
   * Returns a view of {@link technology.sola.ecs.Entity} that have one {@link Component}.
   *
   * @param c1Class the class for the first component
   * @return view of {@link technology.sola.ecs.Entity} that have one {@link Component}
   */
  public final <C1 extends Component>
  List<EcsView1<C1>> of(
    Class<C1> c1Class
  ) {
    var views = new ArrayList<EcsView1<C1>>();

    for (Entity entity : world.getEnabledEntities()) {
      var c1 = entity.getComponent(c1Class);

      if (c1 != null) {
        views.add(new EcsView1<>(entity, c1));
      }
    }

    return views;
  }

  /**
   * Returns a view of {@link technology.sola.ecs.Entity} that have two {@link Component}s.
   *
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @return view of {@link technology.sola.ecs.Entity} that have two {@link Component}s
   */
  public final <C1 extends Component, C2 extends Component>
  List<EcsView2<C1, C2>> of(
    Class<C1> c1Class, Class<C2> c2Class
  ) {
    var views = new ArrayList<EcsView2<C1, C2>>();

    for (Entity entity : world.getEnabledEntities()) {
      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);

      if (c1 != null && c2 != null) {
        views.add(new EcsView2<>(entity, c1, c2));
      }
    }

    return views;
  }

  /**
   * Returns a view of {@link technology.sola.ecs.Entity} that have three {@link Component}s.
   *
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @param c3Class the class for the third component
   * @return view of {@link technology.sola.ecs.Entity} that have three {@link Component}s
   */
  public final <C1 extends Component, C2 extends Component, C3 extends Component>
  List<EcsView3<C1, C2, C3>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class
  ) {
    var views = new ArrayList<EcsView3<C1, C2, C3>>();

    for (Entity entity : world.getEnabledEntities()) {
      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);

      if (c1 != null && c2 != null && c3 != null) {
        views.add(new EcsView3<>(entity, c1, c2, c3));
      }
    }

    return views;
  }

  /**
   * Returns a view of {@link technology.sola.ecs.Entity} that have four {@link Component}s.
   *
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @param c3Class the class for the third component
   * @param c4Class the class for the fourth component
   * @return view of {@link technology.sola.ecs.Entity} that have four {@link Component}s
   */
  public final <C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component>
  List<EcsView4<C1, C2, C3, C4>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class
  ) {
    var views = new ArrayList<EcsView4<C1, C2, C3, C4>>();

    for (Entity entity : world.getEnabledEntities()) {
      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);
      var c4 = entity.getComponent(c4Class);

      if (c1 != null && c2 != null && c3 != null && c4 != null) {
        views.add(new EcsView4<>(entity, c1, c2, c3, c4));
      }
    }

    return views;
  }

  /**
   * Returns a view of {@link technology.sola.ecs.Entity} that have five {@link Component}s.
   *
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @param c3Class the class for the third component
   * @param c4Class the class for the fourth component
   * @param c5Class the class for the fifth component
   * @return view of {@link technology.sola.ecs.Entity} that have five {@link Component}s
   */
  public final <C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component, C5 extends Component>
  List<EcsView5<C1, C2, C3, C4, C5>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class, Class<C5> c5Class
  ) {
    var views = new ArrayList<EcsView5<C1, C2, C3, C4, C5>>();

    for (Entity entity : world.getEnabledEntities()) {
      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);
      var c4 = entity.getComponent(c4Class);
      var c5 = entity.getComponent(c5Class);

      if (c1 != null && c2 != null && c3 != null && c4 != null && c5 != null) {
        views.add(new EcsView5<>(entity, c1, c2, c3, c4, c5));
      }
    }

    return views;
  }

  /**
   * Returns a view of {@link technology.sola.ecs.Entity} that have six {@link Component}s.
   *
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @param c3Class the class for the third component
   * @param c4Class the class for the fourth component
   * @param c5Class the class for the fifth component
   * @param c6Class the class for the sixth component
   * @return view of {@link technology.sola.ecs.Entity} that have six {@link Component}s
   */
  public final <C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component, C5 extends Component, C6 extends Component>
  List<EcsView6<C1, C2, C3, C4, C5, C6>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class, Class<C5> c5Class, Class<C6> c6Class
  ) {
    var views = new ArrayList<EcsView6<C1, C2, C3, C4, C5, C6>>();

    for (Entity entity : world.getEnabledEntities()) {
      var c1 = entity.getComponent(c1Class);
      var c2 = entity.getComponent(c2Class);
      var c3 = entity.getComponent(c3Class);
      var c4 = entity.getComponent(c4Class);
      var c5 = entity.getComponent(c5Class);
      var c6 = entity.getComponent(c6Class);

      if (c1 != null && c2 != null && c3 != null && c4 != null && c5 != null && c6 != null) {
        views.add(new EcsView6<>(entity, c1, c2, c3, c4, c5, c6));
      }
    }

    return views;
  }
}
