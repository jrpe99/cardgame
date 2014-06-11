package dk.game.card.message.request;

import dk.game.card.message.GameMessage;

/**
 *
 * @author jorperss
 */
public class JoinRequest extends GameMessage{
    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
