plugins {
    kotlin("jvm") version "1.8.0"
}

group = "kr.hqservice.fish"
version = "1.0.1"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MinseoServer", "MS-Core", "1.0.18")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveFileName.set("${rootProject.name}-${project.version}.jar")
    destinationDirectory.set(File("D:\\서버\\1.19.3 - 개발\\plugins"))
}