plugins {
	id("java")
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	id("com.bmuschko.docker-spring-boot-application") version "9.3.2"
}

group = "unibo"
version = "0.0.1-SNAPSHOT"

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

}

tasks.test {
	useJUnitPlatform()
}
