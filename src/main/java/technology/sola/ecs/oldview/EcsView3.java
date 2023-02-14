package technology.sola.ecs.oldview;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * Contains three {@link Component}s that were searched for.
 *
 * @param entity the {@link Entity}
 * @param c1     the first component
 * @param c2     the second component
 * @param c3     the third component
 */
public record EcsView3<C1 extends Component, C2 extends Component, C3 extends Component>(
  Entity entity, C1 c1, C2 c2, C3 c3
) {
}