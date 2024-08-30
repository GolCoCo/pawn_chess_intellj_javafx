import com.github.spotbugs.snom.Effort

plugins {
  // Apply the application plugin to add support for building a CLI application in Java.
  application

  // Quality stuff

  // cf. https://checkstyle.sourceforge.io/
  checkstyle
  // cf. https://spotbugs.github.io/
  id("com.github.spotbugs") version "6.0.11"
  // cf. https://github.com/diffplug/spotless/
  id("com.diffplug.spotless") version "6.25.0"

  id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

dependencies {
  // Use JUnit Jupiter for testing.
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

application {
  // Define the main class for the application.
  mainClass.set("bauernschach.view.Main")
}

tasks.register<JavaExec>("runShell")

tasks.named<JavaExec>("runShell") {
  mainClass.set("bauernschach.Shell")
  classpath = tasks.named<JavaExec>("run").get().classpath
  standardInput = System.`in`
  enableAssertions = true
}

javafx {
  version = "21.0.3"
  modules(
      "javafx.base",
      "javafx.swing",
      "javafx.graphics",
      "javafx.controls",
      "javafx.fxml",
      "javafx.media",
      "javafx.web")
}

tasks.named<JavaExec>("run") {
  standardInput = System.`in`
  enableAssertions = true
}

tasks.withType<Javadoc> {
  options { (this as CoreJavadocOptions).addBooleanOption("Werror", true) }
}

tasks.named<Test>("test") {
  // Use JUnit Platform for unit tests.
  useJUnitPlatform()
}

checkstyle {
  toolVersion = "10.15.0"
  maxWarnings = 0
}

spotbugs {
  effort = Effort.MAX
  ignoreFailures = false
}

tasks.spotbugsMain {
  reports.create("html") {
    required = true
    outputLocation = layout.buildDirectory.file("reports/spotbugs.html")
  }
}

tasks.spotbugsTest {
  reports.create("html") {
    required = true
    outputLocation = layout.buildDirectory.file("reports/spotbugsTests.html")
  }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  java { googleJavaFormat() }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
  }
}
