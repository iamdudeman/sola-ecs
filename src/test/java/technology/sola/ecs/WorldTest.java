package technology.sola.ecs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.exception.WorldEntityLimitException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {
  @Nested
  class constructor {
    @Test
    void withZeroMaxEntities_shouldThrowException() {
      assertThrows(IllegalArgumentException.class, () -> new World(0));
    }

    @Test
    void withMaxEntities_shouldHaveMaxEntitiesSet() {
      var world = new World(5);

      assertEquals(5, world.getMaxEntityCount());
      assertEquals(0, world.getEntityCount());
    }
  }

  @Nested
  class createEntity {
    @Test
    void whenCreatingTooManyEntities_shouldThrowException() {
      World world = new World(2);

      assertThrows(WorldEntityLimitException.class, () -> {
        for (int i = 0; i < world.getMaxEntityCount() + 1; i++) {
          world.createEntity();
        }
      });
      assertEquals(2, world.getEntities().size());
    }

    @Test
    void whenCreatingEntities_shouldHaveTotalCount() {
      World world = new World(5);

      for (int i = 0; i < 4; i++) {
        world.createEntity();
      }

      assertEquals(4, world.getEntityCount());
    }

    @Test
    void whenCreatingEntity_shouldBeDisabledUntilUpdate() {
      World world = new World(1);

      Entity entity = world.createEntity("test", new TestComponent1());
      assertTrue(entity.isDisabled());

      world.update();
      assertFalse(entity.isDisabled());
    }

    @Test
    void whenCreatingEntity_withName_shouldNotHaveNameUntilUpdate() {
      World world = new World(1);

      Entity entity = world.createEntity("test", new TestComponent1());
      assertNull(entity.getName());

      world.update();
      assertEquals("test", entity.getName());
    }

    @Test
    void whenCreatingEntity_withComponents_shouldNotHaveComponentsUntilUpdate() {
      World world = new World(1);

      Entity entity = world.createEntity(new TestComponent1());
      assertFalse(entity.hasComponent(TestComponent1.class));

      world.update();
      assertTrue(entity.hasComponent(TestComponent1.class));
    }
  }

  @Nested
  class getEntityAtIndex {
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
  class findEntityByName {
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
  class findEntityByUniqueId {
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

  @Nested
  class getEntities {
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
  class getEnabledEntities {
    @Test
    void shouldReturnAllEnabledEntities() {
      World world = new World(2);
      Entity entity = world.createEntity();
      world.createEntity().setDisabled(true);
      world.update();

      assertEquals(1, world.getEnabledEntities().size());
      assertEquals(entity, world.getEnabledEntities().get(0));
    }
  }

  @Nested
  class findEntitiesWithComponents {
    @Test
    void whenEntityHasAllComponents_shouldReturnEntity() {
      World world = new World(2);
      TestComponent1 testComponent = new TestComponent1();
      TestComponent2 testComponent2 = new TestComponent2();
      Entity entity = world.createEntity();
      world.update();

      world.addComponent(entity.getIndexInWorld(), testComponent);
      world.addComponent(entity.getIndexInWorld(), testComponent2);

      List<Entity> entities = world.findEntitiesWithComponents(TestComponent1.class, TestComponent2.class);
      assertEquals(1, entities.size());
      assertEquals(entity, entities.get(entity.getIndexInWorld()));
    }

    @Test
    void whenEntityIsMissingComponent_shouldNotReturnEntity() {
      World world = new World(2);
      TestComponent1 testComponent = new TestComponent1();
      Entity entity = world.createEntity();
      world.update();

      world.addComponent(entity.getIndexInWorld(), testComponent);

      List<Entity> entities = world.findEntitiesWithComponents(TestComponent1.class, TestComponent2.class);
      assertEquals(0, entities.size());
    }
  }

  @Nested
  class createView {
    @Test
    void whenViewCreated_shouldContainEntities() {
      World world = new World(2);
      TestComponent1 testComponent = new TestComponent1();
      TestComponent2 testComponent2 = new TestComponent2();
      Entity entity = world.createEntity();
      world.update();

      world.addComponent(entity.getIndexInWorld(), testComponent);
      world.addComponent(entity.getIndexInWorld(), testComponent2);

      var view = world.createView().of(TestComponent1.class, TestComponent2.class);
      assertEquals(1, view.getEntries().size());
      assertEquals(entity, view.getEntries().iterator().next().entity());
    }
  }

  @Nested
  class dropView {
    @Test
    void whenViewDropped_shouldNoLongerUpdate() {
      World world = new World(2);
      Entity entity = world.createEntity(new TestComponent1(), new TestComponent2());
      world.update();

      var view = world.createView().of(TestComponent1.class, TestComponent2.class);
      assertEquals(1, view.getEntries().size());

      world.dropView(TestComponent1.class, TestComponent2.class);
      world.removeComponent(entity.getIndexInWorld(), TestComponent1.class, true);
      assertEquals(1, view.getEntries().size());
      assertEquals(0, world.createView().of(TestComponent1.class, TestComponent2.class).getEntries().size());
    }
  }


  @Test
  void whenAddingComponentsForEntity_shouldBeAbleToGetThem() {
    World world = new World(1);
    TestComponent1 testComponent = new TestComponent1();
    world.createEntity();

    world.addComponent(0, testComponent);

    assertEquals(testComponent, world.getComponent(0, TestComponent1.class));
  }

  @Test
  void whenRemovingComponentsFromEntity_shouldNotBeAbleToGetThem() {
    World world = new World(1);
    TestComponent1 testComponent = new TestComponent1();
    world.createEntity();

    world.addComponent(0, testComponent);
    world.removeComponent(0, TestComponent1.class, true);

    assertNull(world.getComponent(0, TestComponent1.class));
  }

  @Nested
  class update {
    @Test
    void whenDestroyingEntity_shouldNotBeAbleToGetComponents() {
      World world = new World(1);
      TestComponent1 testComponent = new TestComponent1();
      Entity entity = world.createEntity();

      world.addComponent(0, testComponent);
      entity.destroy();
      world.update();

      assertNull(world.getComponent(0, TestComponent1.class));
    }

    @Test
    void whenDestroyingEntity_shouldNotBeAbleToGetByName() {
      World world = new World(1);
      Entity entity = world.createEntity().setName("test");
      world.update();

      assertEquals(entity, world.findEntityByName("test"));

      entity.destroy();
      world.update();

      assertNull(world.findEntityByName("test"));
    }

    @Test
    void whenDestroyingEntity_shouldNotHaveComponentsWhenIdReused() {
      World world = new World(1);
      Entity entity = world.createEntity();
      world.update();

      world.addComponent(0, new TestComponent1());
      assertNotNull(world.getComponent(0, TestComponent1.class));

      entity.destroy();
      world.update();

      world.createEntity();
      world.update();

      assertNull(world.getComponent(0, TestComponent1.class));
    }
  }

  private record TestComponent1() implements Component {
  }

  private record TestComponent2() implements Component {
  }
}
