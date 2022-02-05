package technology.sola.ecs;

import java.io.Serial;

public class TestUtil {
  public static class TestUpdateEcsSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
    }

    @Override
    public int getOrder() {
      return 0;
    }
  }

  public static class TestComponent1 implements Component<TestComponent1> {
    @Serial
    private static final long serialVersionUID = 2415102661278676929L;

    @Override
    public TestComponent1 copy() {
      return new TestComponent1();
    }
  }

  public static class TestComponent2 implements Component<TestComponent2> {
    @Serial
    private static final long serialVersionUID = -2938119735776064413L;

    @Override
    public TestComponent2 copy() {
      return new TestComponent2();
    }
  }

  public static class TestComponent3 implements Component<TestComponent3> {
    @Serial
    private static final long serialVersionUID = 1803929311279579781L;

    @Override
    public TestComponent3 copy() {
      return new TestComponent3();
    }
  }

  private TestUtil() {
  }
}
