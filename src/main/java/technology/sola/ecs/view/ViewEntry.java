package technology.sola.ecs.view;

import technology.sola.ecs.Entity;

/**
 * The ViewEntry interface defines the api for an entry in a {@link technology.sola.ecs.cache.View}.
 */
public interface ViewEntry {
  /**
   * @return the {@link Entity} for the {@link technology.sola.ecs.cache.View} entry
   */
  Entity entity();
}
