# sola-ecs
SolaEcs is a small entity component system implementation for Java 17.
It is being developed alongside a 2D game engine written in Java so features will be added as the engine requires them.

[![Java CI](https://github.com/iamdudeman/sola-ecs/actions/workflows/ci_build.yml/badge.svg)](https://github.com/iamdudeman/sola-ecs/actions/workflows/ci_build.yml)
[![Javadocs Link](https://img.shields.io/badge/Javadocs-blue.svg)](https://iamdudeman.github.io/sola-ecs/)
[![](https://jitpack.io/v/iamdudeman/sola-ecs.svg)](https://jitpack.io/#iamdudeman/sola-ecs)

## Download
### Gradle + Jitpack:
```kotlin
repositories {
  maven {
    url = uri("https://jitpack.io")
  }
}

dependencies {
  implementation("com.github.iamdudeman:sola-ecs:SOLA_ECS_VERSION")
}
```

### GitHub Releases:
[sola-ecs jar downloads](https://github.com/iamdudeman/sola-ecs/releases) hosted on GitHub.


## Example usage
```java
public class Example {
  public static void main(String[] args) {
    World world = new World(2);

    world.createEntity(new ExampleComponent("message one"));
    world.createEntity(new ExampleComponent("message two"));

    SolaEcs solaEcs = new SolaEcs(world, new ExampleSystem());

    solaEcs.updateWorld(16f);
  }

  private record ExampleComponent(String message) implements Component {
    @Serial
    private static final long serialVersionUID = 1429000931761226553L;
  }

  private static class ExampleSystem extends EcsSystem {
    @Override
    public void update(World world, float deltaTime) {
      world.createView().of(ExampleComponent.class)
        .forEach(view -> System.out.println(view.getC1().message()));
    }
  }
}
```


## TODO List

* view caching
  * a view is just a list of entities
  * Whenever a view is created it is cached
  * views update when
    * component add or delete of relevant component type
    * entity destruction if has component type
  * need a container of views (maybe World owns that?)
  * maybe need ability to destroy a view once it is no longer needed?
* entity name caching lookup
  * remove Optional from api
  * extra HashMap for name -> entity that is updated on entity#setName and entity#delete
* consider performance testing?
  * https://github.com/clarkware/junitperf
