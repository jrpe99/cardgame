package dk.game.card.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dealer {
	private Random random = new Random();

	public List<Hand> deal(Deck deck, int numberOfHands, int numberOfCards) {
		List<Hand> handList = new ArrayList<>();
		List<Card> cardList = deck.getCardList();
		for (int i = 0; i < numberOfHands; i++) {
			handList.add(new Hand());
		}
		
		for (int i = 0; i < numberOfCards; i++) {
			for (int j = 0; j < numberOfHands; j++) {
				int cardIndex = random.nextInt(cardList.size());
				Card card = cardList.get(cardIndex);
				handList.get(j).addCard(card);
				cardList.remove(cardIndex);
			}
		}
		return handList;
	}
}
