plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

rootProject.name = "k-random"

include(":randomizers")

include(":bean-validation")

include(":core")
