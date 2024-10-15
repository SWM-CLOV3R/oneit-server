plugins {
    java
    id("org.springframework.boot") version "3.3.0" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
}

group = "clov3r"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        // database
        implementation("com.mysql:mysql-connector-j")       // from mysql ver 8.0.31
        runtimeOnly("com.mysql:mysql-connector-j")
        implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")

        // spring
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        // test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    }

    //tasks.withType<Test> {
    //    useJUnitPlatform()
    //}

}
