package springChatbot.service;

import springChatbot.dto.ChatRequest;
import springChatbot.dto.ChatHistory;
import springChatbot.mapper.ChatHistoryMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    // 도커 네트워크 내 서비스 이름인 'llm-api'를 기본값으로 설정합니다.
    @Value("${fastapi.url}")
    private String fastApiUrl;

    private final RestTemplate restTemplate;
    private final ChatHistoryMapper chatHistoryMapper;

    public ChatService(ChatHistoryMapper chatHistoryMapper, RestTemplateBuilder restTemplateBuilder) {
        /* * [주요 수정 사항]
         * CPU 환경에서 Ollama의 응답이 1분 이상 걸리는 문제를 해결하기 위해
         * RestTemplate의 연결(Connect) 및 읽기(Read) 타임아웃을 300초(5분)로 설정합니다.
         */
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(300))
                .setReadTimeout(Duration.ofSeconds(300))
                .build();
        this.chatHistoryMapper = chatHistoryMapper;
    }

    /**
     * FastAPI 컨테이너로 메시지를 보내고 응답을 받습니다.
     */
    public String getChatResponse(ChatRequest chatRequest) {
        String url = fastApiUrl + "/chat/message";
        
        try {
            logger.info("FastAPI에 요청을 보냅니다: {}", url);
            // JSON 응답을 ChatResponse 객체로 매핑하여 받아옵니다.
            ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);
            
            if (chatResponse != null && chatResponse.getResponse() != null) {
                return chatResponse.getResponse();
            }
            return "AI로부터 빈 응답을 받았습니다.";
            
        } catch (Exception e) {
            // 타임아웃이나 통신 단절 시 로그를 남깁니다.
            logger.error("FastAPI 통신 중 오류 발생: {}", e.getMessage());
            return "서버 통신 오류가 발생했습니다: " + e.getMessage();
        }
    }

    /**
     * 채팅 내역을 MariaDB에 저장합니다.
     */
    public void saveChatHistory(String userMessage, String botResponse) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setUserMessage(userMessage);
        chatHistory.setBotResponse(botResponse);
        
        // 실제 사용 중인 모델명 'gemma2'를 기록합니다.
        chatHistory.setModelName("gemma2"); 
        
        chatHistoryMapper.insertChatHistory(chatHistory);
        logger.info("채팅 내역이 MariaDB에 저장되었습니다.");
    }

    public List<ChatHistory> getChatHistory() {
        return chatHistoryMapper.selectChatHistory();
    }
}

/**
 * 응답 DTO 클래스
 * FastAPI가 반환하는 { "response": "..." } 형태의 JSON을 담는 바구니 역할을 합니다.
 */
class ChatResponse {
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}