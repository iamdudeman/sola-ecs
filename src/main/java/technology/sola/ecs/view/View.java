package technology.sola.ecs.view;

import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.List;

/**
 * The View class is a container of {@link ViewEntry} that are updated whenever a {@link technology.sola.ecs.World}
 * updates an {@link Entity}'s components that now satisfies or no longer satisfies the {@link List} of watched
 * {@link Component}s.
 *
 * @param <E> the {@link ViewEntry} implementation
 */
public interface View<E extends ViewEntry> {
  /**
   * @return the {@link List} of {@link ViewEntry} in this View
   */
  List<E> getEntries();

  /**
   * Checks if this view responds to an {@link Entity} adding or removing the component type
   * or if an entity is deleted while having the component type.
   *
   * @param componentClassToCheck the {@link Component} class to check
   * @return true if view will update
   */
  boolean isWatchingComponent(Class<? extends Component> componentClassToCheck);
}
