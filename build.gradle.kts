import org.gradle.internal.jvm.Jvm
import java.util.Date
import java.text.SimpleDateFormat
import java.util.TimeZone

plugins {
    id("net.minecraftforge.gradle") version "5.1.+"
}

val mcVersion: String by project
val forgeVersion: String by project
val modVersion: String by project
val archiveBase: String by project

val libVulpesVersion: String by project
val libVulpesBuildNum: String by project
val jeiVersion: String by project
val icVersion: String by project
val gcVersion: String by project

val startGitRev: String by project

group = "zmaster587.advancedRocketry"
setProperty("archivesBaseName", archiveBase)

val buildNumber: String by lazy { System.getenv("BUILD_NUMBER") ?: getDate() }

fun getDate(): String {
    val format = SimpleDateFormat("HH-mm-dd-MM-yyyy")
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date())
}

version = "$mcVersion-$modVersion-$buildNumber"

println("$archiveBase v$version")

//sourceCompatibility = targetCompatibility = '1.8' // Need this here so eclipse task generates correctly.
tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}


minecraft {
    mappings("snapshot", "20170624-1.12")

    accessTransformer(file("src/main/resources/META-INF/accessTransformer.cfg"))

    runs {
        create("client") {
            properties(
                mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP,COREMODLOG",
                    "forge.logging.console.level" to "info"
                )
            )

            workingDirectory = file("run").canonicalPath

            mods {
                create("advancedrocketry") {
                    source(sourceSets["main"])
                }
            }
        }
        create("server") {
            properties(
                mapOf(
                    "forge.logging.markers" to "SCAN,REGISTRIES,REGISTRYDUMP,COREMODLOG",
                    "forge.logging.console.level" to "info"//, "fml.coreMods.load" to "com.gramdatis.core.setup.GramdatisPlugin"
                )
            )
            arg("nogui")

            workingDirectory = file("run-server").canonicalPath

            mods {
                create("advancedrocketry") {
                    source(sourceSets["main"])
                }
            }
        }
    }
}

repositories {
    mavenCentral()
    maven {
        name = "mezz.jei"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    ivy {
        name = "industrialcraft-2"
        artifactPattern("http://jenkins.ic2.player.to/job/IC2_111/39/artifact/build/libs/[module]-[revision].[ext]")
    }
    maven {
        // location of a maven mirror for JEI files, as a fallback
        name = "ModMaven"
        url = uri("https://modmaven.k-4u.nl")
    }
    maven {
        name = "Galacticraft"
        url = uri("https://maven.galacticraft.net/repository/legacy/")
    }
    maven {
        name = "LibVulpes"
        url = uri("http://maven.dmodoomsirius.me/")
        isAllowInsecureProtocol = true
    }
    flatDir {
        dirs("libs")
    }
}

dependencies {
    minecraft(group = "net.minecraftforge", name = "forge", version = "$mcVersion-$forgeVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.5")
    compileOnly("net.industrial-craft:industrialcraft-2:$icVersion:dev")
    //implementation("zmaster587.libVulpes:LibVulpes:$mcVersion-$libVulpesVersion-$libVulpesBuildNum-deobf")

    //compileOnly("micdoodle8.mods:galacticraft-api:$gcVersion")
    //compileOnly("micdoodle8.mods:galacticraft-core:$gcVersion")
    //compileOnly("micdoodle8.mods:galacticraft-planets:$gcVersion")
    //compileOnly("micdoodle8.mods:micdoodlecore:$gcVersion")

    compileOnly(fg.deobf("mezz.jei:jei_${mcVersion}:${jeiVersion}:api"))
    runtimeOnly(fg.deobf("mezz.jei:jei_${mcVersion}:${jeiVersion}"))
    implementation ("zmaster587.libVulpes:libVulpes:1.12.2-0.4.2+:deobf")
}

tasks.processResources {
    //includeEmptyDirs = false
    inputs.properties(
        "advRocketryVersion" to project.version,
        "mcVersion" to mcVersion,
        "libVulpesVersion" to libVulpesVersion
    )

    filesMatching("mcmod.info") {
        expand(
            "advRocketryVersion" to project.version,
            "mcVersion" to mcVersion,
            "libVulpesVersion" to libVulpesVersion
        )
    }

    exclude("**/*.sh")
}

val currentJvm: String = Jvm.current().toString()
println("Current Java version: $currentJvm")



val deobfJar by tasks.registering(Jar::class) {
    from(sourceSets["main"].output)
    classifier = "deobf"
}

tasks.build {
    dependsOn(deobfJar)
}

