package technology.sola.ecs.view;

import technology.sola.ecs.Entity;

/**
 * The ViewEntry interface defines the api for an entry in a {@link View}.
 */
public interface ViewEntry {
  /**
   * @return the {@link Entity} for the {@link View} entry
   */
  Entity entity();
}
