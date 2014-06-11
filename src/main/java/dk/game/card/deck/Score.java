package dk.game.card.deck;

public enum Score {

    NO_SCORE(0), PAIR(1), TO_PAIR(2), THREE_OF_A_KIND(3), STRAIGHT(4), FLUSH(5), FULL_HOUSE(6), FOUR_OF_A_KIND(7), STRAIGHT_FLUSH(8);

    private int scoreIndex;

    private Score(int score) {
        this.scoreIndex = score;
    }

    public int getScoreIndex() {
        return scoreIndex;
    }
}
