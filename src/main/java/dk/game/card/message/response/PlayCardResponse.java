package dk.game.card.message.response;

import dk.game.card.message.GameMessage;

/**
 *
 * @author jorperss
 */
public class PlayCardResponse extends GameMessage{
    private String card;
    private String gameId;

    public PlayCardResponse(String gameId, String card) {
        this.setType(PLAY_CARD_RESPONSE);
        this.gameId = gameId;
        this.card = card;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
