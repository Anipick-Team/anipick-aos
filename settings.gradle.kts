enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Kakao
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "AniPick"
include(":app")

include(":core:common")
include(":core:designsystem")
include(":core:navigation")
include(":core:ui")
include(":core:data")
include(":core:model")
include(":core:datastore")
include(":core:network")
include(":core:firebase")

include(":feature:splash:api")
include(":feature:splash:impl")
include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:ranking:api")
include(":feature:ranking:impl")
include(":feature:explore:api")
include(":feature:explore:impl")
include(":feature:mypage:api")
include(":feature:mypage:impl")
include(":feature:review:api")
include(":feature:review:impl")
include(":feature:search:api")
include(":feature:search:impl")
include(":feature:catalog:api")
include(":feature:catalog:impl")
include(":feature:community:api")
include(":feature:community:impl")
