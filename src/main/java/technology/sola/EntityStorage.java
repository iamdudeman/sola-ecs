package technology.sola;

import org.jspecify.annotations.Nullable;
import technology.sola.ecs.Component;
import technology.sola.ecs.Entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class EntityStorage {
  int totalEntityCount = 0;
  @Nullable Entity[] entities;
  final Map<Class<? extends Component>, @Nullable Component[]> components = new HashMap<>();
  final Function<Class<? extends Component>, @Nullable Component[]> componentsMappingFunction = (key) -> new Component[this.currentMaxEntityCount];
  private int currentMaxEntityCount;
  private int currentEntityIndex = 0;

  EntityStorage(int initialCapacity) {
    this.currentMaxEntityCount = initialCapacity;
    this.entities = new Entity[initialCapacity];
  }

  int nextOpenEntityIndex() {
    int totalEntityCounter = 1; // Starting at 1 for this entity being created

    while (entities[currentEntityIndex] != null) {
      currentEntityIndex = (currentEntityIndex + 1) % currentMaxEntityCount;
      totalEntityCounter++;

      if (totalEntityCounter > currentMaxEntityCount) {
        resize();
      }
    }

    return currentEntityIndex;
  }

  private void resize() {
    this.currentMaxEntityCount = (int) (currentMaxEntityCount * 1.5f) + 1;

    entities = Arrays.copyOf(entities, this.currentMaxEntityCount);

    for (var entry : components.entrySet()) {
      entry.setValue(Arrays.copyOf(entry.getValue(), this.currentMaxEntityCount));
    }
  }
}
