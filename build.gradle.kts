import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
  id("com.diffplug.spotless") version "6.25.0"
  id("com.palantir.git-version") version "3.1.0"
  java
  `maven-publish`
  signing
}

val gitVersion: groovy.lang.Closure<String> by extra

group = "io.github.k-random"

version = gitVersion().replace("^v".toRegex(), "")

println("Build Version = $version")

java.sourceCompatibility = JavaVersion.VERSION_17

subprojects {
  group = rootProject.group
  version = rootProject.version

  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  java {
    withJavadocJar()
    withSourcesJar()
  }
  configure<SpotlessExtension> { java { googleJavaFormat() } }
  tasks.withType<JavaCompile> { options.encoding = "UTF-8" }
  tasks.withType<Javadoc> { options.encoding = "UTF-8" }
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
          description = project.description
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
