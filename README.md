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
SolaEcsBenchmark.create                              ss   15   142.276 ±   3.950  ms/op
SolaEcsBenchmark.delete                              ss   15   848.474 ±   8.258  ms/op
SolaEcsBenchmark.loopingForMultipleComponent         ss   15    54.921 ±   4.567  ms/op
SolaEcsBenchmark.loopingForOneComponent              ss   15    36.198 ±   5.418  ms/op
SolaEcsBenchmark.update                              ss   15    71.988 ±  19.799  ms/op
SolaEcsBenchmark.view_create                         ss   15   176.408 ±  11.456  ms/op
SolaEcsBenchmark.view_delete                         ss   15  1477.096 ± 385.998  ms/op
SolaEcsBenchmark.view_loopingForMultipleComponent    ss   15    13.407 ±   2.058  ms/op
SolaEcsBenchmark.view_loopingForOneComponent         ss   15     7.262 ±   5.504  ms/op
SolaEcsBenchmark.view_update                         ss   15    37.329 ±  11.923  ms/op
```
