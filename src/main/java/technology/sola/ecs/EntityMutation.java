package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.ecs.cache.EntityNameCache;
import technology.sola.ecs.cache.ViewCache;

import java.util.ArrayList;
import java.util.List;

@NullMarked
class EntityMutation {
  // null if new
  @Nullable
  private Integer entityIndex;

  // disabled mutation
  private boolean isDisabled = false;
  private boolean isEnabled = false;

  // destroyed
  private boolean isDestroyed = false;

  // name mutation
  private boolean isNameChanged = false;
  @Nullable
  private String newName;

  // component mutations
  private List<Component> newComponents = new ArrayList<>();
  private List<Class<? extends Component>> removedComponents = new ArrayList<>();

  EntityMutation(@Nullable Integer entityIndex) {
    this.entityIndex = entityIndex;
  }

  void apply(World world, ViewCache viewCache, EntityNameCache entityNameCache) {
     /*
    todo update order

    1. isDestroyed
    2. isNew : entityIndex == null
    3. newComponents
    4. removedComponents
    5. isNameChanged -> newName
     */
  }

  void mutateName(@Nullable String newName) {
    isNameChanged = true;
    this.newName = newName;
  }

  void mutateDisabled(boolean isDisabled) {
    if (isDisabled) {
      this.isDisabled = true;
      this.isEnabled = false;
    } else {
      this.isEnabled = true;
      this.isDisabled = false;
    }
  }

  void mutateDestroyed() {
    this.isDestroyed = true;
  }

  void mutateAddComponent(Component component) {
    newComponents.add(component);
  }

  void mutateRemoveComponent(Class<? extends Component> componentClass) {
    removedComponents.add(componentClass);
  }
}
