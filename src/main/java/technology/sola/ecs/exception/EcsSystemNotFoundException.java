package technology.sola.ecs.exception;

import technology.sola.ecs.EcsSystem;

import java.io.Serial;

/**
 * Exception thrown when an {@link EcsSystem} was not found.
 */
public class EcsSystemNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 5733761713413878468L;

  public EcsSystemNotFoundException(Class<? extends EcsSystem> ecsSystemClass) {
    super("EcsSystem for class [" + ecsSystemClass.getName() + "] was not found");
  }
}
