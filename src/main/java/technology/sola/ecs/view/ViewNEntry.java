package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.List;

/**
 * {@link ViewEntry} implementation for {@link ViewN}.
 *
 * @param entity     the {@link Entity}
 * @param components the list of {@link Component}s
 */
@NullMarked
public record ViewNEntry(
  Entity entity,
  List<Component> components
) implements ViewEntry {
  /**
   * Gets a component from the component array at desired index. The order is based on how the view was created.
   *
   * @param index the index of the component in the array
   * @param <T>   the {@link Component} type
   * @return the component or null
   */
  @SuppressWarnings("unchecked")
  <T extends Component> T c(int index) {
    return (T) components.get(index);
  }

  /**
   * Gets a component from the component array at desired index. The order is based on how the view was created.
   *
   * @param index          the index of the component in the array
   * @param componentClass the class of the component for the explicit cast
   * @param <T>            the {@link Component} type
   * @return the component or null
   */
  <T extends Component> T c(int index, Class<T> componentClass) {
    return componentClass.cast(components.get(index));
  }
}
