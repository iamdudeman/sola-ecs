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
SolaEcsBenchmark.create                              ss   15   132.569 ±   7.735  ms/op
SolaEcsBenchmark.create_view                         ss   15   159.102 ±   4.411  ms/op
SolaEcsBenchmark.delete                              ss   15  1164.505 ± 403.617  ms/op
SolaEcsBenchmark.delete_view                         ss   15  1377.409 ± 367.182  ms/op
SolaEcsBenchmark.loopingForMultipleComponent         ss   15    43.326 ±  10.135  ms/op
SolaEcsBenchmark.loopingForMultipleComponent_view    ss   15     7.492 ±   2.666  ms/op
SolaEcsBenchmark.loopingForOneComponent              ss   15    19.926 ±   2.919  ms/op
SolaEcsBenchmark.loopingForOneComponent_view         ss   15     8.122 ±   4.259  ms/op
SolaEcsBenchmark.update                              ss   15    10.057 ±   0.993  ms/op
SolaEcsBenchmark.update_view                         ss   15    15.270 ±   4.982  ms/op
```
