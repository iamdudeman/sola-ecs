package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@NullMarked
class View4Test extends ViewNTestBase {
  private View4<TestComponent, TestComponent2, TestComponent3, TestComponent4> view;

  @BeforeEach
  void setup() {
    view = new View4<>(TestComponent.class, TestComponent2.class, TestComponent3.class, TestComponent4.class);
  }

  @Test
  void isWatchingComponent_shouldWatchCorrectComponents() {
    assertTrue(view.isWatchingComponent(TestComponent.class));
    assertTrue(view.isWatchingComponent(TestComponent2.class));
    assertTrue(view.isWatchingComponent(TestComponent3.class));
    assertTrue(view.isWatchingComponent(TestComponent4.class));
    assertFalse(view.isWatchingComponent(TestComponent5.class));
  }

  @Nested
  class createEntryFromEntity {
    @Test
    void whenAllComponentsPresent_shouldReturnEntity() {
      Entity mockEntity = createMockEntity(
        new ComponentMapping<>(TestComponent.class, testComponent),
        new ComponentMapping<>(TestComponent2.class, testComponent2),
        new ComponentMapping<>(TestComponent3.class, testComponent3),
        new ComponentMapping<>(TestComponent4.class, testComponent4)
      );

      var entry = view.createEntryFromEntity(mockEntity);

      assertNotNull(entry);
      assertEquals(testComponent, entry.c1());
      assertEquals(testComponent2, entry.c2());
      assertEquals(testComponent3, entry.c3());
      assertEquals(testComponent4, entry.c4());
    }

    @Test
    void whenComponentMissing_shouldReturnNull() {
      var missingCombinations = List.of(
        // three component combinations
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent2.class, testComponent2),
          new ComponentMapping<>(TestComponent3.class, testComponent3)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent2.class, testComponent2),
          new ComponentMapping<>(TestComponent4.class, testComponent4)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent2.class, testComponent2),
          new ComponentMapping<>(TestComponent3.class, testComponent3),
          new ComponentMapping<>(TestComponent4.class, testComponent4)
        ),
        // two component combinations
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent2.class, testComponent2)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent3.class, testComponent3)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent4.class, testComponent4)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent2.class, testComponent2),
          new ComponentMapping<>(TestComponent3.class, testComponent3)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent2.class, testComponent2),
          new ComponentMapping<>(TestComponent4.class, testComponent4)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent3.class, testComponent3),
          new ComponentMapping<>(TestComponent4.class, testComponent4)
        ),
        // one component combinations
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent2.class, testComponent2)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent3.class, testComponent3)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent4.class, testComponent4)
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
