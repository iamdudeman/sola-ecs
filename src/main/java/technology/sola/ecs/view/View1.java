package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.View;

import java.util.List;

public class View1<C1 extends Component> extends View<View1.EcsViewEntry<C1>> {
  private final Class<C1> c1Class;

  public View1(Class<C1> c1Class) {
    super(List.of(c1Class));
    this.c1Class = c1Class;
  }

  @Override
  protected EcsViewEntry<C1> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    return new EcsViewEntry<>(entity, c1);
  }

  public record EcsViewEntry<C1 extends Component>(Entity entity, C1 c1) implements ViewEntry {
  }
}
