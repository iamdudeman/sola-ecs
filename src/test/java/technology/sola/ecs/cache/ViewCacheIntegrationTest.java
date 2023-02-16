package technology.sola.ecs.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View1;
import technology.sola.ecs.view.View2;
import technology.sola.ecs.view.View3;
import technology.sola.ecs.view.View4;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewCacheIntegrationTest {
  private World testWorld;
  private View1<TestComponent> testComponentView1;
  private View2<TestComponent, TestComponent2> testComponentView12;
  private View3<TestComponent, TestComponent2, TestComponent3> testComponentView123;
  private View4<TestComponent, TestComponent2, TestComponent3, TestComponent4> testComponentView1234;

  private Entity entityWithNoComponents;
  private Entity entityWithTestComponent12;
  private Entity entityWithTestComponent123;
  private Entity entityWithTestComponent1234;

  @BeforeEach
  void setup() {
    testWorld = new World(10);

    entityWithTestComponent1234 = testWorld.createEntity(
      new TestComponent(), new TestComponent2(), new TestComponent3(), new TestComponent4()
    );
    entityWithTestComponent123 = testWorld.createEntity(
      new TestComponent(), new TestComponent2(), new TestComponent3()
    );
    entityWithTestComponent12 = testWorld.createEntity(
      new TestComponent(), new TestComponent2()
    );
    testWorld.createEntity(
      new TestComponent(), new TestComponent3()
    );
    testWorld.createEntity(
      new TestComponent2(), new TestComponent3()
    );
    testWorld.createEntity(
      new TestComponent()
    );
    entityWithNoComponents = testWorld.createEntity();

    testComponentView1 = testWorld.createView().of(TestComponent.class);
    testComponentView12 = testWorld.createView().of(TestComponent.class, TestComponent2.class);
    testComponentView123 = testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class);
    testComponentView1234 = testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class);
  }

  @Nested
  class createView {
    @Test
    void whenCalledTwice_shouldReturnSameInstance() {
      assertEquals(testComponentView1, testWorld.createView().of(TestComponent.class));
      assertEquals(testComponentView12, testWorld.createView().of(TestComponent.class, TestComponent2.class));
      assertEquals(testComponentView123, testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class));
      assertEquals(testComponentView1234, testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class));
    }

    @Test
    void shouldInitializeViews() {
      assertEquals(5, testComponentView1.getEntries().size());
      assertEquals(3, testComponentView12.getEntries().size());
      assertEquals(2, testComponentView123.getEntries().size());
      assertEquals(1, testComponentView1234.getEntries().size());
    }
  }

  @Test
  void updateForAddComponent_integrationTest() {
    entityWithNoComponents.addComponent(new TestComponent());
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());

    entityWithNoComponents.addComponent(new TestComponent2());
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(4, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());

    entityWithTestComponent12.addComponent(new TestComponent3());
    entityWithTestComponent12.addComponent(new TestComponent4());
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(4, testComponentView12.getEntries().size());
    assertEquals(3, testComponentView123.getEntries().size());
    assertEquals(2, testComponentView1234.getEntries().size());
  }

  @Test
  void updateForRemoveComponent_integrationTest() {
    entityWithNoComponents.removeComponent(TestComponent.class);
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());

    entityWithTestComponent1234.removeComponent(TestComponent3.class);
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());

    entityWithTestComponent1234.removeComponent(TestComponent.class);
    assertEquals(4, testComponentView1.getEntries().size());
    assertEquals(2, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());
  }

  @Test
  void updateForDeletedEntity_integrationTest() {
    entityWithNoComponents.destroy();
    testWorld.cleanupDestroyedEntities();
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());

    entityWithTestComponent1234.destroy();
    testWorld.cleanupDestroyedEntities();
    assertEquals(4, testComponentView1.getEntries().size());
    assertEquals(2, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());

    entityWithTestComponent12.destroy();
    testWorld.cleanupDestroyedEntities();
    assertEquals(3, testComponentView1.getEntries().size());
    assertEquals(1, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());
  }

  private record TestComponent() implements Component {
  }

  private record TestComponent2() implements Component {
  }

  private record TestComponent3() implements Component {
  }

  private record TestComponent4() implements Component {
  }
}
