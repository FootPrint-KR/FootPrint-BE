# === Build Stage ===
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Gradle 파일 먼저 복사 (캐시 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 의존성 다운로드 (레이어 캐싱)
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 소스 복사 후 빌드
COPY src src
RUN ./gradlew bootJar --no-daemon

# === Run Stage ===
FROM eclipse-temurin:21-jre
WORKDIR /app

# JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
