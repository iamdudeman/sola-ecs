package technology.sola.ecs.view;

import org.mockito.Mockito;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

abstract class ViewNTestBase {
  protected final TestComponent testComponent = new TestComponent();
  protected final TestComponent2 testComponent2 = new TestComponent2();
  protected final TestComponent3 testComponent3 = new TestComponent3();

  protected Entity createMockEntity(ComponentMapping<?>... componentMappings) {
    Entity mockEntity = Mockito.mock(Entity.class);

    if (componentMappings != null) {
      for (var mapping : componentMappings) {
        Mockito.doReturn(mapping.instance).when(mockEntity).getComponent(mapping.clazz);
      }
    }

    return mockEntity;
  }

  protected record ComponentMapping<T extends Component>(Class<T> clazz, T instance) {
  }

  protected record TestComponent() implements Component {
  }

  protected record TestComponent2() implements Component {
  }

  protected record TestComponent3() implements Component {
  }
}
