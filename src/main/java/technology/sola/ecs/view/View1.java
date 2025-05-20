package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.ViewImpl;

import java.util.List;

/**
 * View1 is a {@link ViewImpl} implementation for {@link ViewEntry} for one {@link Component}.
 *
 * @param <C1> the component type
 */
@NullMarked
public class View1<C1 extends Component> extends ViewImpl<View1Entry<C1>> {
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
  @Nullable
  protected View1Entry<C1> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    return new View1Entry<>(entity, c1);
  }
}
