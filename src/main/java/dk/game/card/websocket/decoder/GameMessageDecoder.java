package dk.game.card.websocket.decoder;

import dk.game.card.message.GameMessage;
import dk.game.card.message.request.DealRequest;
import dk.game.card.message.request.GameStartRequest;
import dk.game.card.message.request.GameStopRequest;
import dk.game.card.message.request.JoinRequest;
import dk.game.card.message.request.LoginRequestMessage;
import dk.game.card.message.request.PlayCardRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class GameMessageDecoder implements Decoder.Text<GameMessage> {

    @Override
    public GameMessage decode(String s) {
        System.out.println(s);
        ObjectMapper mapper = new ObjectMapper();
        GameMessage msg = null;
        try {
            if (s.contains(GameMessage.LOGIN_REQUEST)) {
                msg = mapper.readValue(s, LoginRequestMessage.class);
            } else if (s.contains(GameMessage.GAME_START_REQUEST)) {
                msg = mapper.readValue(s, GameStartRequest.class);
            } else if (s.contains(GameMessage.GAME_STOP_REQUEST)) {
                msg = mapper.readValue(s, GameStopRequest.class);
            } else if (s.contains(GameMessage.DEAL_REQUEST)) {
                msg = mapper.readValue(s, DealRequest.class);
            } else if (s.contains(GameMessage.JOIN_REQUEST)) {
                msg = mapper.readValue(s, JoinRequest.class);
            } else if (s.contains(GameMessage.PLAY_CARD_REQUEST)) {
                msg = mapper.readValue(s, PlayCardRequest.class);
            }
        } catch (IOException ex) {
            Logger.getLogger(GameMessageDecoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
        /*        return s.startsWith(CardGameMessage.DEAL_REQUEST) ||
         s.startsWith(CardGameMessage.LOGIN_REQUEST) ||
         s.startsWith(CardGameMessage.LOGOUT_REQUEST);*/    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
