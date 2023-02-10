package technology.sola.ecs.cache;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.List;


public class EcsView1<C1 extends Component> extends EcsView<EcsView1.EcsViewEntry<C1>> {
  private final Class<C1> c1Class;

  public EcsView1(Class<C1> c1Class) {
    super(List.of(c1Class));
    this.c1Class = c1Class;
  }

  @Override
  public EcsViewEntry<C1> createEntryFromEntity(Entity entity) {
    return new EcsViewEntry<>(entity, entity.getComponent(c1Class));
  }


  public record EcsViewEntry<C1 extends Component>(Entity entity, C1 c1) implements ViewEntry {
  }
}
