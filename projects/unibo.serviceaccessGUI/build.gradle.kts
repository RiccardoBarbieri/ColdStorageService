import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import java.util.*

plugins {
	id("java")
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("com.bmuschko.docker-spring-boot-application") version "9.3.2"
    id("application")
}

group = "unibo"
version = "3.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
	mavenCentral()
	flatDir {
		dirs("../unibolibs")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//Added for WebSocket
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	//JSON
	implementation("com.googlecode.json-simple:json-simple:1.1.1")
	//CUSTOM unibo
	implementation("unibo:unibo.basicomm23-1.0:1.0")


	/* COAP **************************************************************************************************************** */
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
	implementation("org.eclipse.californium:californium-core:3.5.0")
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy2
	implementation("org.eclipse.californium:californium-proxy2:3.5.0")

    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("org.apache.pdfbox:pdfbox:3.0.0")

}

val springProps = Properties()

properties["activeProfile"]?.let {
    println("Loading properties from application-$it.properties")
    springProps.load(file("src/main/resources/application-$it.properties").inputStream())
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    systemProperty("spring.profiles.active", properties["activeProfile"] ?: "dev")
}

tasks.register<Copy>("propcopy") {
    dependsOn("processResources")
    group = "help"
    description = "Copy properties file to resources"
    val activeProfile = properties["activeProfile"] ?: "dev"
    from("src/main/resources/application-$activeProfile.properties")
    into("src/main/resources/")
    rename("application-$activeProfile.properties", "application.properties")
}

tasks.register<Dockerfile>("createDockerfile") {
    mustRunAfter("propcopy")
    mustRunAfter("bootDistTar")
    dependsOn("bootDistTar")
    dependsOn("propcopy")
    group = "unibobootdocker"
    description = "Create Dockerfile"

    doFirst {
        val fileRegex = Regex(".*-boot-(.*)\\.tar")
        val inputDir: Directory = project.layout.projectDirectory.dir("build/distributions")
        val lastModified = inputDir.asFileTree.files.filter {
            it.name.matches(fileRegex)
        }.maxByOrNull { it.lastModified() }

        //nessuna distribuzione disponibile
        if (lastModified == null) {
            println("No file found")
            return@doFirst
        }
        //controllo che file scelto sia della versione corrente
        if (fileRegex.matchEntire(lastModified.name)?.groupValues?.get(1)?.contains(project.version.toString()) == false) {
            println("Mismatched version, check distribution files")
            return@doFirst
        }

        from("openjdk:11")
        exposePort(springProps["server.port"].toString().toInt())
        volume("/data")
        addFile("./build/distributions/" + lastModified.name, "/")
        workingDir(lastModified.name.removeSuffix(".tar") + "/bin")
        defaultCommand("bash", "./" + project.name)
    }
}

tasks.register<DockerBuildImage>("buildImage") {
    dependsOn("createDockerfile")
    mustRunAfter("createDockerfile")
    group = "unibobootdocker"
    description = "Dockerize the spring boot application"

    dockerFile.set(file(layout.projectDirectory.toString() + "/build/docker/Dockerfile"))
    inputDir.set(file(layout.projectDirectory))
    val dockerRepository = properties["dockerRepository"] ?: throw GradleException("dockerRepository property not set")
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


tasks.test {
	useJUnitPlatform()
}
