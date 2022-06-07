package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

public record EcsView3<C1 extends Component, C2 extends Component, C3 extends Component>(
  Entity entity, C1 c1, C2 c2, C3 c3
) {
}
