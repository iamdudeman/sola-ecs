package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.cache.ViewCache;

public class ViewBuilder {
  private final ViewCache viewCache;
  private final World world;

  public ViewBuilder(ViewCache viewCache, World world) {
    this.viewCache = viewCache;
    this.world = world;
  }

  public <C1 extends Component> View1<C1> createView(Class<C1> componentClass) {
    var view = new View1<>(viewCache, world, componentClass);

    viewCache.addViewToCache(view);

    return view;
  }

  public <C1 extends Component, C2 extends Component> View2<C1, C2> createView(Class<C1> c1Class, Class<C2> c2Class) {
    var view = new View2<>(viewCache, world, c1Class, c2Class);

    viewCache.addViewToCache(view);

    return view;
  }
}
