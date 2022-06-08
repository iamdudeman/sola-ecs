package technology.sola.ecs.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EcsViewFactoryTest {
  private EcsViewFactory ecsViewFactory;

  @BeforeEach
  void setup() {
    World world = new World(1);
    ecsViewFactory = new EcsViewFactory(world);

    world.createEntity(new TestComponent1());
  }

  private record TestComponent1() implements Component {
  }

  private record TestComponent2() implements Component {
  }

  @Test
  void of_withOne_shouldProperlyMatchForAllVariations() {
    // match
    assertEquals(1, ecsViewFactory.of(TestComponent1.class).size());
    // no match
    assertEquals(0, ecsViewFactory.of(TestComponent2.class).size());
  }

  @Test
  void of_withTwo_shouldProperlyMatchForAllVariations() {
    // match
    assertEquals(1, ecsViewFactory.of(TestComponent1.class, TestComponent1.class).size());
    // no match
    assertEquals(0, ecsViewFactory.of(TestComponent2.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent2.class).size());
  }

  @Test
  void of_withThree_shouldProperlyMatchForAllVariations() {
    // match
    assertEquals(1, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    // no match
    assertEquals(0, ecsViewFactory.of(TestComponent2.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent2.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent2.class).size());
  }

  @Test
  void of_withFour_shouldProperlyMatchForAllVariations() {
    // match
    assertEquals(1, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    // no match
    assertEquals(0, ecsViewFactory.of(TestComponent2.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent2.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent2.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent2.class).size());
  }

  @Test
  void of_withFive_shouldProperlyMatchForAllVariations() {
    // match
    assertEquals(1, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    // no match
    assertEquals(0, ecsViewFactory.of(TestComponent2.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent2.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent2.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent2.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent2.class).size());
  }

  @Test
  void of_withSix_shouldProperlyMatchForAllVariations() {
    // match
    assertEquals(1, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    // no match
    assertEquals(0, ecsViewFactory.of(TestComponent2.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent2.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent2.class, TestComponent1.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent2.class, TestComponent1.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent2.class, TestComponent1.class).size());
    assertEquals(0, ecsViewFactory.of(TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent1.class, TestComponent2.class).size());
  }
}
