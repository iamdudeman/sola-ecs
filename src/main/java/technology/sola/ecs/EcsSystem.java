package technology.sola.ecs;

/**
 * Acts on a group of {@link Entity} that contain a set of {@link Component}.
 */
public abstract class EcsSystem {
  private boolean isActive = true;

  /**
   * Called to update the state of {@link Component}s attached to an {@link Entity}.
   * <p>
   * Implementations will usually create a view via {@link World#createView()} to get {@code Entity} instances that have
   * a particular {@code Component} set to work on.
   *
   * @param world     the {@link World}
   * @param deltaTime the delta time between the last frame and the current frame
   */
  public abstract void update(World world, float deltaTime);

  /**
   * Gets the order of this System. A higher value means it will be run later in the queue.
   *
   * @return the order of this system
   */
  public int getOrder() {
    return 0;
  }

  /**
   * Returns true if this {@link EcsSystem} is active.
   *
   * @return the active state of this system
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * Sets the active state of this {@link EcsSystem}.
   *
   * @param isActive the new active state
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
