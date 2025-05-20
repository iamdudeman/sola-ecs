package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
class View1Test extends ViewNTestBase {
  private View1<TestComponent> view;

  @BeforeEach
  void setup() {
    view = new View1<>(TestComponent.class);
  }

  @Test
  void isWatchingComponent_shouldWatchCorrectComponents() {
    assertTrue(view.isWatchingComponent(TestComponent.class));
    assertFalse(view.isWatchingComponent(TestComponent2.class));
    assertFalse(view.isWatchingComponent(TestComponent3.class));
    assertFalse(view.isWatchingComponent(TestComponent4.class));
  }

  @Nested
  class createEntryFromEntity {
    @Test
    void whenAllComponentsPresent_shouldReturnEntity() {
      Entity mockEntity = createMockEntity(
        new ComponentMapping<>(TestComponent.class, testComponent)
      );

      var entry = view.createEntryFromEntity(mockEntity);

      assertNotNull(entry);
      assertEquals(testComponent, entry.c1());
    }

    @Test
    void whenComponentMissing_shouldReturnNull() {
      var missingCombinations = List.of(
        createMockEntity()
      );

      missingCombinations.forEach(mockEntity -> {
        var entry = view.createEntryFromEntity(mockEntity);

        assertNull(entry);
      });
    }
  }
}
