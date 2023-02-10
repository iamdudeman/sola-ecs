package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.List;

public class EcsView2<C1 extends Component, C2 extends Component> extends EcsView<EcsView2.EcsViewEntry<C1, C2>> {
  private final Class<C1> c1Class;
  private final Class<C2> c2Class;

  public EcsView2(Class<C1> c1Class, Class<C2> c2Class) {
    super(List.of(c1Class));
    this.c1Class = c1Class;
    this.c2Class = c2Class;
  }

  @Override
  public EcsViewEntry<C1, C2> createEntryFromEntity(Entity entity) {
    return new EcsViewEntry<>(entity, entity.getComponent(c1Class), entity.getComponent(c2Class));
  }


  public record EcsViewEntry<C1 extends Component, C2 extends Component>(Entity entity, C1 c1, C2 c2) implements ViewEntry {
  }
}
