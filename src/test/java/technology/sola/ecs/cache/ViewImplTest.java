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

class ViewImplTest {
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
    void whenAlreadyCached_shouldRemoveAndAddAgain() {
      TestView spiedView = Mockito.spy(testView);
      Entity mockEntity = Mockito.mock(Entity.class);

      spiedView.getEntries().add(new TestView.TestViewEntry(mockEntity));
      spiedView.updateForAddComponent(mockEntity, TestComponent.class);

      Mockito.verify(spiedView, Mockito.times(1)).updateForDeletedEntity(mockEntity);
      Mockito.verify(spiedView, Mockito.times(1)).addEntryIfValidEntity(mockEntity);
    }

    @Test
    void whenWatchingComponent_shouldAddEntryIfValid() {
      TestView spiedView = Mockito.spy(testView);
      Entity mockEntity = Mockito.mock(Entity.class);

      spiedView.updateForAddComponent(mockEntity, TestComponent.class);

      Mockito.verify(spiedView, Mockito.times(1)).addEntryIfValidEntity(mockEntity);
    }

    @Test
    void whenNotWatchingComponent_shouldNotAddEntry() {
      TestView spiedView = Mockito.spy(testView);
      Entity mockEntity = Mockito.mock(Entity.class);

      spiedView.updateForAddComponent(mockEntity, TestComponent3.class);

      Mockito.verify(spiedView, Mockito.times(0)).addEntryIfValidEntity(mockEntity);
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
      Entity mockEntity = Mockito.mock(Entity.class);
      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);
      Mockito.when(mockEntity.isDisabled()).thenReturn(false);

      testView.updateForDisabledStateChange(mockEntity);

      assertEquals(1, testView.getEntries().size());
    }

    @Test
    void whenDisabled_shouldRemoveEntryIfFound() {
      Entity mockEntity = Mockito.mock(Entity.class);
      Mockito.when(mockEntity.hasComponent(TestComponent.class)).thenReturn(true);
      Mockito.when(mockEntity.hasComponent(TestComponent2.class)).thenReturn(true);
      testView.getEntries().add(new TestView.TestViewEntry(mockEntity));
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
      testView.getEntries().add(new TestView.TestViewEntry(mockEntity));

      testView.updateForDeletedEntity(mockEntity);

      assertEquals(0, testView.getEntries().size());
    }
  }

  private static class TestView extends ViewImpl<TestView.TestViewEntry> {
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

  private record TestComponent3() implements Component {
  }
}
