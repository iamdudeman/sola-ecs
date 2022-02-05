package technology.sola.ecs.exception;

public class MissingEntityException extends RuntimeException {
  public MissingEntityException(int id) {
    super("There is no Entity with id of [" + id + "] in this World");
  }
}
