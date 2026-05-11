# 1. 빌드 스테이지: Gradle 8.11.1과 JDK 17을 사용하여 빌드 수행
FROM gradle:8.11.1-jdk17 AS build
WORKDIR /app

# 빌드 효율을 위해 의존성 관련 파일만 먼저 복사하여 캐싱 활용
COPY build.gradle settings.gradle /app/
# 소스 코드 전체 복사
COPY src /app/src

# 테스트를 제외하고 실행 가능한 WAR 파일 생성 (bootWar 사용)
RUN gradle bootWar --no-daemon -x test

# 2. 실행 스테이지: 가벼운 JRE 환경에서 애플리케이션 실행
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 빌드 스테이지에서 생성된 WAR 파일만 추출하여 복사
# Gradle은 기본적으로 build/libs 폴더에 결과물을 생성합니다.
COPY --from=build /app/build/libs/*.war app.war

# 컨테이너 내부에서 사용할 포트 설정 (application.properties의 server.port=8087과 일치)
EXPOSE 8087

# 컨테이너 실행 시 Spring Boot 앱 구동
# WAR 파일이지만 내장 톰캣을 포함하고 있어 java -jar로 실행 가능합니다.
ENTRYPOINT ["java", "-jar", "app.war"]