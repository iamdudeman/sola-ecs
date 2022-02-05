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
    public Component<TestComponent1> copy() {
      return new TestComponent1();
    }
  }

  private TestUtil() {
  }
}
