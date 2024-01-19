/*
================================================================================
build22.gradle
GENERATED val ONCE: ONLY = ===============================================================================
*/
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import java.util.*


plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    id("application")
    id("java")
    id("eclipse")
//    id("com.bmuschko.docker-spring-boot-application") version "9.3.2"
    id("com.bmuschko.docker-java-application") version "9.4.0"

    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.6.0"
//    id("org.jetbrains.kotlin.jvm")
}

version = "4.1.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    flatDir { dirs("../unibolibs") }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform(kotlin("bom")))

    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib"))

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))

    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))

    /* COROUTINES ********************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation(
        group = "org.jetbrains.kotlinx",
        name = "kotlinx-coroutines-core",
        version = project.properties["kotlinVersion"] as String? ?: throw GradleException("kotlinVersion not found"),
        ext = "pom"
    )
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm
    implementation(
        group = "org.jetbrains.kotlinx",
        name = "kotlinx-coroutines-core-jvm",
        version = project.properties["kotlinVersion"] as String? ?: throw GradleException("kotlinVersion not found")
    )

    //SOCKET.IO (for WEnv)
    implementation(group = "javax.websocket", name = "javax.websocket-api", version = "1.1")
    //javax.websocket api(is only the specification)
    implementation(group = "org.glassfish.tyrus.bundles", name = "tyrus-standalone-client", version = "1.9")

    //HTTP
    implementation("org.apache.httpcomponents:httpclient:4.5")

    /*  MQTT *************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3
    implementation(group = "org.eclipse.paho", name = "org.eclipse.paho.client.mqttv3", version = "1.2.5")

    /* JSON **************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.json/json
    //implementation(group = "org.json", name = "json", version = "20220320")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    /* COAP **************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
    implementation(group = "org.eclipse.californium", name = "californium-core", version = "3.5.0")
    // https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy2
    implementation(group = "org.eclipse.californium", name = "californium-proxy2", version = "3.5.0")

//OkHttp library for websockets with Kotlin
    //implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "3.14.0")
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.3")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/mockwebserver
    testImplementation(group = "com.squareup.okhttp3", name = "mockwebserver", version = "4.9.3")

    /* LOG4J *************************************************************************************************************** */
    //implementation(group = "org.slf4j", name = "slf4j-reload4j", version = "2.0.0-alpha7")
    //implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.25")

// https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    testImplementation(group = "org.slf4j", name = "slf4j-simple", version = "2.0.7")

    /* HTTP **************************************************************************************************************** */
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    implementation(group = "org.apache.httpcomponents", name = "httpclient", version = "4.5.13")
    // https://mvnrepository.com/artifact/commons-io/commons-io
    implementation(group = "commons-io", name = "commons-io", version = "2.11.0")

    /* UNIBO *************************************************************************************************************** */
    implementation("unibo:uniboInterfaces")
    implementation("unibo:2p301")
    implementation("unibo:unibo.qakactor23-4.0")
    implementation("unibo:unibo.basicomm23-1.0")
    implementation("unibo:landmarks")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")


    /* AIMA **************************************************************************************************************** */
    //PLANNER
    implementation("unibo:unibo.planner23-1.0")
    // https://mvnrepository.com/artifact/com.googlecode.aima-java/aima-core
    implementation("com.googlecode.aima-java:aima-core:3.0.0")

}

sourceSets {
    main {
        java {
            srcDirs("src", "resources", "test")
        }
    }
}


eclipse {
    classpath {
        sourceSets.minus(listOf("main", "test"))
    }
}

application {
    mainClass.set("it.unibo.ctx_coldstorageservice.MainCtx_coldstorageserviceKt")
}

tasks.jar {
    doLast {
        println("building jar")
    }
    from(sourceSets.main.get().output)
    manifest {
        attributes["Main-Class"] = "it.unibo.ctx_coldstorageservice.MainCtx_coldstorageserviceKt"
    }
}


tasks.register<Dockerfile>("createDockerfile") {
    mustRunAfter("distTar")
    dependsOn("distTar")
    group = "unibobootdocker"
    description = "Create Dockerfile"

    doFirst {
        val inputTarFile =
            project.layout.projectDirectory.file("build/distributions/" + project.name + "-" + project.version + ".tar")

        from("openjdk:11")
        exposePort(8021)
        exposePort(8020)
        volume("/data")
        addFile("./build/distributions/${inputTarFile.asFile.name}", "/")
        workingDir(inputTarFile.asFile.name.removeSuffix(".tar") + "/bin")
        copyFile("./*.pl", "./")
        copyFile("./*.json", "./")
        copyFile("./*.bin", "./")
        copyFile("./*.txt", "./")
        defaultCommand("bash", "./" + project.name)
    }
}

tasks.register<DockerBuildImage>("buildImage") {
    dependsOn("createDockerfile")
    group = "unibobootdocker"
    description = "Dockerize the spring boot application"
    val dockerRepository = properties["dockerRepository"] ?: throw GradleException("dockerRepository property not set")
    dockerFile.set(file(layout.projectDirectory.toString() + "/build/docker/Dockerfile"))
    inputDir.set(file(layout.projectDirectory))
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":latest")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":${project.version}")
}

tasks.register<DockerPushImage>("pushImage") {
    dependsOn("buildImage")
    group = "unibobootdocker"
    description = "Push the docker image to the repository"
    val dockerRepository = properties["dockerRepository"] ?: throw GradleException("dockerRepository property not set")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":latest")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":${project.version}")
}
