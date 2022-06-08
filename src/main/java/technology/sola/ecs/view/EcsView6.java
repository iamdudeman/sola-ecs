package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * Contains six {@link Component}s that were searched for.
 *
 * @param entity the {@link Entity}
 * @param c1     the first component
 * @param c2     the second component
 * @param c3     the third component
 * @param c4     the fourth component
 * @param c5     the fifth component
 * @param c6     the sixth component
 */
public record EcsView6<C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component, C5 extends Component, C6 extends Component>(
  Entity entity, C1 c1, C2 c2, C3 c3, C4 c4, C5 c5, C6 c6
) {
}
