package dk.game.card.user;

import dk.java8.game.card.Hand;

/**
 *
 * @author jorperss
 */
public class GameUser {
    private String gameId;
    private Hand hand;

    public GameUser(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
