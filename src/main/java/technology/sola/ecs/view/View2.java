package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.cache.View;
import technology.sola.ecs.cache.ViewCache;

import java.util.List;

public class View2<C1 extends Component, C2 extends Component> extends View<View2.EcsViewEntry<C1, C2>> {
  private final Class<C1> c1Class;
  private final Class<C2> c2Class;

  View2(ViewCache viewCache, World world, Class<C1> c1Class, Class<C2> c2Class) {
    super(viewCache, List.of(c1Class));
    this.c1Class = c1Class;
    this.c2Class = c2Class;
    initializeView(world);
  }

  @Override
  protected EcsViewEntry<C1, C2> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    C2 c2 = entity.getComponent(c2Class);
    if (c2 == null) return null;

    return new EcsViewEntry<>(entity, c1, c2);
  }

  public record EcsViewEntry<C1 extends Component, C2 extends Component>(Entity entity, C1 c1, C2 c2) implements ViewEntry {
  }
}
