package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

public record EcsView1<C1 extends Component>(Entity entity, C1 c1) {
}
