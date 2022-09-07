plugins {
  id("java-library")
  id("maven-publish")
}

version = "2.0.5"

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
  api("com.github.iamdudeman:sola-json:2.1.2")

  testImplementation(platform("org.junit:junit-bom:5.7.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")
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
