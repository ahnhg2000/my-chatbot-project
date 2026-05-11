import requests
import os

class OllamaService:
    def __init__(self, model="gemma2"):
        """
        Ollama 서비스 초기화
        :param model: 사용할 LLM 모델명 (기본값: gemma2)
        """
        # 도커 컨테이너에서 호스트 PC 접속을 위한 주소 설정
        # 환경 변수 OLLAMA_HOST가 없으면 host.docker.internal을 기본값으로 사용합니다.
        self.host = os.getenv("OLLAMA_HOST", "host.docker.internal")
        self.port = os.getenv("OLLAMA_PORT", "11434")
        self.base_url = f"http://{self.host}:{self.port}/api/generate"
        self.model = model

    def generate_response(self, prompt: str) -> str:
        """
        Ollama API를 호출하여 모델의 응답을 생성합니다.
        """
        payload = {
            "model": self.model,
            "prompt": prompt,
            "stream": False  # 결과값을 한 번에 받기 위해 False 설정
        }

        try:
            # 타임아웃을 설정하여 무한 대기를 방지합니다 (예: 60초)
            response = requests.post(self.base_url, json=payload, timeout=60)
            
            # HTTP 상태 코드가 200이 아니면 예외를 발생시킵니다.
            response.raise_for_status()
            
            # Ollama 응답 JSON에서 텍스트 결과만 추출하여 반환합니다.
            result = response.json()
            return result.get('response', '응답을 생성할 수 없습니다.')

        except requests.exceptions.ConnectionError:
            raise Exception(f"Ollama 서버({self.base_url})에 연결할 수 없습니다. 호스트 설정을 확인하세요.")
        except requests.exceptions.Timeout:
            raise Exception("Ollama 응답 시간이 초과되었습니다.")
        except Exception as e:
            raise Exception(f"Ollama 서비스 오류: {str(e)}")