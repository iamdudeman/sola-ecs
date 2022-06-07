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

  public record TestComponent1() implements Component {
    @Serial
    private static final long serialVersionUID = 2415102661278676929L;
  }

  public record TestComponent2() implements Component {
    @Serial
    private static final long serialVersionUID = -2938119735776064413L;
  }

  public record TestComponent3() implements Component {
    @Serial
    private static final long serialVersionUID = 1803929311279579781L;
  }

  private TestUtil() {
  }
}
