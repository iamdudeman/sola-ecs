package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * {@link ViewEntry} implementation for {@link View1}.
 *
 * @param entity the {@link Entity}
 * @param c1     the {@link Component} instance
 * @param <C1>   the component type
 */
public record View1Entry<C1 extends Component>(
  Entity entity, C1 c1
) implements ViewEntry {
}
