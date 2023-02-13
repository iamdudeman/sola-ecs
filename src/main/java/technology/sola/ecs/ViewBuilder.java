package technology.sola.ecs;

import technology.sola.ecs.cache.ViewCache;
import technology.sola.ecs.view.View1;
import technology.sola.ecs.view.View2;
import technology.sola.ecs.view.View3;

public class ViewBuilder {
  private final ViewCache viewCache;

  ViewBuilder(ViewCache viewCache) {
    this.viewCache = viewCache;
  }

  public <C1 extends Component> View1<C1> of(Class<C1> c1Class) {
    return viewCache.createView(c1Class);
  }

  public <C1 extends Component, C2 extends Component> View2<C1, C2> of(Class<C1> c1Class, Class<C2> c2Class) {
    return viewCache.createView(c1Class, c2Class);
  }

  public <C1 extends Component, C2 extends Component, C3 extends Component> View3<C1, C2, C3> of(Class<C1> c1Class, Class<C2> c2Class, Class<C3> c3Class) {
    return viewCache.createView(c1Class, c2Class, c3Class);
  }
}
