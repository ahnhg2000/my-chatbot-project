from pydantic import BaseModel   # pydantic : 데이터 유효성 검사 및 설정을 위한 파이썬 라이브러리

class ChatRequest(BaseModel):
    message: str  # ChatRequest 모델의 필드를 정의 (str 문자열)

class ChatResponse(BaseModel):
    response: str  # ChatResponse 모델의 필드를 정의 (str 문자열)