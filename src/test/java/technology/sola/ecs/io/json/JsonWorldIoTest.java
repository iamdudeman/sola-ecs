package technology.sola.ecs.io.json;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import technology.sola.ecs.Component;
import technology.sola.ecs.World;
import technology.sola.json.JsonObject;
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

    world.update();

    JsonWorldIo worldIo = new JsonWorldIo(
      List.of(TestComponent1.mapper, TestComponent2.mapper)
    );
    String serializedWorld = worldIo.stringify(world);
    World deserializedWorld = worldIo.parse(serializedWorld);

    var view1 = deserializedWorld.createView().of(TestComponent1.class);
    assertEquals(uuid1, view1.getEntries().iterator().next().entity().getUniqueId());
    assertEquals("name1", view1.getEntries().iterator().next().entity().getName());
    assertEquals("test", view1.getEntries().iterator().next().c1().string());
    Mockito.verify(mockConsumer, Mockito.times(1)).accept("test1");

    var view2 = deserializedWorld.createView().of(TestComponent2.class);
    assertEquals(uuid2, view2.getEntries().iterator().next().entity().getUniqueId());
    assertEquals("name2", view2.getEntries().iterator().next().entity().getName());
    assertEquals(5, view2.getEntries().iterator().next().c1().number());
    Mockito.verify(mockConsumer, Mockito.times(1)).accept("test2");
  }

  @Test
  void stringify_whenMissingJsonMapper_shouldThrowException() {
    World world = new World(2);
    world.createEntity()
      .addComponent(new TestComponent1("test"));

    JsonWorldIo worldIo = new JsonWorldIo(
      List.of(TestComponent2.mapper)
    );

    assertThrows(ComponentJsonMapperNotFoundException.class, () -> worldIo.stringify(world));
  }

  @NullMarked
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
        return new JsonObject()
          .put("string", testComponent1.string());
      }

      @Override
      public TestComponent1 toObject(JsonObject jsonObject) {
        return new TestComponent1(jsonObject.getString("string"));
      }
    };
  }

  @NullMarked
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
        return new JsonObject()
          .put("number", testComponent2.number());
      }

      @Override
      public TestComponent2 toObject(JsonObject jsonObject) {
        return new TestComponent2(jsonObject.getInt("number"));
      }
    };
  }
}
