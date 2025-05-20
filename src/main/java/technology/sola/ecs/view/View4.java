package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.ViewImpl;

import java.util.List;

/**
 * View4 is a {@link ViewImpl} implementation for {@link ViewEntry} for four {@link Component}.
 *
 * @param <C1> the first component type
 * @param <C2> the second component type
 * @param <C3> the third component type
 * @param <C4> the fourth component type
 */
@NullMarked
public class View4<C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component> extends ViewImpl<View4Entry<C1, C2, C3, C4>> {
  private final Class<C1> c1Class;
  private final Class<C2> c2Class;
  private final Class<C3> c3Class;
  private final Class<C4> c4Class;

  /**
   * Creates an instance of View3.
   *
   * @param c1Class the first {@link Component} class this view watches
   * @param c2Class the second component class this view watches
   * @param c3Class the third class component class this view watches
   * @param c4Class the fourth class component class this view watches
   */
  public View4(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class) {
    super(List.of(c1Class, c2Class, c3Class, c4Class));
    this.c1Class = c1Class;
    this.c2Class = c2Class;
    this.c3Class = c3Class;
    this.c4Class = c4Class;
  }

  @Override
  @Nullable
  protected View4Entry<C1, C2, C3, C4> createEntryFromEntity(Entity entity) {
    C1 c1 = entity.getComponent(c1Class);
    if (c1 == null) return null;

    C2 c2 = entity.getComponent(c2Class);
    if (c2 == null) return null;

    C3 c3 = entity.getComponent(c3Class);
    if (c3 == null) return null;

    C4 c4 = entity.getComponent(c4Class);
    if (c4 == null) return null;

    return new View4Entry<>(entity, c1, c2, c3, c4);
  }
}
