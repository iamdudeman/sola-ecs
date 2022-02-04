package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

public class EcsView1<C1 extends Component<?>> {
  private final Entity entity;
  private final C1 c1;

  public EcsView1(Entity entity, C1 c1) {
    this.entity = entity;
    this.c1 = c1;
  }

  public Entity getEntity() {
    return entity;
  }

  public C1 getC1() {
    return c1;
  }
}
