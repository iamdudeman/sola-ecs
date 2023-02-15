package technology.sola.ecs.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.ViewEntry;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewTest {
  private TestView testView;

  @BeforeEach
  void setup() {
    testView = new TestView();
  }

  @Nested
  class addEntryIfValidEntity {
    @Test
    void whenValid_shouldAdd() {
      Entity mockEntity = Mockito.mock(Entity.class);
      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);

      testView.addEntryIfValidEntity(mockEntity);

      assertEquals(1, testView.getEntries().size());
      assertEquals(mockEntity, testView.getEntries().get(0).entity);
    }

    @Test
    void whenInvalid_shouldNotAdd() {
      Entity mockEntity = Mockito.mock(Entity.class);
      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(false);

      testView.addEntryIfValidEntity(mockEntity);

      assertEquals(0, testView.getEntries().size());
    }
  }

  @Nested
  class updateForAddComponent {
    @Test
    void whenAlreadyCached_shouldDoNothing() {
      // todo
    }

    @Test
    void whenWatchingComponent_shouldAddEntryIfValid() {
      // todo
    }

    @Test
    void whenWatchingComponent_shouldNotAddEntryIfInvalid() {
      // todo
    }
  }

  @Nested
  class updateForRemoveComponent {
    @Test
    void whenWatchingComponent_shouldRemoveEntry() {
      // todo
    }

    @Test
    void whenNotWatchingComponent_shouldDoNothing() {
      // todo
    }
  }

  @Nested
  class updateForDeletedEntity {
    @Test
    void shouldRemoveEntryIfFound() {

    }
  }

  private static class TestView extends View<TestView.TestViewEntry> {
    public TestView() {
      super(List.of(TestComponent.class, TestComponent2.class));
    }

    @Override
    protected TestViewEntry createEntryFromEntity(Entity entity) {
      if (entity.hasComponent(TestComponent.class) && entity.hasComponent(TestComponent2.class)) {
        return new TestViewEntry(entity);
      }

      return null;
    }

    private record TestViewEntry(Entity entity) implements ViewEntry {
    }
  }

  private record TestComponent() implements Component {
  }

  private record TestComponent2() implements Component {
  }
}
