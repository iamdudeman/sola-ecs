package technology.sola.ecs.exception;

import org.jspecify.annotations.NullMarked;

import java.io.Serial;

/**
 * Exception thrown when a {@link technology.sola.ecs.io.JsonWorldIo} does not have the required
 * {@link technology.sola.json.mapper.JsonMapper} for a {@link technology.sola.ecs.Component} registered to it.
 */
@NullMarked
public class ComponentJsonMapperNotFoundException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 5486800727450528189L;

  /**
   * Creates a new instance of this exception.
   *
   * @param componentClass the {@link technology.sola.ecs.Component} that is missing a {@link technology.sola.json.mapper.JsonMapper}
   */
  public ComponentJsonMapperNotFoundException(String componentClass) {
    super("JsonMapper for Component class [" + componentClass + "] not found for this WorldJsonMapper");
  }
}
