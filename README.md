# sola-ecs
SolaEcs is a small entity component system implementation for Java.
It is being developed alongside a 2D game engine written in Java so features will be added as the engine requires them.

[![Java CI](https://github.com/iamdudeman/sola-ecs/actions/workflows/gradle.yml/badge.svg)](https://github.com/iamdudeman/sola-ecs/actions/workflows/gradle.yml)

## Example usage
```java
public class Example {
  public static void main(String[] args) {
    World world = new World(2);
    world.createEntity().addComponent(new ExampleComponent("message one"));
    world.createEntity().addComponent(new ExampleComponent("message two"));

    SolaEcs solaEcs = new SolaEcs();
    solaEcs.setWorld(world);
    solaEcs.addSystem(new ExampleSystem());

    solaEcs.updateWorld(1f);
  }

  private record ExampleComponent(String message) implements Component<ExampleComponent> {
    @Serial
    private static final long serialVersionUID = 1429000931761226553L;

    @Override
    public Component<ExampleComponent> copy() { return new ExampleComponent(message); }
  }

  private static class ExampleSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.getView().of(ExampleComponent.class)
        .forEach(view -> {
          ExampleComponent exampleComponent = view.getC1();

          System.out.println(exampleComponent.message());
        });
    }

    @Override
    public int getOrder() { return 0; }
  }
}
```

## TODO List
* Update JavaDocs
* More test coverage
