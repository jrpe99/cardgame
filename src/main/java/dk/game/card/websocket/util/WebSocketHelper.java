package dk.game.card.websocket.util;

import dk.game.card.CardGame;
import dk.game.card.message.GameMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;

/**
 *
 * @author jorperss
 */
public class WebSocketHelper {
    public static void send(Session session, GameMessage msg) {
        try {
            String json = msg.toJson();
            System.out.println("Send to session : " + session.getId());
            System.out.println("Message : \n" + json);
            session.getBasicRemote().sendText(json);
        } catch (IOException ex) {
            Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
