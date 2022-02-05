package technology.sola.ecs;

import java.io.Serializable;

public interface Component<T> extends Serializable {
  /**
   * Creates a new instance of this Component that is a copy.
   *
   * @return  the new instance copy
   */
  Component<T> copy();
}
