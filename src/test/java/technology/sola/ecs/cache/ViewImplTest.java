package technology.sola.ecs.cache;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.view.ViewEntry;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewImplTest {
  private TestView testView;

  @BeforeEach
  void setup() {
    testView = new TestView();
  }

  @Nested
  class isWatchingComponent {
    @Test
    void whenWatching_shouldReturnTrue() {
      assertTrue(testView.isWatchingComponent(TestComponent.class));
      assertTrue(testView.isWatchingComponent(TestComponent2.class));
    }

    @Test
    void whenNotWatching_shouldReturnFalse() {
      assertFalse(testView.isWatchingComponent(TestComponent3.class));
    }
  }

  @Nested
  class addEntryIfValidEntity {
    @Test
    void whenValid_shouldAdd() {
      var mockEntity = Mockito.mock(Entity.class);

      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent3.class)).thenReturn(true);

      testView.addEntryIfValidEntity(mockEntity);

      assertEquals(1, testView.getEntries().size());
      assertEquals(mockEntity, testView.getEntries().iterator().next().entity);
    }

    @Test
    void whenInvalid_shouldNotAdd() {
      var mockEntity = Mockito.mock(Entity.class);

      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);

      testView.addEntryIfValidEntity(mockEntity);

      assertEquals(0, testView.getEntries().size());
    }
  }

  @Nested
  class updateForAddComponent {
    @Test
    void whenAlreadyCached_shouldRemoveAndAddAgain() {
      var mockEntity = Mockito.mock(Entity.class);

      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);

      testView.updateForAddComponent(mockEntity, TestComponent.class);
      testView.updateForAddComponent(mockEntity, TestComponent.class);

      assertEquals(1, testView.getEntries().size());
      assertEquals(mockEntity, testView.getEntries().iterator().next().entity);
    }

    @Test
    void whenWatchingComponent_shouldAddEntryIfValid() {
      Entity mockEntity = Mockito.mock(Entity.class);

      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);

      testView.updateForAddComponent(mockEntity, TestComponent.class);

      assertEquals(1, testView.getEntries().size());
      assertEquals(mockEntity, testView.getEntries().iterator().next().entity);
    }

    @Test
    void whenNotWatchingComponent_shouldNotAddEntry() {
      TestView spiedView = Mockito.spy(testView);
      Entity mockEntity = Mockito.mock(Entity.class);

      spiedView.updateForAddComponent(mockEntity, TestComponent3.class);

      Mockito.verify(spiedView, Mockito.times(0)).addEntryIfValidEntity(mockEntity);
      assertEquals(0, testView.getEntries().size());
    }
  }

  @Nested
  class updateForRemoveComponent {
    @Test
    void whenWatchingComponent_shouldRemoveEntry() {
      TestView spiedView = Mockito.spy(testView);
      Entity mockEntity = Mockito.mock(Entity.class);

      spiedView.updateForRemoveComponent(mockEntity, TestComponent.class);

      Mockito.verify(spiedView, Mockito.times(1)).updateForDeletedEntity(mockEntity);
    }

    @Test
    void whenNotWatchingComponent_shouldDoNothing() {
      TestView spiedView = Mockito.spy(testView);
      Entity mockEntity = Mockito.mock(Entity.class);

      spiedView.updateForRemoveComponent(mockEntity, TestComponent3.class);

      Mockito.verify(spiedView, Mockito.times(0)).updateForDeletedEntity(mockEntity);
    }
  }

  @Nested
  class updateForDisabledStateChange {
    @Test
    void whenEnabled_shouldAddEntryIfValid() {
      var mockEntity = Mockito.mock(Entity.class);

      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);

      Mockito.when(mockEntity.isDisabled()).thenReturn(true);
      testView.updateForDisabledStateChange(mockEntity);
      assertEquals(0, testView.getEntries().size());

      Mockito.when(mockEntity.isDisabled()).thenReturn(false);
      testView.updateForDisabledStateChange(mockEntity);
      assertEquals(1, testView.getEntries().size());
    }

    @Test
    void whenDisabled_shouldRemoveEntryIfFound() {
      var mockEntity = Mockito.mock(Entity.class);

      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);

      testView.addEntryIfValidEntity(mockEntity);
      assertEquals(1, testView.getEntries().size());

      Mockito.when(mockEntity.isDisabled()).thenReturn(true);
      testView.updateForDisabledStateChange(mockEntity);
      assertEquals(0, testView.getEntries().size());
    }
  }

  @Nested
  class updateForDeletedEntity {
    @Test
    void shouldRemoveEntryIfFound() {
      Entity mockEntity = Mockito.mock(Entity.class);
      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);

      testView.addEntryIfValidEntity(mockEntity);
      assertEquals(1, testView.getEntries().size());

      testView.updateForDeletedEntity(mockEntity);

      assertEquals(0, testView.getEntries().size());
    }
  }

  @NullMarked
  private static class TestView extends ViewImpl<TestView.TestViewEntry> {
    public TestView() {
      super(List.of(TestComponent.class, TestComponent2.class));
    }

    @Override
    @Nullable
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

  private record TestComponent3() implements Component {
  }
}
