package technology.sola.ecs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EntityTest {
  @Mock
  private World mockWorld;

  @Test
  void constructor_whenCreated_ShouldHaveIndexAndUniqueId() {
    Entity entity = new Entity(mockWorld, 1, "uuid");

    assertEquals(1, entity.getIndexInWorld());
    assertEquals("uuid", entity.getUniqueId());
  }

  @Test
  void setName_shouldUpdateName() {
    Entity entity = new Entity(mockWorld, 0, "");
    assertNull(entity.getName());

    entity.setName("test");

    assertEquals("test", entity.getName());
    Mockito.verify(mockWorld).updateEntityNameCache(entity, null);
  }

  @Test
  void setDisabled_shouldUpdateDisabled() {
    Entity entity = new Entity(mockWorld, 0, "");
    assertFalse(entity.isDisabled());

    entity.setDisabled(true);

    assertTrue(entity.isDisabled());

  }

  @Nested
  @DisplayName("addComponent")
  class AddComponentTests {
    @Test
    void whenCalled_shouldAddComponentClassToCurrentComponents() {
      Entity entity = new World(1).createEntity();

      entity.addComponent(new TestComponent1());
      entity.addComponent(new TestComponent1());

      assertEquals(1, entity.getCurrentComponents().size());
      assertTrue(entity.hasComponent(TestComponent1.class));
      assertEquals(TestComponent1.class, entity.getCurrentComponents().get(0));
    }

    @Test
    void whenCalled_shouldAddToWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      TestComponent1 testComponent = new TestComponent1();
      entity.addComponent(testComponent);

      Mockito.verify(mockWorld, Mockito.times(1)).addComponentForEntity(0, testComponent);
    }
  }

  @Nested
  @DisplayName("getComponent")
  class GetComponentTests {
    @Test
    void whenCalled_shouldGetFromWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.getComponent(TestComponent1.class);

      Mockito.verify(mockWorld, Mockito.times(1)).getComponentForEntity(0, TestComponent1.class);
    }
  }

  @Nested
  @DisplayName("removeComponent")
  class RemoveComponentTests {
    @Test
    void whenCalled_shouldRemoveComponentClassFromCurrentComponents() {
      Entity entity = new World(1).createEntity();

      entity.addComponent(new TestComponent1());
      assertEquals(1, entity.getCurrentComponents().size());

      entity.removeComponent(TestComponent1.class);
      assertEquals(0, entity.getCurrentComponents().size());
    }

    @Test
    void whenCalled_shouldRemoveFromWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.removeComponent(TestComponent1.class);

      Mockito.verify(mockWorld, Mockito.times(1)).removeComponent(0, TestComponent1.class, true);
    }

    @Test
    void whenCalled_shouldNotCreateImmutableList() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.addComponent(new TestComponent1());
      entity.removeComponent(TestComponent1.class);

      assertDoesNotThrow(() -> entity.addComponent(new TestComponent1()));
    }
  }

  @Nested
  @DisplayName("destroy")
  class DestroyTests {
    @Test
    void whenCalled_shouldDestroyFromWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.destroy();

      Mockito.verify(mockWorld, Mockito.times(1)).queueEntityForDestruction(entity);
    }
  }

  private record TestComponent1() implements Component {
  }
}
