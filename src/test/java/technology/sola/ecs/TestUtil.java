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

  private TestUtil() {
  }
}
