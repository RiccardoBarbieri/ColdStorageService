import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile

plugins {
    id("java")
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.bmuschko.docker-spring-boot-application") version "6.1.4"

    id("application")
}

group = "unibo"
version = "2.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
    flatDir {
        dirs("../unibolibs")
    }
}

configurations {
    runtimeClasspath {
        extendsFrom(configurations["developmentOnly"])
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    //implementation("jakarta.annotation:jakarta.annotation-api:2.0.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")


    //kotlin runtime
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")

    //websocket NOSTOMP
    implementation("org.springframework:spring-websocket:5.3.14")

    //webjars
    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars:bootstrap:5.1.3")
    implementation("org.webjars:jquery:3.6.0")

    //JSON
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    //COAP
    implementation("org.eclipse.californium:californium-core:3.5.0")
    implementation("org.eclipse.californium:californium-proxy2:3.5.0")

    //UNIBO
    implementation("unibo:uniboInterfaces:1.0")
    implementation("unibo:2p301:1.0")
    implementation("unibo:unibo.qakactor23-3.5:1.0")
    implementation("unibo:unibo.basicomm23-1.0:1.0")
    implementation("unibo:unibo.planner23-1.0:1.0")

    //AIMA
    implementation("com.googlecode.aima-java:aima-core:3.0.0")

}

tasks.register<Dockerfile>("createDockerfile") {
    dependsOn("bootDistTar")
    val fileRegex: Regex = Regex("unibo.mapConfigurator-boot(.*)\\.tar")
    val inputDir: Directory = layout.projectDirectory.dir("build/distributions")
    group = "docker"
    description = "Create Dockerfile"
    val lastModified = inputDir.asFileTree.files.filter {
        it.name.matches(fileRegex)
    }.maxByOrNull { it.lastModified() }

    from("openjdk:11")
    exposePort(8015)
    volume("/data")
    addFile("./build/distributions/" + lastModified!!.name, "/")
    workingDir(lastModified.name.removeSuffix(".tar") + "/bin")
    defaultCommand("bash" ,"./" + lastModified.name.split("-")[0])
}

tasks.register<DockerBuildImage>("dockerize") {
    dependsOn("createDockerfile")
    group = "docker"
    description = "Dockerize the spring boot application"
    inputDir.set(layout.projectDirectory.dir("build/docker"))
    images.add("mapconfigurator:latest")
}



tasks.test {
    useJUnitPlatform()
}