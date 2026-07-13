# Python 3.12-slim 이미지 사용
FROM python:3.12-slim

# 시스템 라이브러리 업데이트 및 필수 도구 설치 (필요시)
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 의존성 파일 복사 및 설치
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# 프로젝트 전체 소스 복사
COPY . .

# 컨테이너 실행 포트 설정
EXPOSE 8000

# uvicorn 실행 (app.main:app은 프로젝트 진입점에 따라 수정 가능)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]