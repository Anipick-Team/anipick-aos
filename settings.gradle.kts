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
include(":core:datastore")
include(":core:network")
include(":core:firebase")

include(":feature:auth:login")
include(":feature:auth:email:login")
include(":feature:auth:email:register")
include(":feature:auth:findpassword:verification")
include(":feature:auth:findpassword:reset")
include(":feature:auth:preferencesetup")

include(":feature:main:explore")
include(":feature:main:search")
include(":feature:main:ranking")
include(":feature:main:mypage")
include(":feature:main:detail")
include(":feature:main:setting")
include(":feature:main:review")

include(":feature:main:shell:home:main")
include(":feature:main:shell:home:detail")
include(":feature:main:info:anime")
include(":feature:main:info:studio")
include(":feature:main:info:character")
include(":feature:main:info:series")
include(":feature:main:info:recommend")
include(":feature:main:actor")
