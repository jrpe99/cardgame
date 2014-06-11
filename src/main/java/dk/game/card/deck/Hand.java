package dk.game.card.deck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Hand implements Comparator<Hand> {

    private Score score = null;
    private List<Card> hand = new ArrayList<Card>();

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getCards() {
        return hand;
    }

    public List<Suit> getSuitList() {
        return this.getCards().stream()
                .map(card -> card.getSuit())
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Rank> getRankList() {
        return this.getCards().stream()
                .map(card -> card.getRank())
                .sorted()
                .collect(Collectors.toList());
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((score == null) ? 0 : score.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Hand other = (Hand) obj;
        if (score != other.score) {
            return false;
        }
        return true;
    }

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

    @Override
    public String toString() {
        return "Hand [score=" + score + ", hand=" + hand + "]";
    }

}
