package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

public record EcsView2<C1 extends Component, C2 extends Component>(Entity entity, C1 c1, C2 c2) {
}
