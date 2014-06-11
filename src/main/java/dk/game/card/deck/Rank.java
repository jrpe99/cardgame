package dk.game.card.deck;

public enum Rank {

    DEUCE(1),
    THREE(2),
    FOUR(3),
    FIVE(4),
    SIX(5),
    SEVEN(6),
    EIGHT(7),
    NINE(8),
    TEN(9),
    JACK(10),
    QUEEN(11),
    KING(12),
    ACE(13);

    private int rankIndex;

    private Rank(int rankIndex) {
        this.rankIndex = rankIndex;
    }

    public int getRankIndex() {
        return rankIndex;
    }

    public static Rank getNextRank(Rank rank) {
        if (rank.equals(Rank.ACE)) {
            return null;
        }
        return Rank.values()[rank.rankIndex];
    }
}
