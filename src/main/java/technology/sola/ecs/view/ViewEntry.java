package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Entity;

/**
 * The ViewEntry interface defines the api for an entry in a {@link View}.
 */
@NullMarked
public interface ViewEntry {
  /**
   * @return the {@link Entity} for the {@link View} entry
   */
  Entity entity();
}
