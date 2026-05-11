package springChatbot.service;

import springChatbot.dto.ChatRequest;
import springChatbot.dto.ChatHistory;
import springChatbot.mapper.ChatHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    // 도커 네트워크 내 서비스 이름인 'llm-api'를 기본값으로 설정합니다.
    @Value("${fastapi.url:http://llm-api:8000}")
    private String fastApiUrl;

    private final RestTemplate restTemplate;
    private final ChatHistoryMapper chatHistoryMapper;

    @Autowired
    public ChatService(ChatHistoryMapper chatHistoryMapper, RestTemplateBuilder restTemplateBuilder) {
        // Spring Boot 3에서는 RestTemplateBuilder를 통한 주입을 권장합니다.
        this.restTemplate = restTemplateBuilder.build();
        this.chatHistoryMapper = chatHistoryMapper;
    }

    /**
     * FastAPI 컨테이너로 메시지를 보내고 응답을 받습니다.
     */
    public String getChatResponse(ChatRequest chatRequest) {
        String url = fastApiUrl + "/chat/message";
        
        try {
            logger.info("FastAPI에 요청을 보냅니다: {}", url);
            ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);
            
            if (chatResponse != null && chatResponse.getResponse() != null) {
                return chatResponse.getResponse();
            }
            return "AI로부터 빈 응답을 받았습니다.";
            
        } catch (Exception e) {
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
        
        // 실제 사용 중인 모델명 'gemma2'로 업데이트
        chatHistory.setModelName("gemma2"); 
        
        chatHistoryMapper.insertChatHistory(chatHistory);
        logger.info("채팅 내역이 저장되었습니다.");
    }

    public List<ChatHistory> getChatHistory() {
        return chatHistoryMapper.selectChatHistory();
    }
}

/**
 * 응답 DTO 클래스 (내부 정적 클래스 또는 별도 파일 권장)
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