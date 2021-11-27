plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.oskarsmc"
System.getenv("GRADLE_RELEASE")
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.bstats:bstats-velocity:2.2.1")
    implementation("com.velocitypowered:velocity-api:3.0.1")
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("cloud.commandframework:cloud-velocity:1.5.0")
    implementation("cloud.commandframework:cloud-minecraft-extras:1.5.0")
    compileOnly("net.luckperms:api:5.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    processResources {
        expand("project" to project)
    }

    shadowJar {
        dependencies {
            include {
                it.moduleGroup == "org.bstats" || it.moduleGroup == "cloud.commandframework" || it.moduleGroup == "io.leangen.geantyref"
            }
            include(dependency("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT"))
        }
        relocate("org.bstats", "com.oskarsmc.message.relocated.bstats")
        relocate("net.kyori.adventure.text.minimessage", "com.oskarsmc.message.relocated.minimessage")
        relocate("cloud.commandframework", "com.oskarsmc.message.relocated.cloud")
        relocate("io.leangen.geantyref", "com.oskarsmc.message.relocated.geantyref")
    }

    build {
        dependsOn(named("shadowJar"))
    }
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Implementation-Title"] = "message"
        attributes["Implementation-Version"] = project.version
        attributes["Implementation-Vendor"] = "OskarsMC"
    }
}