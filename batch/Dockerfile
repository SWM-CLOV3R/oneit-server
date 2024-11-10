# 1. Java 어플리케이션 빌드를 위한 기본 이미지
FROM public.ecr.aws/lambda/java:17 AS build

# 빌드할 JAR 파일 경로 설정
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /oneit/app.jar

# 2. AWS Lambda Web Adapter를 위한 빌드 단계
FROM public.ecr.aws/sam/build-java17:1.116.0-20240430173307 AS artifact-image
WORKDIR "/task"
COPY --from=build /oneit/app.jar app.jar

# 3. AWS Lambda Adapter 포함 및 어플리케이션 실행
FROM public.ecr.aws/docker/library/amazoncorretto:17-al2023-headless

# Lambda Web Adapter 설치
COPY --from=public.ecr.aws/awsguru/aws-lambda-adapter:0.8.3-x86_64 /lambda-adapter /opt/extensions/lambda-adapter

# 환경 변수 설정
ARG ACTIVE_PROFILE
ENV ACTIVE_PROFILE=${ACTIVE_PROFILE}

# Lambda 어플리케이션 경로 설정
WORKDIR /opt
COPY --from=artifact-image /task/app.jar /opt

# Lambda 어플리케이션 실행
CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "/opt/app.jar"]