package dk.game.card.message.response;

import dk.game.card.message.GameMessage;

/**
 *
 * @author jorperss
 */
public class GameTimeBroadcastMessage extends GameMessage {
    private String time;

    public GameTimeBroadcastMessage() {
        this.setType(GAME_TIME_RESPONSE);
    }
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
