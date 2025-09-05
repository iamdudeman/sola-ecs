package technology.sola.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.exception.WorldEntityLimitException;

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
    assertEquals(2, world.findEntitiesWithComponents().size());
  }

  @Test
  void whenCreatingEntities_shouldHaveTotalCount() {
    World world = new World(5);

    for (int i = 0; i < 4; i++) {
      world.createEntity();
    }

    world.getEntityAtIndex(0).destroy();
//    world.queueEntityForDestruction(world.getEntityAtIndex(0));
    world.update();

    assertEquals(3, world.getEntityCount());
  }

  @Test
  void whenCreatingEntity_withComponents_shouldHaveComponents() {
    World world = new World(1);

    Entity entity = world.createEntity(new TestComponent1());

    assertTrue(entity.hasComponent(TestComponent1.class));
  }

  @Nested
  class GetEntityAtIndexTests {
    @Test
    void whenEntityNotAtIndex_shouldReturnNull() {
      World world = new World(2);

      assertNull(world.getEntityAtIndex(0));
    }

    @Test
    void whenEntityWithIdCreated_shouldReturnEntity() {
      World world = new World(2);
      Entity entity = world.createEntity();

      Entity result = world.getEntityAtIndex(entity.getIndexInWorld());

      assertEquals(entity, result);
    }
  }

  @Nested
  class FindEntityByNameTests {
    @Test
    void whenNoEntityWithName_shouldReturnEmpty() {
      World world = new World(2);
      world.createEntity();
      world.createEntity();

      var result = world.findEntityByName("test");

      assertNull(result);
    }

    @Test
    void whenEntityFoundWithName_shouldReturnEntity() {
      World world = new World(2);
      world.createEntity();
      Entity expected = world.createEntity().setName("test");
      world.update();

      var result = world.findEntityByName("test");

      assertEquals(expected, result);
    }
  }

  @Nested
  class FindEntityByUniqueIdTests {
    @Test
    void whenNoEntityWithUniqueId_shouldReturnEmpty() {
      World world = new World(2);
      world.createEntity();
      world.createEntity();

      var result = world.findEntityByUniqueId("test");

      assertNull(result);
    }

    @Test
    void whenEntityFoundWithUniqueID_shouldReturnEntity() {
      World world = new World(2);
      world.createEntity();
      Entity expected = world.createEntity("unique", null, new Component[]{});

      var result = world.findEntityByUniqueId("unique");

      assertEquals(expected, result);
    }

    @Test
    void whenEntityFoundWithGeneratedUniqueID_shouldReturnEntity() {
      World world = new World(2);
      world.createEntity();
      Entity expected = world.createEntity();

      var result = world.findEntityByUniqueId(expected.getUniqueId());

      assertEquals(expected, result);
    }
  }

  @Test
  void whenAddingComponentsForEntity_shouldBeAbleToGetThem() {
    World world = new World(1);
    TestComponent1 testComponent = new TestComponent1();
    world.createEntity();

    world.addComponentForEntity(0, testComponent);

    assertEquals(testComponent, world.getComponentForEntity(0, TestComponent1.class));
  }

  @Test
  void whenRemovingComponentsFromEntity_shouldNotBeAbleToGetThem() {
    World world = new World(1);
    TestComponent1 testComponent = new TestComponent1();
    world.createEntity();

    world.addComponentForEntity(0, testComponent);
    world.removeComponent(0, TestComponent1.class, true);

    assertNull(world.getComponentForEntity(0, TestComponent1.class));
  }

  @Nested
  class update {
    // todo update test names to "update" or something like that
    @Test
    void whenDestroyingEntity_shouldNotBeAbleToGetComponents() {
      World world = new World(1);
      TestComponent1 testComponent = new TestComponent1();
      Entity entity = world.createEntity();

      entity.getCurrentComponents().add(TestComponent1.class);
      world.addComponentForEntity(0, testComponent);
      entity.destroy();
      world.update();

      assertNull(world.getComponentForEntity(0, TestComponent1.class));
    }

    @Test
    void whenDestroyingEntity_shouldNotBeAbleToGetByName() {
      World world = new World(1);

      Entity entity = world.createEntity().setName("test");

      assertEquals(entity, world.findEntityByName("test"));

      world.destroyEntity(entity);
      world.update();

      assertNull(world.findEntityByName("test"));
    }

    @Test
    void whenDestroyingEntity_shouldNotHaveComponentsWhenIdReused() {
      World world = new World(1);

      Entity entity = world.createEntity();

      world.addComponentForEntity(0, new TestComponent1());
      assertNotNull(world.getComponentForEntity(0, TestComponent1.class));
      entity.destroy();
      world.update();

      world.createEntity();

      assertNull(world.getComponentForEntity(0, TestComponent1.class));
    }
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
      entity2.setDisabledImmediately(true);

      assertEquals(1, world.getEnabledEntities().size());
      assertEquals(entity, world.getEnabledEntities().get(0));
    }
  }

  @Nested
  class GetEntitiesWithComponentsTests {
    @Test
    void whenEntityHasAllComponents_shouldReturnEntity() {
      World world = new World(2);
      TestComponent1 testComponent = new TestComponent1();
      TestComponent2 testComponent2 = new TestComponent2();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getIndexInWorld(), testComponent);
      world.addComponentForEntity(entity.getIndexInWorld(), testComponent2);

      List<Entity> entities = world.findEntitiesWithComponents(TestComponent1.class, TestComponent2.class);
      assertEquals(1, entities.size());
      assertEquals(entity, entities.get(entity.getIndexInWorld()));
    }

    @Test
    void whenEntityIsMissingComponent_shouldNotReturnEntity() {
      World world = new World(2);
      TestComponent1 testComponent = new TestComponent1();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getIndexInWorld(), testComponent);

      List<Entity> entities = world.findEntitiesWithComponents(TestComponent1.class, TestComponent2.class);
      assertEquals(0, entities.size());
    }
  }

  @Nested
  class ViewTests {
    @Test
    void whenViewCreated_shouldContainEntities() {
      World world = new World(2);
      TestComponent1 testComponent = new TestComponent1();
      TestComponent2 testComponent2 = new TestComponent2();
      Entity entity = world.createEntity();

      world.addComponentForEntity(entity.getIndexInWorld(), testComponent);
      world.addComponentForEntity(entity.getIndexInWorld(), testComponent2);

      var view = world.createView().of(TestComponent1.class, TestComponent2.class);
      assertEquals(1, view.getEntries().size());
      assertEquals(entity, view.getEntries().iterator().next().entity());
    }

    @Test
    void whenViewDropped_shouldNoLongerUpdate() {
      World world = new World(2);
      Entity entity = world.createEntity(new TestComponent1(), new TestComponent2());

      var view = world.createView().of(TestComponent1.class, TestComponent2.class);
      assertEquals(1, view.getEntries().size());

      world.dropView(TestComponent1.class, TestComponent2.class);
      world.removeComponent(entity.getIndexInWorld(), TestComponent1.class, true);
      assertEquals(1, view.getEntries().size());
      assertEquals(0, world.createView().of(TestComponent1.class, TestComponent2.class).getEntries().size());
    }
  }

  private record TestComponent1() implements Component {
  }

  private record TestComponent2() implements Component {
  }
}
