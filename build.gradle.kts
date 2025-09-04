plugins {
  id("idea")
  id("java-library")
  id("maven-publish")
}

version = "2.2.0"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }

  withSourcesJar()
  withJavadocJar()
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://jitpack.io")
  }
}

dependencies {
  api("com.github.iamdudeman:sola-json:4.0.3")

  // nullability annotations
  api("org.jspecify:jspecify:1.0.0")

  // unit testing
  testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
  testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")

  // performance testing dependencies
  testImplementation("org.openjdk.jmh:jmh-core:1.37")
  testAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

tasks.javadoc {
  options.memberLevel = JavadocMemberLevel.PROTECTED
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "technology.sola.ecs"
      artifactId = "sola-ecs"
      version = version

      from(components["java"])
    }
  }
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

tasks.register("jmhBenchmark", JavaExec::class) {
  group = "verification"
  description = "Execute jmh benchmark comparisons"
  mainClass = "org.openjdk.jmh.Main"

  classpath = sourceSets.test.get().runtimeClasspath
}
