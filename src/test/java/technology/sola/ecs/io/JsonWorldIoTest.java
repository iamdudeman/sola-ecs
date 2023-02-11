package technology.sola.ecs.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.exception.ComponentJsonMapperNotFoundException;
import technology.sola.json.JsonObject;
import technology.sola.json.builder.JsonObjectBuilder;
import technology.sola.json.mapper.JsonMapper;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class JsonWorldIoTest {
  @Mock
  private static Consumer<String> mockConsumer;

  @Test
  void integrationTest() {
    World world = new World(2);
    String uuid1 = world.createEntity()
      .addComponent(new TestComponent1("test"))
      .setName("name1")
      .getUniqueId();
    String uuid2 = world.createEntity()
      .addComponent(new TestComponent2(5))
      .setName("name2")
      .getUniqueId();

    JsonWorldIo base64WorldIo = new JsonWorldIo(
      List.of(TestComponent1.mapper, TestComponent2.mapper)
    );
    String serializedWorld = base64WorldIo.stringify(world);
    World deserializedWorld = base64WorldIo.parse(serializedWorld);

    var view1 = deserializedWorld.viewBuilder().createView(TestComponent1.class);
    assertEquals(uuid1, view1.getEntries().get(0).entity().getUniqueId());
    assertEquals("name1", view1.getEntries().get(0).entity().getName());
    assertEquals("test", view1.getEntries().get(0).c1().string());
    Mockito.verify(mockConsumer, Mockito.times(1)).accept("test1");

    var view2 = deserializedWorld.viewBuilder().createView(TestComponent2.class);
    assertEquals(uuid2, view2.getEntries().get(0).entity().getUniqueId());
    assertEquals("name2", view2.getEntries().get(0).entity().getName());
    assertEquals(5, view2.getEntries().get(0).c1().number());
    Mockito.verify(mockConsumer, Mockito.times(1)).accept("test2");
  }

  @Test
  void stringify_whenMissingJsonMapper_shouldThrowException() {
    World world = new World(2);
    world.createEntity()
      .addComponent(new TestComponent1("test"));

    JsonWorldIo base64WorldIo = new JsonWorldIo(
      List.of(TestComponent2.mapper)
    );

    assertThrows(ComponentJsonMapperNotFoundException.class, () -> base64WorldIo.stringify(world));
  }

  private record TestComponent1(String string) implements Component {
    @Override
    public void afterDeserialize(World world) {
      mockConsumer.accept("test1");
    }

    static final JsonMapper<TestComponent1> mapper = new JsonMapper<>() {
      @Override
      public Class<TestComponent1> getObjectClass() {
        return TestComponent1.class;
      }

      @Override
      public JsonObject toJson(TestComponent1 testComponent1) {
        return new JsonObjectBuilder()
          .addString("string", testComponent1.string())
          .build();
      }

      @Override
      public TestComponent1 toObject(JsonObject jsonObject) {
        return new TestComponent1(jsonObject.getString("string"));
      }
    };
  }

  private record TestComponent2(int number) implements Component {
    @Override
    public void afterDeserialize(World world) {
      mockConsumer.accept("test2");
    }

    static final JsonMapper<TestComponent2> mapper = new JsonMapper<>() {
      @Override
      public Class<TestComponent2> getObjectClass() {
        return TestComponent2.class;
      }

      @Override
      public JsonObject toJson(TestComponent2 testComponent2) {
        return new JsonObjectBuilder()
          .addInt("number", testComponent2.number())
          .build();
      }

      @Override
      public TestComponent2 toObject(JsonObject jsonObject) {
        return new TestComponent2(jsonObject.getInt("number"));
      }
    };
  }
}
