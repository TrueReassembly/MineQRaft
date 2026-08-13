plugins {
    id("java")
}

group = "dev.reassembly"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    implementation("com.github.kenglxn.QRGen:javase:3.0.1")
}

tasks.test {
    useJUnitPlatform()
}