import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.1.2"
	id("com.github.johnrengelman.shadow") version "8.1.1"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}


group = "space.keyme"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
extra["springCloudFunctionVersion"] = "3.2.5" // "2021.0.3"


repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-starter-function-web:3.2.5")
	implementation("org.springframework.cloud:spring-cloud-function-kotlin:3.2.5")
	implementation("org.springframework.cloud:spring-cloud-function-adapter-aws:3.2.5")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.8")
	implementation("com.amazonaws:aws-lambda-java-events:3.11.2")
	implementation("com.amazonaws:aws-lambda-java-core:1.2.2")
	runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-function-dependencies:${property("springCloudFunctionVersion")}")
	}
}

tasks.jar {
	manifest {
		attributes["Start-Class"] = "space.keyme.keymeserverless.KeymeServerlessApplication"
	}
}

tasks.assemble {
	dependsOn("shadowJar")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
	archiveFileName.set("keyme-serverless-lambda.jar")
	dependencies {
		exclude("org.springframework.cloud:spring-cloud-function-web")
	}
	mergeServiceFiles()
	append("META-INF/spring.handlers")
	append("META-INF/spring.schemas")
	append("META-INF/spring.tooling")
	transform(com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer::class.java) {
		paths.add("META-INF/spring.factories")
		mergeStrategy = "append"
	}
}