package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
class EcsSystemTest {
  @Test
  void whenCreated_shouldDefaultToActive() {
    assertTrue(new TestUpdateEcsSystem().isActive());
  }

  @Nested
  @DisplayName("setActive")
  class SetActiveTests {
    @Test
    void whenCalled_withFalse_shouldBeFalse() {
      EcsSystem updateSystem = new TestUpdateEcsSystem();

      updateSystem.setActive(false);

      assertFalse(updateSystem.isActive());
    }
  }

  @Test
  void getOrder_shouldDefaultToZero() {
    EcsSystem ecsSystem = new EcsSystem() {
      @Override
      public void update(World world, float deltaTime) {
      }
    };

    assertEquals(0, ecsSystem.getOrder());
  }

  public static class TestUpdateEcsSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
    }
  }
}
