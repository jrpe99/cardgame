package dk.game.card.websocket.endpoint;

import dk.game.card.CardGame;
import dk.game.card.websocket.decoder.GameMessageDecoder;
import dk.game.card.websocket.encoder.GameMessageEncoder;
import dk.game.card.message.GameMessage;
import dk.game.card.message.request.DealRequest;
import dk.game.card.message.request.GameStartRequest;
import dk.game.card.message.request.GameStopRequest;
import dk.game.card.message.request.LoginRequestMessage;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/play",
        decoders = {GameMessageDecoder.class},
        encoders = {GameMessageEncoder.class}
)
public class GameServer {

    public static CardGame game = new CardGame();
    
    @OnClose
    public void handleClosedConnection(Session session, CloseReason reason) {
        game.removeSession(session);
    }

    @OnMessage
    public void handleMessage(GameMessage message, Session session){
        if(message instanceof LoginRequestMessage) {
            game.handleLoginRequest((LoginRequestMessage)message, session);
        } else if(message instanceof GameStartRequest) {
            game.switchStateToGameStarted();
        } else if(message instanceof GameStopRequest) {
            game.switchStateToGameFinished();
        } else if(message instanceof DealRequest) {
            game.handleDealRequest(message, session);
        }
    }
}