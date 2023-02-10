package technology.sola.ecs.cache;

import technology.sola.ecs.Component;

public class ViewCache {
  public static void main(String[] args) {
    var view = new ViewCache().createView(TestComponent.class);
    var view2 = new ViewCache().createView(TestComponent.class, TestComponent2.class);

    TestComponent testComponent = view.getEntries().get(0).c1();
    TestComponent2 testComponent2 = view2.getEntries().get(0).c2();
  }

  private record TestComponent() implements Component {
  }

  private record TestComponent2() implements Component {
  }


  public <C1 extends Component> EcsView1<C1> createView(Class<C1> componentClass) {
    return new EcsView1<>(componentClass);
  }

  public <C1 extends Component, C2 extends Component> EcsView2<C1, C2> createView(Class<C1> c1Class, Class<C2> c2Class) {
    return new EcsView2<>(c1Class, c2Class);
  }
}
