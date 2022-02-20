plugins {
    id("org.openjfx.javafxplugin") version "0.0.10"
}

javafx {
    version = "17"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":database"))
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
