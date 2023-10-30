import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import java.util.Properties


plugins {
    id("java")
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.1.2"
    id("com.bmuschko.docker-spring-boot-application") version "9.3.4"
    id("application")
}

group = "unibo"
version = "2.3"

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
    implementation("unibo:landmarks")

    //AIMA
    implementation("com.googlecode.aima-java:aima-core:3.0.0")


}

val springProps = Properties()

properties["activeProfile"]?.let {
    println("Loading properties from application-$it.properties")
    springProps.load(file("src/main/resources/application-$it.properties").inputStream())
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    systemProperty("spring.profiles.active", properties["activeProfile"] ?: "dev")
}

tasks.register<Dockerfile>("createDockerfile") {
    dependsOn("bootDistTar")
    group = "unibobootdocker"
    description = "Create Dockerfile"

    val fileRegex = Regex(".*-boot-(.*)\\.tar")
    val inputDir: Directory = project.layout.projectDirectory.dir("build/distributions")
    val lastModified = inputDir.asFileTree.files.filter {
        it.name.matches(fileRegex)
    }.maxByOrNull { it.lastModified() }

    //nessuna distribuzione disponibile
    if (lastModified == null) {
        println("No file found")
        return@register
    }
    //controllo che file scelto sia della versione corrente
    if (fileRegex.matchEntire(lastModified.name)?.groupValues?.get(1)?.contains(project.version.toString()) == false) {
        println("Mismatched version, check distribution files")
        return@register
    }

    from("openjdk:11")
    exposePort(springProps["server.port"].toString().toInt())
    volume("/data")
    addFile("./build/distributions/" + lastModified.name, "/")
    workingDir(lastModified.name.removeSuffix(".tar") + "/bin")
    defaultCommand("bash", "./" + lastModified.name.removeSuffix(".tar"))
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
    val dockerRepository = properties["dockerRepository"] ?: "riccardoob"
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":latest")
    images.add("${dockerRepository}/" + project.name.split(".").last().lowercase() + ":${project.version}")
}






tasks.test {
    useJUnitPlatform()
}