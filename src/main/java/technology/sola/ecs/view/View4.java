package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.View;

import java.util.List;

/**
 * View4 is a {@link View} implementation for {@link ViewEntry} for four {@link Component}.
 *
 * @param <C1> the first component type
 * @param <C2> the second component type
 * @param <C3> the third component type
 * @param <C4> the fourth component type
 */
public class View4<C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component> extends View<View4.View4Entry<C1, C2, C3, C4>> {
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
}
