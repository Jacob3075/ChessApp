plugins {
    id("io.freefair.lombok") version "6.4.1"
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
