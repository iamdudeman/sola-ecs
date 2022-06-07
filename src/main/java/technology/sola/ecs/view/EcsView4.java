package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

public record EcsView4<C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component>(
  Entity entity, C1 c1, C2 c2, C3 c3, C4 c4
) {
}
