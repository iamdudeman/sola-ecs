package technology.sola.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.exception.WorldEntityLimitException;
import technology.sola.ecs.exception.MissingEntityException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
  @Test
  void whenCreated_withZeroMaxEntities_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new World(0));
  }

  @Test
  void whenCreatingTooManyEntities_shouldThrowException() {
    World world = new World(2);
    int maxEntityCount = world.getMaxEntityCount();

    assertThrows(WorldEntityLimitException.class, () -> {
      for (int i = 0; i < maxEntityCount + 1; i++) {
        world.createEntity();
      }
    });
    assertEquals(2, world.getEntitiesWithComponents().size());
  }

  @Test
  void whenCreatingEntities_shouldHaveTotalCount() {
    World world = new World(5);

    for (int i = 0; i < 4; i++) {
      world.createEntity();
    }

    world.queueEntityForDestruction(world.getEntityById(0));
    world.cleanupDestroyedEntities();

    assertEquals(3, world.getTotalEntityCount());
  }

  @Nested
  class GetEntityByIdTests {
    @Test
    void whenEntityWithIdNotCreated_shouldThrowException() {
      World world = new World(2);

      assertThrows(MissingEntityException.class, () -> world.getEntityById(0));
    }

    @Test
    void whenEntityWithIdCreated_shouldReturnEntity() {
      World world = new World(2);
      Entity entity = world.createEntity();

      Entity result = world.getEntityById(entity.getIndexInWorld());

      assertEquals(entity, result);
    }
  }

  @Nested
  class GetEntityByNameTests {
    @Test
    void whenNoEntityWithName_shouldReturnNull() {
      World world = new World(2);
      world.createEntity();
      world.createEntity();

      Entity result = world.getEntityByName("test");

      assertNull(result);
    }

    @Test
    void whenEntityFoundWithName_shouldReturnEntity() {
      World world = new World(2);
      world.createEntity();
      Entity expected = world.createEntity().setName("test");

      Entity result = world.getEntityByName("test");

      assertEquals(expected, result);
    }
  }

  @Test
  void whenAddingComponentsForEntity_shouldBeAbleToGetThem() {
    World world = new World(1);
    TestUtil.TestComponent1 testComponent = new TestUtil.TestComponent1();
    world.createEntity();

    world.addComponentForEntity(0, testComponent);

    assertEquals(testComponent, world.getComponentForEntity(0, TestUtil.TestComponent1.class));
  }

  @Test
  void whenRemovingComponentsFromEntity_shouldNotBeAbleToGetThem() {
    World world = new World(1);
    TestUtil.TestComponent1 testComponent = new TestUtil.TestComponent1();
    world.createEntity();

    world.addComponentForEntity(0, testComponent);
    world.removeComponent(0, TestUtil.TestComponent1.class);

    assertNull(world.getComponentForEntity(0, TestUtil.TestComponent1.class));
  }

  @Test
  void whenDestroyingEntity_shouldNotBeAbleToGetComponents() {
    World world = new World(1);
    TestUtil.TestComponent1 testComponent = new TestUtil.TestComponent1();
    Entity entity = world.createEntity();

    entity.getCurrentComponents().add(TestUtil.TestComponent1.class);
    world.addComponentForEntity(0, testComponent);
    world.queueEntityForDestruction(entity);
    world.cleanupDestroyedEntities();

    assertNull(world.getComponentForEntity(0, TestUtil.TestComponent1.class));
  }

  @Nested
  class GetEntitiesTests {
    @Test
    void shouldReturnAllEntities() {
      World world = new World(2);
      Entity entity = world.createEntity();
      Entity entity2 = world.createEntity();

      assertEquals(2, world.getEntities().size());
      assertEquals(entity, world.getEntities().get(0));
      assertEquals(entity2, world.getEntities().get(1));
    }
  }

  @Nested
  class GetEnabledEntitiesTests {
    @Test
    void shouldReturnAllEnabledEntities() {
      World world = new World(2);
      Entity entity = world.createEntity();
      Entity entity2 = world.createEntity();
      entity2.setDisabled(true);

      assertEquals(1, world.getEnabledEntities().size());
      assertEquals(entity, world.getEnabledEntities().get(0));
    }
  }

  @Nested
  class GetEntitiesWithComponentsTests {
    @Test
    void whenEntityHasAllComponents_shouldReturnEntity() {
      World world = new World(2);
      TestUtil.TestComponent1 testComponent = new TestUtil.TestComponent1();
      TestUtil.TestComponent2 testComponent2 = new TestUtil.TestComponent2();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getIndexInWorld(), testComponent);
      world.addComponentForEntity(entity.getIndexInWorld(), testComponent2);

      List<Entity> entities = world.getEntitiesWithComponents(TestUtil.TestComponent1.class, TestUtil.TestComponent2.class);
      assertEquals(1, entities.size());
      assertEquals(entity, entities.get(entity.getIndexInWorld()));
    }

    @Test
    void whenEntityIsMissingComponent_shouldNotReturnEntity() {
      World world = new World(2);
      TestUtil.TestComponent1 testComponent = new TestUtil.TestComponent1();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getIndexInWorld(), testComponent);

      List<Entity> entities = world.getEntitiesWithComponents(TestUtil.TestComponent1.class, TestUtil.TestComponent2.class);
      assertEquals(0, entities.size());
    }
  }

  @Nested
  class ViewTests {
    @Test
    void whenViewCreated_shouldContainEntities() {
      World world = new World(2);
      TestUtil.TestComponent1 testComponent = new TestUtil.TestComponent1();
      TestUtil.TestComponent2 testComponent2 = new TestUtil.TestComponent2();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getIndexInWorld(), testComponent);
      world.addComponentForEntity(entity.getIndexInWorld(), testComponent2);

      var view = world.getView().of(TestUtil.TestComponent1.class, TestUtil.TestComponent2.class);
      assertEquals(1, view.size());
      assertEquals(entity, view.get(entity.getIndexInWorld()).entity());
    }
  }
}
