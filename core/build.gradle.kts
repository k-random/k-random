/*
 * This file was generated by the Gradle 'init' task.
 */

plugins { `java-library` }

dependencies {
  api(libs.org.objenesis.objenesis)
  api(libs.io.github.classgraph.classgraph)
  testImplementation(libs.org.junit.jupiter.junit.jupiter)
  testImplementation(libs.org.assertj.assertj.core)
  testImplementation(libs.org.mockito.mockito.core)
  testImplementation(libs.org.mockito.mockito.junit.jupiter)
}

description = "k-random Core"

java { withJavadocJar() }
