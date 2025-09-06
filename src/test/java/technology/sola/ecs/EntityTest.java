package technology.sola.ecs;

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

  @Nested
  class constructor {
    @Test
    void constructor_whenCreated_ShouldHaveIndexAndUniqueId() {
      Entity entity = new Entity(mockWorld, 1, "uuid");

      assertEquals(1, entity.getIndexInWorld());
      assertEquals("uuid", entity.getUniqueId());
    }

    @Test
    void whenCreated_ShouldBeDisabled() {
      Entity entity = new Entity(mockWorld, 1, "uuid");

      assertTrue(entity.isDisabled());
    }
  }

  @Nested
  class setName {
    @Test
    void shouldAddEntityMutation() {
      Entity entity = new Entity(mockWorld, 0, "");

      entity.setName("test");

      Mockito.verify(mockWorld, Mockito.times(1))
        .addEntityMutation(Mockito.any(EntityMutation.Name.class));
    }

    @Test
    void setImmediately_shouldUpdateName() {
      Entity entity = new Entity(mockWorld, 1, "uuid");
      assertNull(entity.getName());

      entity.setNameImmediately("newName");
      assertEquals("newName", entity.getName());
    }
  }

  @Nested
  class setDisabled {
    @Test
    void shouldAddEntityMutation() {
      Entity entity = new Entity(mockWorld, 0, "");

      entity.setDisabled(false);

      Mockito.verify(mockWorld, Mockito.times(1))
        .addEntityMutation(Mockito.any(EntityMutation.Disable.class));
    }

    @Test
    void setImmediately_shouldUpdateDisabled() {
      Entity entity = new Entity(mockWorld, 0, "");
      assertTrue(entity.isDisabled());

      entity.setDisabledImmediately(false);

      assertFalse(entity.isDisabled());
    }
  }

  @Nested
  class addComponent {
    @Test
    void shouldAddEntityMutation() {
      Entity entity = new Entity(mockWorld, 0, "");

      entity.addComponent(new TestComponent1());
      entity.addComponent(new TestComponent1());

      Mockito.verify(mockWorld, Mockito.times(2))
        .addEntityMutation(Mockito.any(EntityMutation.AddComponent.class));
    }
  }

  @Nested
  class removeComponent {
    @Test
    void shouldAddEntityMutation() {
      Entity entity = new Entity(mockWorld, 0, "");

      entity.removeComponent(TestComponent1.class);

      Mockito.verify(mockWorld, Mockito.times(1))
        .addEntityMutation(Mockito.any(EntityMutation.RemoveComponent.class));
    }
  }

  @Nested
  class getComponent {
    @Test
    void whenCalled_shouldGetFromWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.getComponent(TestComponent1.class);

      Mockito.verify(mockWorld, Mockito.times(1)).getComponent(0, TestComponent1.class);
    }
  }

  @Nested
  class hasComponent {
    @Test
    void whenCalled_shouldGetFromWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.hasComponent(TestComponent1.class);

      Mockito.verify(mockWorld, Mockito.times(1)).hasComponent(0, TestComponent1.class);
    }
  }

  @Nested
  class getCurrentComponents {
    @Test
    void whenCalled_shouldGetFromWorld() {
      Entity entity = new Entity(mockWorld, 0, "uuid");

      entity.getCurrentComponents();

      Mockito.verify(mockWorld, Mockito.times(1)).getCurrentComponents(0);
    }
  }

  @Nested
  class destroy {
    @Test
    void shouldAddEntityMutation() {
      Entity entity = new Entity(mockWorld, 0, "");

      entity.destroy();

      Mockito.verify(mockWorld, Mockito.times(1))
        .addEntityMutation(Mockito.any(EntityMutation.Destroy.class));
    }
  }

  private record TestComponent1() implements Component {
  }
}
