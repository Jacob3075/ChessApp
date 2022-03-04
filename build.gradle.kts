plugins {
    id("org.springframework.boot") version "2.6.3" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    java
}

java.sourceCompatibility = JavaVersion.VERSION_17

subprojects {
    group = "com.jacob"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains:annotations:22.0.0")
    }
}

listOf(":ui", ":database").forEach {
    project(it) {
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")

        dependencies {
            implementation(project(":chess-engine"))
            implementation("org.springframework.boot:spring-boot-starter")
            testImplementation("org.springframework.boot:spring-boot-starter-test")
        }

        tasks.register("prepareKotlinBuildScriptModel") {}
    }
}
