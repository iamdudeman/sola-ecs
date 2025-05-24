package technology.sola.ecs.view;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;
import technology.sola.ecs.cache.ViewImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewN is a {@link ViewImpl} implementation for {@link ViewEntry} for N many {@link Component}s.
 */
@NullMarked
public class ViewN extends ViewImpl<ViewNEntry> {
  /**
   * Creates an instance for desired component classes.
   *
   * @param componentClasses the component classes for the view
   */
  public ViewN(Class<? extends Component>[] componentClasses) {
    super(List.of(componentClasses));
  }

  @Override
  protected @Nullable ViewNEntry createEntryFromEntity(Entity entity) {
    List<Component> components = new ArrayList<>(componentClasses.size());

    for (var componentClass : componentClasses) {
      var component = entity.getComponent(componentClass);

      if (component == null) {
        return null;
      }

      components.add(component);
    }

    return new ViewNEntry(entity, components);
  }
}
