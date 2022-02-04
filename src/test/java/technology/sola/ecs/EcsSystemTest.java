package technology.sola.ecs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcsSystemTest {
  @Test
  void whenCreated_shouldDefaultToActive() {
    assertTrue(new TestUtil.TestUpdateEcsSystem().isActive());
  }

  @Nested
  @DisplayName("setActive")
  class SetActiveTests {
    @Test
    void whenCalled_withFalse_shouldBeFalse() {
      EcsSystem updateSystem = new TestUtil.TestUpdateEcsSystem();

      updateSystem.setActive(false);

      assertFalse(updateSystem.isActive());
    }
  }
}
