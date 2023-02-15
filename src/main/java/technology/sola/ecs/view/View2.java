package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.View;

import java.util.List;

public class View2<C1 extends Component, C2 extends Component> extends View<View2.View2Entry<C1, C2>> {
  private final Class<C1> c1Class;
  private final Class<C2> c2Class;

  public View2(Class<C1> c1Class, Class<C2> c2Class) {
    super(List.of(c1Class, c2Class));
    this.c1Class = c1Class;
    this.c2Class = c2Class;
  }

  @Override
  protected View2Entry<C1, C2> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    C2 c2 = entity.getComponent(c2Class);
    if (c2 == null) return null;

    return new View2Entry<>(entity, c1, c2);
  }

  public record View2Entry<C1 extends Component, C2 extends Component>(Entity entity, C1 c1, C2 c2) implements ViewEntry {
  }
}
