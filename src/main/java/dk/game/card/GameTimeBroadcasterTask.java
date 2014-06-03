package dk.game.card;

import dk.game.card.message.response.GameTimeBroadcastMessage;
import dk.game.card.util.WebSocketHelper;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;
import javax.websocket.Session;

public class GameTimeBroadcasterTask extends TimerTask {

    private CardGame owner;
    private LocalTime time = LocalTime.of(0, 0);

    public GameTimeBroadcasterTask(CardGame owner) {
        this.owner = owner;
    }

    @Override
    public void run() {
        if (!owner.getRemoteClients().isEmpty()) {
            time = time.plusSeconds(1);
            GameTimeBroadcastMessage gameTimeBroadcastMessage = new GameTimeBroadcastMessage();
            gameTimeBroadcastMessage.setTime(time.format(DateTimeFormatter.ISO_LOCAL_TIME));
            for (Session session : owner.getRemoteClients()) {
                WebSocketHelper.send(session, gameTimeBroadcastMessage);
            }
        }
    }
}
