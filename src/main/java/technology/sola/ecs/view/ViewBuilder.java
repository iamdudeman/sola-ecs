package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.cache.View;
import technology.sola.ecs.cache.ViewCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewBuilder {
  private final ViewCache viewCache;
  private final World world;
  private Map<Integer, View<?>> builtViews = new HashMap<>();

  public ViewBuilder(ViewCache viewCache, World world) {
    this.viewCache = viewCache;
    this.world = world;
  }

  public <C1 extends Component> View1<C1> createView(Class<C1> c1Class) {
    int hash = Objects.hash(c1Class);
    var view = builtViews.get(hash);

    // todo think about this more! would be nice to reuse already built views
    if (view == null) {
      view = new View1<>(viewCache, world, c1Class);

      viewCache.addViewToCache(view);
      builtViews.put(hash, view);
    }


    return (View1<C1>) view;
  }

  public <C1 extends Component, C2 extends Component> View2<C1, C2> createView(Class<C1> c1Class, Class<C2> c2Class) {
    var view = new View2<>(viewCache, world, c1Class, c2Class);

    viewCache.addViewToCache(view);

    return view;
  }
}
