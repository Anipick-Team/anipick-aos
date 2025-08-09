pluginManagement {
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

include(":core:ui")
include(":core:data")

include(":core:domain")
include(":core:model")
include(":feature:auth:login")
include(":feature:auth:email")
include(":feature:auth:findpassword")
include(":feature:auth:preferencesetup")
include(":feature:main:home")
include(":feature:main:explore")
include(":feature:main:search")
include(":feature:main:ranking")
include(":feature:main:mypage")
include(":feature:main:detail")
include(":core:datastore")
include(":core:network")
include(":feature:main:setting")
include(":feature:main:review")
