package dk.game.card.deck;

import java.util.Comparator;

public class HandComparator implements Comparator<Hand> {
    @Override
    public int compare(Hand o1, Hand o2) {
        if (o1.getScore().getScoreIndex() > o2.getScore().getScoreIndex()) {
            return 1;
        }
        if (o1.getScore().getScoreIndex() == o2.getScore().getScoreIndex()) {
            return 0;
        }
        if (o1.getScore().getScoreIndex() < o2.getScore().getScoreIndex()) {
            return -1;
        }
        return 0;
    }
}
