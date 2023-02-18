package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * {@link ViewEntry} implementation for {@link View4}.
 *
 * @param entity the {@link Entity}
 * @param c1     the first {@link Component} instance
 * @param c2     the second component instance
 * @param c3     the third component instance
 * @param c4     the fourth component instance
 * @param <C1>   the first component type
 * @param <C2>   the second component type
 * @param <C3>   the third component type
 * @param <C4>   the fourth component type
 */
public record View4Entry<C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component>(
  Entity entity, C1 c1, C2 c2, C3 c3, C4 c4
) implements ViewEntry {
}
