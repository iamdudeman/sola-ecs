package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.View;

import java.util.List;

/**
 * View1 is a {@link View} implementation for {@link ViewEntry} for one {@link Component}.
 *
 * @param <C1> the component type
 */
public class View1<C1 extends Component> extends View<View1Entry<C1>> {
  private final Class<C1> c1Class;

  /**
   * Creates an instance of View1.
   *
   * @param c1Class the {@link Component} class this view watches
   */
  public View1(Class<C1> c1Class) {
    super(List.of(c1Class));
    this.c1Class = c1Class;
  }

  @Override
  protected View1Entry<C1> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    return new View1Entry<>(entity, c1);
  }

}
