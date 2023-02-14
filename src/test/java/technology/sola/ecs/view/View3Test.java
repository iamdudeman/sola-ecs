package technology.sola.ecs.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class View3Test extends ViewNTestBase {
  private View3<TestComponent, TestComponent2, TestComponent3> view;

  @BeforeEach
  void setup() {
    view = new View3<>(TestComponent.class, TestComponent2.class, TestComponent3.class);
  }

  @Nested
  class createEntryFromEntity {
    @Test
    void whenAllComponentsPresent_shouldReturnEntity() {
      Entity mockEntity = createMockEntity(
        new ComponentMapping<>(TestComponent.class, testComponent),
        new ComponentMapping<>(TestComponent2.class, testComponent2),
        new ComponentMapping<>(TestComponent3.class, testComponent3)
      );

      var entry = view.createEntryFromEntity(mockEntity);

      assertNotNull(entry);
      assertEquals(testComponent, entry.c1());
      assertEquals(testComponent2, entry.c2());
      assertEquals(testComponent3, entry.c3());
    }

    @Test
    void whenComponentMissing_shouldReturnNull() {
      var missingCombinations = List.of(
        // two component combinations
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent2.class, testComponent2)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent2.class, testComponent2),
          new ComponentMapping<>(TestComponent3.class, testComponent3)
        ),
        createMockEntity(
          new ComponentMapping<>(TestComponent.class, testComponent),
          new ComponentMapping<>(TestComponent3.class, testComponent3)
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
