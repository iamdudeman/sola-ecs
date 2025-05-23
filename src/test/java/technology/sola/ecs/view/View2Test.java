package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
class View2Test extends ViewNTestBase {
  private View2<TestComponent, TestComponent2> view;

  @BeforeEach
  void setup() {
    view = new View2<>(TestComponent.class, TestComponent2.class);
  }

  @Test
  void isWatchingComponent_shouldWatchCorrectComponents() {
    assertTrue(view.isWatchingComponent(TestComponent.class));
    assertTrue(view.isWatchingComponent(TestComponent2.class));
    assertFalse(view.isWatchingComponent(TestComponent3.class));
    assertFalse(view.isWatchingComponent(TestComponent4.class));
  }

  @Nested
  class createEntryFromEntity {
    @Test
    void whenAllComponentsPresent_shouldReturnEntity() {
      Entity mockEntity = createMockEntity(
        new ComponentMapping<>(TestComponent.class, testComponent),
        new ComponentMapping<>(TestComponent2.class, testComponent2)
      );

      var entry = view.createEntryFromEntity(mockEntity);

      assertNotNull(entry);
      assertEquals(testComponent, entry.c1());
      assertEquals(testComponent2, entry.c2());
    }

    @Test
    void whenComponentMissing_shouldReturnNull() {
      var missingCombinations = List.of(
        // one component combinations
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent2.class, testComponent2)
        ),
        // zero component
        createMockEntity()
      );

      missingCombinations.forEach(mockEntity -> {
        var entry = view.createEntryFromEntity(mockEntity);

        assertNull(entry);
      });
    }
  }
}
