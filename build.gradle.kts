plugins {
  id("java-library")
}

version = "2.0.0"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(18))
  }
}

repositories {
  mavenCentral()
}

dependencies {
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
