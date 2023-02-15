package technology.sola.ecs.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.World;
import technology.sola.ecs.view.ViewEntry;

import java.util.List;

class ViewCacheTest {
  private ViewCache viewCache;

  @BeforeEach
  void setup() {
    World mockWorld = Mockito.mock(World.class);

    viewCache = new ViewCache(mockWorld);
  }

  @Nested
  class createView {
    @Test
    void whenCalledTwice_shouldReturnSameInstance() {
      // todo
    }

    @Test
    void shouldInitializeViews() {
      // todo
    }
  }

  @Test
  void updateForAddComponent_shouldUpdateEachView() {
    // todo
  }

  @Test
  void updateCacheForRemoveComponent_shouldUpdateEachView() {
    // todo
  }

  @Test
  void updateCacheForDeletedEntity_shouldUpdateEachView() {
    // todo
  }

  private static class TestView extends View<TestViewEntry> {
    public TestView() {
      super(List.of(TestComponent.class, TestComponent2.class));
    }

    @Override
    protected TestViewEntry createEntryFromEntity(Entity entity) {
      return null;
    }
  }

  private record TestViewEntry(Entity entity) implements ViewEntry {
  }

  private record TestComponent() implements Component {
  }

  private record TestComponent2() implements Component {
  }
}
