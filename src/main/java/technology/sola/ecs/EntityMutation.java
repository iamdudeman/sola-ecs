package technology.sola.ecs;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class EntityMutation {
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

  /*
  todo update order

  1. isDestroyed
  2. isNew : entityIndex == null
  3. newComponents
  4. removedComponents
  5. isNameChanged -> newName
   */
}
