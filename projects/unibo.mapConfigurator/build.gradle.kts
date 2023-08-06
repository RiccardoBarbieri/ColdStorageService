plugins {
	id ("java")
	id ("org.springframework.boot") version "2.7.8"
	id ("io.spring.dependency-management") version "1.1.2"
	id ("application")
}
val customFlatDir: Configuration by configurations.creating

group = "unibo"
version = "1.0"

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

// Task to print out the group and version of each jar in the flat directory
tasks.register("printJarInfo") {
	doLast {
		customFlatDir.incoming.resolutionResult.allComponents.forEach { component ->
			val moduleVersion = component.moduleVersion
			println(moduleVersion)
		}
	}
}

//mainClassName = "unibo.mapConfigurator.MapConfiguratorApplication"

//jar {
//	println("executing jar")
//	from sourceSets.main.allSource
//	manifest {
//		attributes "Main-Class": "$mainClassName"
//	}
//}
abstract class Dockerize : DefaultTask() {
	@TaskAction
	fun greet() {
		println("hello from GreetingTask")
	}

}

tasks.register<Dockerize>("dockerize") {
	group = "docker"
	description = "Dockerize the application"
	doLast {
		println("Dockerizing the application")
	}
}



tasks.test {
	useJUnitPlatform()
}