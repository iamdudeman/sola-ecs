/**
 * Defines the sola-ecs API.
 */
module technology.sola.ecs {
  requires transitive technology.sola.json;
  requires org.jspecify;

  exports technology.sola.ecs;
  exports technology.sola.ecs.exception;
  exports technology.sola.ecs.io;
  exports technology.sola.ecs.view;
}
