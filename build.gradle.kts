plugins {
    id("org.springframework.boot") version ("2.6.3")
    id("io.spring.dependency-management") version ("1.0.11.RELEASE")
    java
}

group = "com.jacob"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":database"))
    implementation(project(":chess-engine"))
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
