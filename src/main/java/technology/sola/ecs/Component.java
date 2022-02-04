package technology.sola.ecs;

import java.io.Serializable;

public interface Component<T> extends Serializable {
  Component<T> copy();
}
