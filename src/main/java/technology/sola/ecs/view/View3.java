package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.View;

import java.util.List;

public class View3<C1 extends Component, C2 extends Component, C3 extends Component> extends View<View3.View3Entry<C1, C2, C3>> {
  private final Class<C1> c1Class;
  private final Class<C2> c2Class;
  private final Class<C3> c3Class;

  public View3(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class) {
    super(List.of(c1Class));
    this.c1Class = c1Class;
    this.c2Class = c2Class;
    this.c3Class = c3Class;
  }

  @Override
  protected View3Entry<C1, C2, C3> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    C2 c2 = entity.getComponent(c2Class);
    if (c2 == null) return null;

    C3 c3 = entity.getComponent(c3Class);
    if (c3 == null) return null;

    return new View3Entry<>(entity, c1, c2, c3);
  }

  public record View3Entry<C1 extends Component, C2 extends Component, C3 extends Component>(Entity entity, C1 c1, C2 c2, C3 c3) implements ViewEntry {
  }
}
