package ralmnsk.webrtcvideo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        super.handleTextMessage(session, message);
        System.out.println((message.getPayload()));
        for(WebSocketSession wsSession:sessions){
            if(wsSession.isOpen()&&!session.getId().equals(wsSession.getId())){
                wsSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
//        if(sessions.size()>1){ //when client gets event:start he begins send offer
//            session.sendMessage(new TextMessage("{\"event\": \"start\",\"data\":\"start\"}"));
//        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(sessions.contains(session)){
            sessions.remove(session);
        }
    }
}
