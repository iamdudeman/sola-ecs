package technology.sola.ecs.io;

import org.junit.jupiter.api.Test;
import technology.sola.ecs.TestUtil;
import technology.sola.ecs.World;
import technology.sola.ecs.exception.Base64WorldIoException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Base64WorldIoTest {
  @Test
  void integrationTest() {
    World world = new World(2);
    String uuid1 = world.createEntity()
      .addComponent(new TestUtil.TestComponent1())
      .getUniqueId();
    String uuid2 = world.createEntity()
      .addComponent(new TestUtil.TestComponent2())
      .getUniqueId();

    Base64WorldIo base64WorldIo = new Base64WorldIo();
    String serializedWorld = base64WorldIo.stringify(world);
    World deserializedWorld = base64WorldIo.parse(serializedWorld);

    assertEquals(uuid1, deserializedWorld.getView().of(TestUtil.TestComponent1.class).get(0).getEntity().getUniqueId());
    assertEquals(uuid2, deserializedWorld.getView().of(TestUtil.TestComponent2.class).get(0).getEntity().getUniqueId());
  }

  @Test
  void parse_whenNotWorldString_shouldThrowException() {
    assertThrows(Base64WorldIoException.class, () -> new Base64WorldIo().parse(Base64.getEncoder().encodeToString("bogus_world".getBytes(StandardCharsets.UTF_8))));
  }
}
