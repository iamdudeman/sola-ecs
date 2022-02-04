package technology.sola.ecs;

public abstract class EcsSystem {
  private boolean isActive = true;

  /**
   * Called to update the state of {@link Component}s attached to an {@link Entity}.
   * <p>
   * Implementations will usually create a view via {@link World#getView()} to get {@code Entity} instances that have
   * a particular {@code Component} set to work on.
   *
   * @param world  the {@code World}
   * @param deltaTime  the delta time between the last frame and the current frame
   */
  public abstract void update(World world, float deltaTime);

  /**
   * Gets the order of this System. A higher value means it will be run later in the queue.
   *
   * @return the order of this system
   */
  public abstract int getOrder();

  /**
   * Gets whether or not this System is active.
   *
   * @return the active state of this system
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * Sets the active state of this System.
   *
   * @param isActive  the new active state
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
