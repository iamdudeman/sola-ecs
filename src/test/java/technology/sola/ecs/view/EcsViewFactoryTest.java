package technology.sola.ecs.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Entity;
import technology.sola.ecs.TestUtil;
import technology.sola.ecs.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EcsViewFactoryTest {
  private static final int NUMBER_OF_VIEW_CLASSES = 5;
  private World world;
  private EcsViewFactory ecsViewFactory;

  @BeforeEach
  void setup() {
    world = new World(NUMBER_OF_VIEW_CLASSES);
    ecsViewFactory = new EcsViewFactory(world);
  }

  @Nested
  class OfOneTests {
    protected TestUtil.TestComponent1 testComponent1;
    private Entity entity;

    @BeforeEach
    void setup() {
      testComponent1 = new TestUtil.TestComponent1();

      entity = world.createEntity()
        .addComponent(testComponent1);
    }

    @Test
    void whenEntityHasComponents_shouldBeInView() {
      var view = ecsViewFactory.of(TestUtil.TestComponent1.class);

      assertEquals(1, view.size());
      assertEquals(entity, view.get(0).entity());
      assertEquals(testComponent1, view.get(0).c1());
    }

    @Test
    void whenEntityMissingComponent_shouldNotBeInView() {
      assertEquals(
        0,
        ecsViewFactory.of(TestUtil.TestComponent2.class).size()
      );
    }
  }

  @Nested
  class OfTwoTests extends OfOneTests {
    protected TestUtil.TestComponent2 testComponent2;
    private Entity entity;

    @BeforeEach
    void setup() {
      super.setup();

      testComponent2 = new TestUtil.TestComponent2();

      entity = world.createEntity()
        .addComponent(testComponent1)
        .addComponent(testComponent2);
    }

    @Override
    @Test
    void whenEntityHasComponents_shouldBeInView() {
      var view = ecsViewFactory.of(TestUtil.TestComponent1.class, TestUtil.TestComponent2.class);

      assertEquals(1, view.size());
      assertEquals(entity, view.get(0).entity());
      assertEquals(testComponent1, view.get(0).c1());
      assertEquals(testComponent2, view.get(0).c2());
    }

    @Override
    @Test
    void whenEntityMissingComponent_shouldNotBeInView() {
      assertEquals(
        0,
        ecsViewFactory.of(TestUtil.TestComponent1.class, TestUtil.TestComponent3.class).size()
      );
      assertEquals(
        0,
        ecsViewFactory.of(TestUtil.TestComponent2.class, TestUtil.TestComponent3.class).size()
      );
    }
  }
}
