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
    compileOnly(libs.minecraft.next.spigot)
    runtimeOnly(libs.mysql.connector.j)
}

tasks.processResources {
    val props =
        mapOf(
            "name" to project.name,
            "version" to project.version,
        )
    filesMatching(listOf("plugin.yml")) {
        expand(props)
    }
}
