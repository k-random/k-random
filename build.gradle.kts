import com.diffplug.gradle.spotless.SpotlessExtension
import groovy.lang.Closure
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
  id("com.diffplug.spotless") version "6.25.0"
  id("com.palantir.git-version") version "3.1.0"
  id("org.jetbrains.dokka") version "1.9.20" apply false
  id("io.gitlab.arturbosch.detekt") version "1.23.7" apply false
  java
  kotlin("jvm")
  `maven-publish`
  signing
}

val gitVersion: Closure<String> by extra

group = "io.github.k-random"

version = gitVersion().replace("^v".toRegex(), "")

println("Build Version = $version")

subprojects {
  group = rootProject.group
  version = rootProject.version

  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "org.jetbrains.dokka")
  apply(plugin = "io.gitlab.arturbosch.detekt")
  java {
    withJavadocJar()
    withSourcesJar()
  }
  tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
      configureEach {
        sourceRoots.from(file("src/main/java"))
        jdkVersion.set(17)
      }
    }
  }
  tasks.named<Javadoc>("javadoc").configure { enabled = false }
  tasks.named<Jar>("javadocJar") { from(tasks.named("dokkaJavadoc")) }
  configure<SpotlessExtension> {
    java { googleJavaFormat() }
    kotlin { ktfmt().googleStyle() }
  }
  tasks.withType<JavaCompile> { options.encoding = "UTF-8" }
  tasks.named("compileJava") { dependsOn("spotlessApply") }
  tasks.withType<Test> { useJUnitPlatform() }
  repositories { mavenCentral() }
  publishing {
    publications {
      create<MavenPublication>("maven") {
        groupId = rootProject.group.toString()
        artifactId = "${rootProject.name}-${project.name}"
        from(components["java"])
        pom {
          name = "${rootProject.name}-${project.name}"
          description =
              "k-random is a fork of easy-random and is used to generate random Kotlin and Java beans."
          url = "https://github.com/k-random/k-random"
          inceptionYear = "2024"
          licenses {
            license {
              name = "MIT License"
              url = "https://opensource.org/licenses/MIT"
            }
          }
          developers {
            developer {
              id = "Alex-Schiff"
              name = "Alex Schiff"
              email = "alex.schiff@protonmail.com"
              url = "https://github.com/Alex-Schiff"
              roles = listOf("Lead developer")
            }
          }
          scm {
            connection = "scm:git:git://github.com/k-random/k-random.git"
            developerConnection = "scm:git:ssh://github.com/k-random/k-random.git"
            url = "https://github.com/k-random/k-random"
          }
        }
      }
    }
    repositories {
      maven { url = uri(layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()) }
    }
  }
}

allprojects {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
  configure<SpotlessExtension> {
    kotlinGradle {
      target("*.gradle.kts")
      ktfmt()
      trimTrailingWhitespace()
      endWithNewline()
    }
  }
  tasks.named("build") { dependsOn("spotlessApply") }
}

dependencies { implementation(kotlin("stdlib")) }

repositories { mavenCentral() }

kotlin { jvmToolchain(17) }
