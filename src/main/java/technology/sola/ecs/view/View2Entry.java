package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * {@link ViewEntry} implementation for {@link View2}.
 *
 * @param entity the {@link Entity}
 * @param c1     the first {@link Component} instance
 * @param c2     the second component instance
 * @param <C1>   the first component type
 * @param <C2>   the second component type
 */
public record View2Entry<C1 extends Component, C2 extends Component>(
  Entity entity, C1 c1, C2 c2
) implements ViewEntry {
}
