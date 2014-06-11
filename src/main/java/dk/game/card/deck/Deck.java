package dk.game.card.deck;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private List<Card> cardList = new ArrayList<Card>();
	public Deck() {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				cardList.add(new Card(suit, rank));
			}
		}
	}
	public List<Card> getCardList() {
		return cardList;
	}

	public void printDeck() {
		for (Card card : cardList) {
			System.out.println(card);
		}
	}
}
