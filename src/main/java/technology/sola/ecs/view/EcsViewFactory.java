package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.World;

import java.util.List;
import java.util.Objects;

public class EcsViewFactory {
  private final World world;

  public EcsViewFactory(World world) {
    this.world = world;
  }

  public final <C1 extends Component<?>>
  List<EcsView1<C1>> of(
    Class<C1> c1Class
  ) {
    return world.getAllEnabledEntities()
      .stream()
      .map(entity -> {
        var c1 = entity.getComponent(c1Class);

        return c1 == null
          ? null
          : new EcsView1<>(entity, c1);
      })
      .filter(Objects::nonNull)
      .toList();
  }

  public final <C1 extends Component<?>, C2 extends Component<?>>
  List<EcsView2<C1, C2>> of(
    Class<C1> c1Class, Class<C2> c2Class
  ) {
    return world.getAllEnabledEntities()
      .stream()
      .map(entity -> {
        var c1 = entity.getComponent(c1Class);
        var c2 = entity.getComponent(c2Class);

        return c1 == null || c2 == null
          ? null
          : new EcsView2<>(entity, c1, c2);
      })
      .filter(Objects::nonNull)
      .toList();
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>>
  List<EcsView3<C1, C2, C3>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class
  ) {
    return world.getAllEnabledEntities()
      .stream()
      .map(entity -> {
        var c1 = entity.getComponent(c1Class);
        var c2 = entity.getComponent(c2Class);
        var c3 = entity.getComponent(c3Class);

        return c1 == null || c2 == null || c3 == null
          ? null
          : new EcsView3<>(entity, c1, c2, c3);
      })
      .filter(Objects::nonNull)
      .toList();
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>, C4 extends Component<?>>
  List<EcsView4<C1, C2, C3, C4>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class
  ) {
    return world.getAllEnabledEntities()
      .stream()
      .map(entity -> {
        var c1 = entity.getComponent(c1Class);
        var c2 = entity.getComponent(c2Class);
        var c3 = entity.getComponent(c3Class);
        var c4 = entity.getComponent(c4Class);

        return c1 == null || c2 == null || c3 == null
          ? null
          : new EcsView4<>(entity, c1, c2, c3, c4);
      })
      .filter(Objects::nonNull)
      .toList();
  }

  public final <C1 extends Component<?>, C2 extends Component<?>, C3 extends Component<?>, C4 extends Component<?>, C5 extends Component<?>>
  List<EcsView5<C1, C2, C3, C4, C5>> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class, Class<C5> c5Class
  ) {
    return world.getAllEnabledEntities()
      .stream()
      .map(entity -> {
        var c1 = entity.getComponent(c1Class);
        var c2 = entity.getComponent(c2Class);
        var c3 = entity.getComponent(c3Class);
        var c4 = entity.getComponent(c4Class);
        var c5 = entity.getComponent(c5Class);

        return c1 == null || c2 == null || c3 == null
          ? null
          : new EcsView5<>(entity, c1, c2, c3, c4, c5);
      })
      .filter(Objects::nonNull)
      .toList();
  }
}
