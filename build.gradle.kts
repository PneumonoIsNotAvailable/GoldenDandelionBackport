plugins {
	id("fabric-loom") version "1.14-SNAPSHOT"
	id("maven-publish")
	id("me.modmuss50.mod-publish-plugin") version "1.1.0"
	id("dev.kikugie.fletching-table") version "0.1.0-alpha.22"
}

val javaVersion = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5"))
	JavaVersion.VERSION_21 else JavaVersion.VERSION_17
java.targetCompatibility = javaVersion
java.sourceCompatibility = javaVersion

base.archivesName.set(project.property("mod_id") as String)
version = "${project.property("mod_version")}+${stonecutter.current.project}+${property("mod_subversion")}"

repositories {

}

loom {
	splitEnvironmentSourceSets()

	mods {
		create("gdb") {
			sourceSet(sourceSets["main"])
			sourceSet(sourceSets["client"])
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${stonecutter.current.version}")
	mappings(loom.officialMojangMappings())
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	// Fabric API
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
}

tasks {
	val java = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) 21 else 17

	processResources {
		inputs.property("version", project.property("mod_version"))
		inputs.property("min_supported", project.property("min_supported_version"))
		inputs.property("max_supported", project.property("max_supported_version"))

		filesMatching("fabric.mod.json") {
			expand(
				mapOf(
					"version" to project.property("mod_version"),
					"min_supported" to project.property("min_supported_version"),
					"max_supported" to project.property("max_supported_version"),
					"java" to "$java"
				)
			)
		}
	}

	withType<JavaCompile> {
		options.release.set(java)
	}

	java {
		withSourcesJar()
	}

	jar {
		from("LICENSE") {
			rename {"${it}_${base.archivesName.get()}"}
		}
	}
}

fletchingTable {
	j52j.register("main") {
		extension("json", "umbrellas.mixins.json5")
		extension("json", "data/**/*.json5")
	}
}

publishMods {
	file = tasks.remapJar.get().archiveFile
	additionalFiles.from(tasks.remapSourcesJar.get().archiveFile)
	displayName = "Golden Dandelion Backport ${project.version}"
	version = "${project.version}"
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = STABLE
	modLoaders.addAll("fabric", "quilt")

	val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
	val discordToken = providers.environmentVariable("DISCORD_TOKEN")

	dryRun = modrinthToken.getOrNull() == null || discordToken.getOrNull() == null

	modrinth {
		accessToken = modrinthToken
		projectId = "WBG3OROn"

		minecraftVersionRange {
			start = "${property("min_supported_version")}"
			end = "${property("max_supported_version")}"
		}

		requires {
			// Fabric API
			id = "P7dR8mSH"
		}
	}

	if (stonecutter.current.project == "1.21.9") {
		discord {
			webhookUrl = discordToken

			username = "Golden Dandelion Backport Updates"

			avatarUrl = "https://github.com/PneumonoIsNotAvailable/GoldenDandelionBackport/blob/master/src/main/resources/assets/gdb/icon.png?raw=true"

			content = changelog.map { "# Golden Dandelion Backport version ${project.property("mod_version")}\n<@&1472490332783378472>\n" + it }
		}
	}
}

// configure the maven publication
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	// See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
	repositories {
		// Add repositories to publish to here.
		// Notice: This block does NOT have the same function as the block in the top level.
		// The repositories here will be used for publishing your artifact, not for
		// retrieving dependencies.
	}
}