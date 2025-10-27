pluginManagement {
    repositories {
        google() // Adicionado para plugins
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Adicionado para dependências
        mavenCentral()
    }
}

rootProject.name = "listaCompras"
include(":app")
