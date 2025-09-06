package technology.sola.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import technology.sola.ecs.cache.EntityNameCache;
import technology.sola.ecs.cache.ViewCache;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EntityMutationTest {
  @Mock
  private ViewCache mockViewCache;
  @Mock
  private EntityNameCache mockEntityNameCache;

  @Nested
  class Created {
    @Test
    void shouldSetDisabledToFalse() {
      World world = new World(2);
      Entity entity = world.createEntity();

      new EntityMutation.Created(0, null, new Component[0])
        .apply(world, mockViewCache, mockEntityNameCache);

      assertFalse(entity.isDisabled());
    }

    @Test
    void shouldSetNameIfProvided() {
      World world = new World(2);
      Entity entity = world.createEntity();

      new EntityMutation.Created(0, "test", new Component[0])
        .apply(world, mockViewCache, mockEntityNameCache);

      assertEquals("test", entity.getName());
      Mockito.verify(mockEntityNameCache).update(entity, null);
    }

    @Test
    void shouldAddComponentsIfProvided() {
      World world = new World(2);
      Entity entity = world.createEntity();

      new EntityMutation.Created(0, null, new Component[] { new TestComponent() })
        .apply(world, mockViewCache, mockEntityNameCache);

      assertTrue(entity.hasComponent(TestComponent.class));
    }

    @Test
    void whenEntityDoesNotExist_shouldDoNothing() {
      World world = new World(2);

      var mutation = new EntityMutation.Created(0, null, new Component[0]);

      assertDoesNotThrow(() -> mutation.apply(world, mockViewCache, mockEntityNameCache));
    }
  }

  @Nested
  class Name {
    @Test
    void shouldUpdateNameAndCache() {
      World world = new World(2);
      Entity entity = world.createEntity();

      new EntityMutation.Name(0, "test")
        .apply(world, mockViewCache, mockEntityNameCache);

      assertEquals("test", entity.getName());
      Mockito.verify(mockEntityNameCache).update(entity, null);
    }

    @Test
    void whenPreviousName_shouldUpdateNameAndCacheProperly() {
      World world = new World(2);
      Entity entity = world.createEntity();

      entity.setNameImmediately("previous");

      new EntityMutation.Name(0, "test")
        .apply(world, mockViewCache, mockEntityNameCache);

      assertEquals("test", entity.getName());
      Mockito.verify(mockEntityNameCache).update(entity, "previous");
    }


    @Test
    void whenNull_shouldUpdateName() {
      World world = new World(2);
      Entity entity = world.createEntity();
      entity.setNameImmediately("test");

      new EntityMutation.Name(0, null)
        .apply(world, mockViewCache, mockEntityNameCache);

      assertNull(entity.getName());
    }

    @Test
    void whenEntityDoesNotExist_shouldDoNothing() {
      World world = new World(2);

      var mutation = new EntityMutation.Name(0, "test");

      assertDoesNotThrow(() -> mutation.apply(world, mockViewCache, mockEntityNameCache));
    }
  }

  @Nested
  class Disable {
    @Test
    void test() {
      // todo
    }

    @Test
    void whenEntityDoesNotExist_shouldDoNothing() {
      World world = new World(2);

      var mutation = new EntityMutation.Disable(0, true);

      assertDoesNotThrow(() -> mutation.apply(world, mockViewCache, mockEntityNameCache));
    }
  }

  @Nested
  class Destroy {
    @Test
    void test() {
      // todo
    }

    @Test
    void whenEntityDoesNotExist_shouldDoNothing() {
      World world = new World(2);

      var mutation = new EntityMutation.Destroy(0);

      assertDoesNotThrow(() -> mutation.apply(world, mockViewCache, mockEntityNameCache));
    }
  }

  @Nested
  class AddComponent {
    @Test
    void test() {
      // todo
    }
  }

  @Nested
  class RemoveComponent {
    @Test
    void test() {
      // todo
    }
  }

  private record TestComponent() implements Component {}
}
