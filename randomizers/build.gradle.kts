/*
 * This file was generated by the Gradle 'init' task.
 */

plugins { kotlin("jvm") }

dependencies {
  // keep-sorted start
  api(libs.net.datafaker.datafaker)
  implementation(kotlin("reflect"))
  implementation(kotlin("stdlib"))
  implementation(project(":core"))
  testImplementation(libs.org.assertj.assertj.core)
  testImplementation(libs.org.junit.jupiter.junit.jupiter)
  // keep-sorted end
}

description = "k-random Randomizers"

repositories { mavenCentral() }

kotlin { jvmToolchain(17) }
