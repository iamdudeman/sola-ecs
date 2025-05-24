package technology.sola.ecs.cache;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.*;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
public class ViewCacheIntegrationTest {
  private World testWorld;
  private View1<TestComponent> testComponentView1;
  private View2<TestComponent, TestComponent2> testComponentView12;
  private View3<TestComponent, TestComponent2, TestComponent3> testComponentView123;
  private View4<TestComponent, TestComponent2, TestComponent3, TestComponent4> testComponentView1234;
  private ViewN testComponentViewN;

  private Entity entityWithNoComponents;
  private Entity entityWithTestComponent12;
  private Entity entityWithTestComponent1234;
  private Entity entityDisabled;

  @BeforeEach
  void setup() {
    testWorld = new World(10);

    entityWithTestComponent1234 = testWorld.createEntity(
      new TestComponent(), new TestComponent2(), new TestComponent3(), new TestComponent4()
    );
    testWorld.createEntity(
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
    entityDisabled = testWorld.createEntity(
      new TestComponent(), new TestComponent2(), new TestComponent3(), new TestComponent4()
    ).setDisabled(true);

    testComponentView1 = testWorld.createView().of(TestComponent.class);
    testComponentView12 = testWorld.createView().of(TestComponent.class, TestComponent2.class);
    testComponentView123 = testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class);
    testComponentView1234 = testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class);
    testComponentViewN = testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class, TestComponent.class);
  }

  @Nested
  class createView {
    @Test
    void whenCalledTwice_shouldReturnSameInstance() {
      assertEquals(testComponentView1, testWorld.createView().of(TestComponent.class));
      assertEquals(testComponentView12, testWorld.createView().of(TestComponent.class, TestComponent2.class));
      assertEquals(testComponentView123, testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class));
      assertEquals(testComponentView1234, testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class));
      assertEquals(testComponentViewN, testWorld.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class, TestComponent.class));
    }

    @Test
    void shouldInitializeViews() {
      assertEquals(5, testComponentView1.getEntries().size());
      assertEquals(3, testComponentView12.getEntries().size());
      assertEquals(2, testComponentView123.getEntries().size());
      assertEquals(1, testComponentView1234.getEntries().size());
      assertEquals(1, testComponentViewN.getEntries().size());
    }
  }

  @Nested
  class dropView {
    @Test
    void whenCalled_shouldClearView() {
      var view = testWorld.createView().of(TestComponent.class);

      testWorld.dropView(TestComponent.class);
      assertEquals(5, view.getEntries().size());

      testWorld.createEntity(new TestComponent());
      assertEquals(5, view.getEntries().size());
    }

    @Test
    void whenCalled_shouldBeAbleToCreateAgain() {
      var view = testWorld.createView().of(TestComponent.class);

      testWorld.dropView(TestComponent.class);
      assertEquals(5, view.getEntries().size());

      assertEquals(5, testWorld.createView().of(TestComponent.class).getEntries().size());
      testWorld.createEntity(new TestComponent());
      assertEquals(5, view.getEntries().size());
      assertEquals(6, testWorld.createView().of(TestComponent.class).getEntries().size());
    }
  }

  @Test
  void updateForAddComponent_integrationTest() {
    var originalTestComponentAdded = new TestComponent();
    entityWithNoComponents.addComponent(originalTestComponentAdded);
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());
    assertEquals(1, testComponentViewN.getEntries().size());

    entityWithNoComponents.addComponent(new TestComponent("test2"));
    testComponentView1.getEntries().stream()
      .filter(entry -> entry.entity() == entityWithNoComponents)
      .findFirst()
      .ifPresentOrElse(
        entry -> assertNotEquals(originalTestComponentAdded.message, entry.c1().message, "TestComponent instance should update"),
        () -> fail("entityWithNoComponents should still be in testComponentView1")
      );

    entityWithNoComponents.addComponent(new TestComponent2());
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(4, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());
    assertEquals(1, testComponentViewN.getEntries().size());

    entityWithTestComponent12.addComponent(new TestComponent3());
    entityWithTestComponent12.addComponent(new TestComponent4());
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(4, testComponentView12.getEntries().size());
    assertEquals(3, testComponentView123.getEntries().size());
    assertEquals(2, testComponentView1234.getEntries().size());
    assertEquals(2, testComponentViewN.getEntries().size());
  }

  @Test
  void updateForRemoveComponent_integrationTest() {
    entityWithNoComponents.removeComponent(TestComponent.class);
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());
    assertEquals(1, testComponentViewN.getEntries().size());

    entityWithTestComponent1234.removeComponent(TestComponent3.class);
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());
    assertEquals(0, testComponentViewN.getEntries().size());

    entityWithTestComponent1234.removeComponent(TestComponent.class);
    assertEquals(4, testComponentView1.getEntries().size());
    assertEquals(2, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());
    assertEquals(0, testComponentViewN.getEntries().size());
  }

  @Test
  void updateForDisabledStateChange_integrationTest() {
    entityDisabled.setDisabled(false);
    assertEquals(6, testComponentView1.getEntries().size());
    assertEquals(4, testComponentView12.getEntries().size());
    assertEquals(3, testComponentView123.getEntries().size());
    assertEquals(2, testComponentView1234.getEntries().size());
    assertEquals(2, testComponentViewN.getEntries().size());

    entityDisabled.setDisabled(true);
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());
    assertEquals(1, testComponentViewN.getEntries().size());
  }

  @Test
  void updateForDeletedEntity_integrationTest() {
    entityWithNoComponents.destroy();
    testWorld.cleanupDestroyedEntities();
    assertEquals(5, testComponentView1.getEntries().size());
    assertEquals(3, testComponentView12.getEntries().size());
    assertEquals(2, testComponentView123.getEntries().size());
    assertEquals(1, testComponentView1234.getEntries().size());
    assertEquals(1, testComponentViewN.getEntries().size());

    entityWithTestComponent1234.destroy();
    testWorld.cleanupDestroyedEntities();
    assertEquals(4, testComponentView1.getEntries().size());
    assertEquals(2, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());
    assertEquals(0, testComponentViewN.getEntries().size());

    entityWithTestComponent12.destroy();
    testWorld.cleanupDestroyedEntities();
    assertEquals(3, testComponentView1.getEntries().size());
    assertEquals(1, testComponentView12.getEntries().size());
    assertEquals(1, testComponentView123.getEntries().size());
    assertEquals(0, testComponentView1234.getEntries().size());
    assertEquals(0, testComponentViewN.getEntries().size());
  }

  private record TestComponent(String message) implements Component {
    public TestComponent() {
      this("test");
    }
  }

  private record TestComponent2() implements Component {
  }

  private record TestComponent3() implements Component {
  }

  private record TestComponent4() implements Component {
  }
}
