glass {
    application {
        sugar {
            enabled.set(true)
        }
    }
}

repositories {
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "placeholder-api"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    @Suppress("VulnerableLibrariesLocal", "RedundantSuppression")
    compileOnly(libs.spigot.api)
    compileOnly(libs.placeholderapi)
}

tasks.processResources {
    val props =
        mapOf(
            "name" to rootProject.name,
            "version" to project.version,
        )
    filesMatching(listOf("plugin.yml")) {
        expand(props)
    }
}
