package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

/**
 * Contains one {@link Component} that was searched for.
 *
 * @param entity the {@link Entity}
 * @param c1     the first component
 */
public record EcsView1<C1 extends Component>(Entity entity, C1 c1) {
}
