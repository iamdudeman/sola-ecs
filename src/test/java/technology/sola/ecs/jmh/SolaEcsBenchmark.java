package technology.sola.ecs.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.view.View1;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 3, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 5)
public class SolaEcsBenchmark {
  private static final int COUNT = 500_000;

  @Benchmark
  public void loopingForOneComponent(OneComponentBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    for (var entity : world.findEntitiesWithComponents(TestComponent.class)) {
      blackhole.consume(entity.getComponent(TestComponent.class));
    }

    for (var entity : world.findEntitiesWithComponents(TestComponent.class)) {
      blackhole.consume(entity.getComponent(TestComponent.class));
    }
  }

  @Benchmark
  public void loopingForMultipleComponent(MultipleComponentBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    for (var entity : world.findEntitiesWithComponents(TestComponent.class, TestComponent2.class, TestComponent3.class)) {
      blackhole.consume(entity.getComponent(TestComponent.class));
      blackhole.consume(entity.getComponent(TestComponent2.class));
      blackhole.consume(entity.getComponent(TestComponent3.class));
    }

    for (var entity : world.findEntitiesWithComponents(TestComponent.class, TestComponent2.class, TestComponent3.class)) {
      blackhole.consume(entity.getComponent(TestComponent.class));
      blackhole.consume(entity.getComponent(TestComponent2.class));
      blackhole.consume(entity.getComponent(TestComponent3.class));
    }
  }

  @Benchmark
  public void create(Blackhole blackhole) {
    World world = new World(COUNT);

    for (int i = 0; i < world.getMaxEntityCount(); i++) {
      blackhole.consume(world.createEntity(new TestComponent()));
    }
  }

  @Benchmark
  public void update(IterationOneComponentBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    int count = 0;

    for (int i = 0; i < world.getMaxEntityCount() - 1; i++) {
      count++;

      if (count >= 4) {
        world.getEntityAtIndex(i).removeComponent(TestComponent.class);
        count = 0;
      }
    }

    for (int i = 0; i < world.getMaxEntityCount() - 1; i++) {
      count++;

      if (count >= 4) {
        var entity = world.getEntityAtIndex(i);

        if (!entity.hasComponent(TestComponent.class)) {
          entity.addComponent(new TestComponent());
        }
      }
    }

    blackhole.consume(world);
  }

  @Benchmark
  public void delete(IterationOneComponentBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    int count = 0;

    for (int i = 0; i < world.getMaxEntityCount() - 1; i++) {
      count++;

      if (count >= 4) {
        world.getEntityAtIndex(i).destroy();
        count = 0;
      }
    }

    world.cleanupDestroyedEntities();

    blackhole.consume(world);
  }

  @Benchmark
  public void loopingForOneComponent_view(OneComponentBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    var view = world.createView().of(TestComponent.class);

    for (var entry : view.getEntries()) {
      blackhole.consume(entry.c1());
    }

    for (var entry : view.getEntries()) {
      blackhole.consume(entry.c1());
    }
  }

  @Benchmark
  public void loopingForMultipleComponent_view(MultipleComponentBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    var view = world.createView().of(TestComponent.class, TestComponent2.class, TestComponent3.class);

    for (var entry : view.getEntries()) {
      blackhole.consume(entry.c1());
      blackhole.consume(entry.c2());
      blackhole.consume(entry.c3());
    }

    for (var entry : view.getEntries()) {
      blackhole.consume(entry.c1());
      blackhole.consume(entry.c2());
      blackhole.consume(entry.c3());
    }
  }

  @Benchmark
  public void create_view(Blackhole blackhole) {
    World world = new World(COUNT);

    var view = world.createView().of(TestComponent.class);

    for (int i = 0; i < world.getMaxEntityCount(); i++) {
      blackhole.consume(world.createEntity(new TestComponent()));
    }

    blackhole.consume(view);
  }

  @Benchmark
  public void update_view(IterationOneComponentWithViewBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    int count = 0;

    for (int i = 0; i < world.getMaxEntityCount() - 1; i++) {
      count++;

      if (count >= 4) {
        world.getEntityAtIndex(i).removeComponent(TestComponent.class);
        count = 0;
      }
    }

    for (int i = 0; i < world.getMaxEntityCount() - 1; i++) {
      count++;

      if (count >= 4) {
        var entity = world.getEntityAtIndex(i);

        if (!entity.hasComponent(TestComponent.class)) {
          entity.addComponent(new TestComponent());
        }
      }
    }

    blackhole.consume(world);
  }

  @Benchmark
  public void delete_view(IterationOneComponentWithViewBenchmarkState benchmarkState, Blackhole blackhole) {
    World world = benchmarkState.world;

    int count = 0;

    for (int i = 0; i < world.getMaxEntityCount() - 1; i++) {
      count++;

      if (count >= 4) {
        world.getEntityAtIndex(i).destroy();
        count = 0;
      }
    }

    world.cleanupDestroyedEntities();

    blackhole.consume(world);
  }

  private static class TestComponent implements Component {
    public String state;
  }

  private static class TestComponent2 implements Component {
    public String state;
  }

  private static class TestComponent3 implements Component {
    public String state;
  }

  @State(Scope.Thread)
  public static class OneComponentBenchmarkState {
    private World world;

    @Setup
    public void prepare() {
      world = new World(COUNT);

      for (int i = 0; i < world.getMaxEntityCount(); i++) {
        world.createEntity(new TestComponent());
      }
    }
  }

  @State(Scope.Thread)
  public static class IterationOneComponentBenchmarkState {
    private World world;

    @Setup(Level.Iteration)
    public void prepare() {
      world = new World(COUNT);

      for (int i = 0; i < world.getMaxEntityCount(); i++) {
        world.createEntity(new TestComponent());
      }
    }
  }

  @State(Scope.Thread)
  public static class IterationOneComponentWithViewBenchmarkState {
    private World world;
    private View1<TestComponent> view;

    @Setup(Level.Iteration)
    public void prepare() {
      world = new World(COUNT);

      for (int i = 0; i < world.getMaxEntityCount(); i++) {
        world.createEntity(new TestComponent());
      }

      view = world.createView().of(TestComponent.class);
    }
  }

  @State(Scope.Thread)
  public static class MultipleComponentBenchmarkState {
    private World world;

    @Setup
    public void prepare() {
      world = new World(COUNT);

      for (int i = 0; i < world.getMaxEntityCount(); i++) {
        world.createEntity(new TestComponent(), new TestComponent2(), new TestComponent3());
      }
    }
  }
}
