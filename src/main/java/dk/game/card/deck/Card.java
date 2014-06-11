package dk.game.card.deck;

public class Card implements Comparable<Card> {
	private Suit suit = null;
	private Rank rank = null;

	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public int getIndex() {
		return suit.getSuitIndex()*13 + rank.getRankIndex();
	}
	
	@Override
	public String toString() {
		return "Card [suit=" + suit + ", rank=" + rank + ", index=" + hashCode() + "]";
	}

	@Override
	public int hashCode() {
		int result = getIndex();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (rank != other.rank)
			return false;
		if (suit != other.suit)
			return false;
		return true;
	}

//	@Override
//	public int compareTo(Card o) {
//		if(o.getIndex() > getIndex()) return 1;
//		if(o.getIndex() == getIndex()) return 0;
//		if(o.getIndex() < getIndex()) return -1;
//		return 0;
//	}
	public int compareTo(Card o) {
		if(o.getRank().getRankIndex() > getRank().getRankIndex()) return 1;
		if(o.getRank().getRankIndex() == getRank().getRankIndex()) return 0;
		if(o.getRank().getRankIndex() < getRank().getRankIndex()) return -1;
		return 0;
	}
}
