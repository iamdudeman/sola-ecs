package technology.sola.ecs.cache;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
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
      Entity testEntity = new World(1).createEntity(new TestComponent(), new TestComponent2());

      testView.addEntryIfValidEntity(testEntity);

      assertEquals(1, testView.getEntries().size());
      assertEquals(testEntity, testView.getEntries().iterator().next().entity);
    }

    @Test
    void whenInvalid_shouldNotAdd() {
      Entity testEntity = new World(1).createEntity(new TestComponent());

      testView.addEntryIfValidEntity(testEntity);

      assertEquals(0, testView.getEntries().size());
    }
  }

  @Nested
  class updateForAddComponent {
    @Test
    void whenAlreadyCached_shouldRemoveAndAddAgain() {
      Entity testEntity = new World(1).createEntity(new TestComponent2());

      testEntity.addComponent(new TestComponent());
      testView.updateForAddComponent(testEntity, TestComponent.class);
      testView.updateForAddComponent(testEntity, TestComponent.class);

      assertEquals(1, testView.getEntries().size());
      assertEquals(testEntity, testView.getEntries().iterator().next().entity);
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
      Entity testEntity = new World(1).createEntity(new TestComponent(), new TestComponent2());

      testEntity.setDisabled(true);
      testView.updateForDisabledStateChange(testEntity);
      assertEquals(0, testView.getEntries().size());

      testEntity.setDisabled(false);
      testView.updateForDisabledStateChange(testEntity);
      assertEquals(1, testView.getEntries().size());
    }

    @Test
    void whenDisabled_shouldRemoveEntryIfFound() {
      Entity testEntity = new World(1).createEntity(new TestComponent(), new TestComponent2());

      testView.addEntryIfValidEntity(testEntity);
      assertEquals(1, testView.getEntries().size());

      testEntity.setDisabled(true);
      testView.updateForDisabledStateChange(testEntity);
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
