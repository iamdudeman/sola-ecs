package technology.sola.ecs;

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
    @Override
    public TestComponent1 copy() {
      return new TestComponent1();
    }
  }

  public static class TestComponent2 implements Component<TestComponent2> {
    @Override
    public TestComponent2 copy() {
      return new TestComponent2();
    }
  }

  public static class TestComponent3 implements Component<TestComponent3> {
    @Override
    public TestComponent3 copy() {
      return new TestComponent3();
    }
  }

  private TestUtil() {
  }
}
