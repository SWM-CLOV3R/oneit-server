plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

repositories {
    mavenCentral()
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

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.3")

    //QueryDsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    //s3
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

    // slack
    implementation("com.slack.api:slack-api-client:1.40.0")

    // Google Firebase Admin
    implementation("com.google.firebase:firebase-admin:9.2.0")

    // Sentry
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.9.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}