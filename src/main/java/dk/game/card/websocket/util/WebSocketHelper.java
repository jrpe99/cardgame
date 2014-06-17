package dk.game.card.websocket.util;

import dk.game.card.CardGame;
import dk.game.card.message.GameMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author jorperss
 */
public class WebSocketHelper {
    public static void send(Session session, GameMessage msg) {
        try {
            System.out.println("Send to session : " + session.getId());
            session.getBasicRemote().sendObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncodeException ex) {
            Logger.getLogger(WebSocketHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
