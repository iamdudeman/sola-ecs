package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * Contains four {@link Component}s that were searched for.
 *
 * @param entity the {@link Entity}
 * @param c1     the first component
 * @param c2     the second component
 * @param c3     the third component
 * @param c4     the fourth component
 */
public record EcsView4<C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component>(
  Entity entity, C1 c1, C2 c2, C3 c3, C4 c4
) {
}
