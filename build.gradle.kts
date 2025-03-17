plugins {
  id("java-library")
  id("maven-publish")
}

version = "2.1.6"

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
  api("com.github.iamdudeman:sola-json:4.0.0")

  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
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
