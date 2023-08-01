pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "orderbook_assignment"

include(":common:bloc")

include(":android:common:presentation")

include(":features:orderbook:domain")
include(":features:orderbook:presentation")
include(":features:orderbook:repository")
include(":features:orderbook:datasource")
include(":features:orderbook:ui")

include(":application:assignment")
