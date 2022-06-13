package technology.sola.ecs.io;

import org.junit.jupiter.api.Test;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.ecs.exception.ComponentJsonMapperNotFoundException;
import technology.sola.json.JsonMapper;
import technology.sola.json.JsonObject;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonWorldIoTest {
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
      Map.of(
        TestComponent1.class, TestComponent1.mapper,
        TestComponent2.class, TestComponent2.mapper
      )
    );
    String serializedWorld = base64WorldIo.stringify(world);
    World deserializedWorld = base64WorldIo.parse(serializedWorld);

    var view1 = deserializedWorld.createView().of(TestComponent1.class);
    assertEquals(uuid1, view1.get(0).entity().getUniqueId());
    assertEquals("name1", view1.get(0).entity().getName());
    assertEquals("test", view1.get(0).c1().string());

    var view2 = deserializedWorld.createView().of(TestComponent2.class);
    assertEquals(uuid2, view2.get(0).entity().getUniqueId());
    assertEquals("name2", view2.get(0).entity().getName());
    assertEquals(5, view2.get(0).c1().number());
  }

  @Test
  void stringify_whenMissingJsonMapper_shouldThrowException() {
    World world = new World(2);
    world.createEntity()
      .addComponent(new TestComponent1("test"));

    JsonWorldIo base64WorldIo = new JsonWorldIo(
      Map.of(
        TestComponent2.class, TestComponent2.mapper
      )
    );

    assertThrows(ComponentJsonMapperNotFoundException.class, () -> base64WorldIo.stringify(world));
  }

  private record TestComponent1(String string) implements Component {
    static final JsonMapper<TestComponent1> mapper = new JsonMapper<>() {
      @Override
      public JsonObject toJson(TestComponent1 testComponent1) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("string", testComponent1.string());

        return jsonObject;
      }

      @Override
      public TestComponent1 toObject(JsonObject jsonObject) {
        return new TestComponent1(jsonObject.getString("string"));
      }
    };
  }

  private record TestComponent2(int number) implements Component {
    static final JsonMapper<TestComponent2> mapper = new JsonMapper<>() {
      @Override
      public JsonObject toJson(TestComponent2 testComponent2) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("number", testComponent2.number());

        return jsonObject;
      }

      @Override
      public TestComponent2 toObject(JsonObject jsonObject) {
        return new TestComponent2(jsonObject.getInt("number"));
      }
    };
  }
}
