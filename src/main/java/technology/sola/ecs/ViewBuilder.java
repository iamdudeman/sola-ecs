package technology.sola.ecs;

import technology.sola.ecs.cache.ViewCache;
import technology.sola.ecs.view.View1;
import technology.sola.ecs.view.View2;
import technology.sola.ecs.view.View3;
import technology.sola.ecs.view.View4;

/**
 * The ViewBuilder class exposes methods for creating {@link technology.sola.ecs.cache.View}s.
 */
public class ViewBuilder {
  private final ViewCache viewCache;

  ViewBuilder(ViewCache viewCache) {
    this.viewCache = viewCache;
  }

  /**
   * Returns a {@link View1} of entries having one {@link Component}.
   *
   * @param <C1>    the type of the component
   * @param c1Class the class for the component
   * @return the view
   */
  public <C1 extends Component> View1<C1> of(
    Class<C1> c1Class
  ) {
    return viewCache.createView(c1Class);
  }

  /**
   * Returns a {@link View2} of entries having two {@link Component}s.
   *
   * @param <C1>    the type for the first component
   * @param <C2>    the type for the second component
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @return the view
   */
  public <C1 extends Component, C2 extends Component> View2<C1, C2> of(
    Class<C1> c1Class, Class<C2> c2Class
  ) {
    return viewCache.createView(c1Class, c2Class);
  }

  /**
   * Returns a {@link View3} of entries having three {@link Component}s.
   *
   * @param <C1>    the type for the first component
   * @param <C2>    the type for the second component
   * @param <C3>    the type for the third component
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @param c3Class the class for the third component
   * @return the view
   */
  public <C1 extends Component, C2 extends Component, C3 extends Component> View3<C1, C2, C3> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class
  ) {
    return viewCache.createView(c1Class, c2Class, c3Class);
  }

  /**
   * Returns a {@link View3} of entries having three {@link Component}s.
   *
   * @param <C1>    the type for the first component
   * @param <C2>    the type for the second component
   * @param <C3>    the type for the third component
   * @param <C4>    the type for the fourth component
   * @param c1Class the class for the first component
   * @param c2Class the class for the second component
   * @param c3Class the class for the third component
   * @param c4Class the class for the fourth component
   * @return the view
   */
  public <C1 extends Component, C2 extends Component, C3 extends Component, C4 extends Component> View4<C1, C2, C3, C4> of(
    Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class, Class<C4> c4Class
  ) {
    return viewCache.createView(c1Class, c2Class, c3Class, c4Class);
  }
}
