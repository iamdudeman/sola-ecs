package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import technology.sola.ecs.exception.EcsSystemNotFoundException;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class SolaEcsTest {
  private SolaEcs solaEcs;

  @BeforeEach
  void setup() {
    solaEcs = new SolaEcs();
  }

  @Test
  void setWorld_shouldUpdateWorld() {
    World world = new World(2);

    solaEcs.setWorld(world);

    assertEquals(world, solaEcs.getWorld());
  }

  @Nested
  class AddRemoveTests {
    @Test
    void whenAdding_shouldSortByOrder() {
      EcsSystem ecsSystem = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem.getOrder()).thenReturn(1);
      EcsSystem ecsSystem2 = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem2.getOrder()).thenReturn(-2);
      EcsSystem ecsSystem3 = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem3.getOrder()).thenReturn(0);

      solaEcs.addSystems(ecsSystem, ecsSystem2, ecsSystem3);

      Iterator<EcsSystem> iter = solaEcs.systemIterator();

      assertEquals(ecsSystem2, iter.next());
      assertEquals(ecsSystem3, iter.next());
      assertEquals(ecsSystem, iter.next());
    }

    @Test
    void whenRemoving_shouldRemoveSystem() {
      EcsSystem ecsSystem = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem.getOrder()).thenReturn(1);
      EcsSystem ecsSystem2 = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem2.getOrder()).thenReturn(-2);

      solaEcs.addSystems(ecsSystem, ecsSystem2);
      solaEcs.removeSystem(ecsSystem2);

      assertEquals(1, solaEcs.getSystems().size());
      assertEquals(ecsSystem, solaEcs.getSystems().get(0));
    }
  }

  @Nested
  @DisplayName("getSystem")
  class GetTests {
    @Test
    void whenMissing_shouldReturnNull() {
      assertThrows(EcsSystemNotFoundException.class, () -> solaEcs.getSystem(TestUpdateEcsSystem.class));
    }

    @Test
    void whenPresent_shouldReturnSystem() {
      TestUpdateEcsSystem testUpdateSystem = new TestUpdateEcsSystem();

      solaEcs.addSystem(testUpdateSystem);

      assertEquals(testUpdateSystem, solaEcs.getSystem(TestUpdateEcsSystem.class));
    }
  }

  @Nested
  @DisplayName("activeSystemIterator")
  class ActiveSystemsIteratorTests {
    @Test
    void shouldReturnActiveSystems() {
      EcsSystem ecsSystem = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem.getOrder()).thenReturn(1);
      Mockito.when(ecsSystem.isActive()).thenReturn(true);
      EcsSystem ecsSystem2 = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem2.getOrder()).thenReturn(-2);
      Mockito.when(ecsSystem2.isActive()).thenReturn(false);

      solaEcs.addSystems(ecsSystem, ecsSystem2);

      assertEquals(ecsSystem, solaEcs.activeSystemIterator().next());
    }
  }

  @Nested
  @DisplayName("updateWorld")
  class UpdateTests {
    @Test
    void shouldUpdateViaActiveSystemsInOrder() {
      EcsSystem ecsSystem = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem.getOrder()).thenReturn(1);
      Mockito.when(ecsSystem.isActive()).thenReturn(true);
      EcsSystem ecsSystem2 = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem2.getOrder()).thenReturn(-2);
      Mockito.when(ecsSystem2.isActive()).thenReturn(false);
      EcsSystem ecsSystem3 = Mockito.mock(EcsSystem.class);
      Mockito.when(ecsSystem3.getOrder()).thenReturn(-2);
      Mockito.when(ecsSystem3.isActive()).thenReturn(true);
      World mockWorld = Mockito.mock(World.class);

      solaEcs = new SolaEcs(mockWorld, ecsSystem, ecsSystem2, ecsSystem3);
      solaEcs.updateWorld(1f);

      InOrder inOrder = Mockito.inOrder(ecsSystem3, ecsSystem);
      inOrder.verify(ecsSystem3).update(mockWorld, 1f);
      inOrder.verify(ecsSystem).update(mockWorld, 1f);
      Mockito.verify(ecsSystem2, Mockito.never()).update(Mockito.any(), Mockito.anyFloat());
    }

    @Test
    void shouldCleanupDestroyedEntities() {
      World mockWorld = Mockito.mock(World.class);
      solaEcs.setWorld(mockWorld);

      solaEcs.updateWorld(1f);

      Mockito.verify(mockWorld, Mockito.times(1)).cleanupDestroyedEntities();
    }
  }

  @NullMarked
  public static class TestUpdateEcsSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
    }
  }
}
