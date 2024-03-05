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

rootProject.name = "CityGo"
include(":app")
include(":domain")
include(":common")
include(":common-compose")
include(":core-testing")
include(":data:network")
include(":data:repository")
include(":presentation:ui-userrequests")
include(":presentation:ui-users")
include(":model")
