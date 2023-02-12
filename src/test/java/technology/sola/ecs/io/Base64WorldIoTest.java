package technology.sola.ecs.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.exception.Base64WorldIoException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class Base64WorldIoTest {
  @Mock
  private static Consumer<String> mockConsumer;

  @Test
  void integrationTest() {
    World world = new World(2);
    String uuid1 = world.createEntity()
      .addComponent(new TestComponent1())
      .getUniqueId();
    String uuid2 = world.createEntity()
      .addComponent(new TestComponent2())
      .getUniqueId();

    Base64WorldIo base64WorldIo = new Base64WorldIo();
    String serializedWorld = base64WorldIo.stringify(world);
    World deserializedWorld = base64WorldIo.parse(serializedWorld);

//    assertEquals(uuid1, deserializedWorld.of().of(TestComponent1.class).get(0).entity().getUniqueId());
//    assertEquals(uuid2, deserializedWorld.of().of(TestComponent2.class).get(0).entity().getUniqueId());
    Mockito.verify(mockConsumer, Mockito.times(1)).accept("test1");
    Mockito.verify(mockConsumer, Mockito.times(1)).accept("test2");

  }

  @Test
  void parse_whenNotWorldString_shouldThrowException() {
    assertThrows(Base64WorldIoException.class, () -> new Base64WorldIo().parse(Base64.getEncoder().encodeToString("bogus_world".getBytes(StandardCharsets.UTF_8))));
  }

  private record TestComponent1() implements Component {
    @Override
    public void afterDeserialize(World world) {
      mockConsumer.accept("test1");
    }
  }

  private record TestComponent2() implements Component {
    @Override
    public void afterDeserialize(World world) {
      mockConsumer.accept("test2");
    }
  }
}
