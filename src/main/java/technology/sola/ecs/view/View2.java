package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.ViewImpl;

import java.util.List;

/**
 * View2 is a {@link ViewImpl} implementation for {@link ViewEntry} for two {@link Component}.
 *
 * @param <C1> the first component type
 * @param <C2> the second component type
 */
@NullMarked
public class View2<C1 extends Component, C2 extends Component> extends ViewImpl<View2Entry<C1, C2>> {
  private final Class<C1> c1Class;
  private final Class<C2> c2Class;

  /**
   * Creates an instance of View2.
   *
   * @param c1Class the first {@link Component} class this view watches
   * @param c2Class the second component class this view watches
   */
  public View2(Class<C1> c1Class, Class<C2> c2Class) {
    super(List.of(c1Class, c2Class));
    this.c1Class = c1Class;
    this.c2Class = c2Class;
  }

  @Override
  @Nullable
  protected View2Entry<C1, C2> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    C2 c2 = entity.getComponent(c2Class);
    if (c2 == null) return null;

    return new View2Entry<>(entity, c1, c2);
  }
}
