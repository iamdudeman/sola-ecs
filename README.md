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
    // if you want to utilize JsonWorldIo implementation
    implementation("com.github.iamdudeman:sola-json:4.0.1")
}
```

[sola-ecs jar downloads](https://github.com/iamdudeman/sola-ecs/releases) hosted on GitHub releases.

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
    }

    private static class ExampleSystem extends EcsSystem {
        @Override
        public void update(World world, float deltaTime) {
            world.createView().of(ExampleComponent.class)
                .getEntries()
                .forEach(view -> System.out.println(view.getC1().message()));
        }
    }
}
```

## Performance

Execute benchmark view gradle task `jmhBenchmark` in verification category.

Results:
```
Benchmark                                  Mode  Cnt   Score   Error  Units
SolaEcsBenchmark.create                              ss   15   25.313 ±  4.572  ms/op
SolaEcsBenchmark.create_view                         ss   15   54.532 ±  9.862  ms/op
SolaEcsBenchmark.delete                              ss   15  823.973 ±  7.365  ms/op
SolaEcsBenchmark.delete_view                         ss   15  829.456 ± 11.073  ms/op
SolaEcsBenchmark.loopingForMultipleComponent         ss   15   35.879 ±  2.761  ms/op
SolaEcsBenchmark.loopingForMultipleComponent_view    ss   15    8.260 ±  2.715  ms/op
SolaEcsBenchmark.loopingForOneComponent              ss   15   19.425 ±  2.871  ms/op
SolaEcsBenchmark.loopingForOneComponent_view         ss   15    7.956 ±  4.531  ms/op
SolaEcsBenchmark.update                              ss   15    6.286 ±  1.305  ms/op
SolaEcsBenchmark.update_view                         ss   15   18.751 ± 12.513  ms/op
```
