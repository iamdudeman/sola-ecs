package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * Contains two {@link Component}s that were searched for.
 *
 * @param entity the {@link Entity}
 * @param c1     the first component
 * @param c2     the second component
 */
public record EcsView2<C1 extends Component, C2 extends Component>(Entity entity, C1 c1, C2 c2) {
}
