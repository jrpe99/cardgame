package dk.game.card.deck;

public enum Suit {
	HEARTS(0), 
	DIAMONDS(1), 
	CLUBS(2), 
	SPADES(3);
	
	private int suitIndex;

	private Suit(int suitIndex) {
		this.suitIndex = suitIndex;
	}

	public int getSuitIndex() {
		return suitIndex;
	}
}
