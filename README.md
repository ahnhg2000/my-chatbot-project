# My Chatbot Project

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white)
![Ollama](https://img.shields.io/badge/Ollama-000000?style=for-the-badge&logo=ollama&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)

## Java + Python 하이브리드 AI 챗봇 프로젝트

Spring Boot(JSP)와 FastAPI(Ollama)를 결합한 풀스택 AI 챗봇 시스템입니다.

---

## 📋 목차

- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [빠른 시작](#빠른-시작)
- [Docker Compose 배포](#docker-compose-배포)
- [CI/CD](#cicd-jenkins)
- [환경 변수](#환경-변수)

---

## 주요 기능

- Ollama(Gemma2) 기반 **지능형 AI 챗봇**
- Spring Boot + JSP 프론트엔드
- FastAPI 기반 LLM 서비스 분리
- MariaDB 데이터베이스 연동
- Docker 컨테이너 기반 배포
- Jenkins CI/CD 자동화 파이프라인 구성

---

## 기술 스택

| 계층 | 기술 |
|---|---|
| Frontend | JSP, HTML, CSS |
| Backend | Spring Boot 3.2 (Java 17), FastAPI |
| AI / LLM | Ollama + Gemma2 |
| Database | MariaDB 10.6 |
| Deployment | Docker, docker-compose |
| CI/CD | Jenkins |
| 기타 | MyBatis, Lombok, Gradle |

---

## 프로젝트 구조

```bash
my-chatbot-project/
├── backend-java/              # Spring Boot + JSP 메인 애플리케이션
│   ├── src/main/webapp/
│   ├── build.gradle
│   └── Dockerfile
│
├── backend-python/            # FastAPI LLM 서비스
│   ├── app/
│   │   ├── routers/
│   │   └── services/
│   │       └── ollama_service.py
│   ├── main.py
│   ├── requirements.txt
│   └── Dockerfile
│
├── docker-compose.yml
├── Jenkinsfile
├── .gitignore
└── .env
```

---

# 빠른 시작

## 1. 저장소 클론

```bash
git clone https://github.com/ahnhg2000/my-chatbot-project.git

cd my-chatbot-project
```

---

## 2. 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성합니다.

```env
DB_ROOT_PASSWORD=your_strong_password
DB_NAME=chatbot_db
DB_USERNAME=root
```

---

## 3. Docker 실행

### 빌드 및 실행

```bash
docker-compose up --build -d
```

### 로그 확인

```bash
docker-compose logs -f
```

---

## 접속 주소

| 서비스 | URL |
|---|---|
| 메인 웹(Spring Boot) | http://localhost:8087 |
| FastAPI API | http://localhost:8000 |

---

# Docker Compose 배포

## 컨테이너 실행

```bash
docker-compose up --build -d
```

## 컨테이너 중지

```bash
docker-compose down
```

## 로그 확인

```bash
docker-compose logs -f
```

---

# CI/CD (Jenkins)

GitHub Repository 변경 시 Jenkins Pipeline을 통해 자동 배포됩니다.

## Jenkins Pipeline 과정

```
GitHub Push
      ↓
Jenkins Trigger
      ↓
기존 Container 정리
      ↓
Docker Image Build
      ↓
Container 실행
      ↓
Slack Notification
```

---

# 환경 변수 (.env)

| 변수명 | 설명 |
|---|---|
| DB_ROOT_PASSWORD | MariaDB Root 비밀번호 |
| DB_NAME | 데이터베이스 이름 |
| DB_USERNAME | 데이터베이스 사용자명 |

---

## Architecture

```
              User
               |
               ▼
        Spring Boot + JSP
               |
               ▼
          FastAPI API
               |
               ▼
        Ollama + Gemma2
               |
               ▼
            MariaDB
```

