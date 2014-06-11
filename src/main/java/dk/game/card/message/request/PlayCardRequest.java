package dk.game.card.message.request;

import dk.game.card.message.GameMessage;

/**
 *
 * @author jorperss
 */
public class PlayCardRequest extends GameMessage{
    private String card;

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
