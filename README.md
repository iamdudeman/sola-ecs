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
SolaEcsBenchmark.create                              ss   15  24.515 ±  4.382  ms/op
SolaEcsBenchmark.create_view                         ss   15  52.200 ±  7.615  ms/op
SolaEcsBenchmark.delete                              ss   15   5.524 ±  1.833  ms/op
SolaEcsBenchmark.delete_view                         ss   15   8.219 ±  3.915  ms/op
SolaEcsBenchmark.loopingForMultipleComponent         ss   15  35.652 ±  2.716  ms/op
SolaEcsBenchmark.loopingForMultipleComponent_view    ss   15   6.220 ±  4.172  ms/op
SolaEcsBenchmark.loopingForOneComponent              ss   15  19.733 ±  2.862  ms/op
SolaEcsBenchmark.loopingForOneComponent_view         ss   15   5.745 ±  2.586  ms/op
SolaEcsBenchmark.update                              ss   15   6.430 ±  1.943  ms/op
SolaEcsBenchmark.update_view                         ss   15  16.782 ± 11.091  ms/op
```
