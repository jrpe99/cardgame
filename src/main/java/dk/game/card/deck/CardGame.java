package dk.game.card.deck;

import java.util.List;

public class CardGame {

	public static void jpmain(String[] args) {
		Deck deck = new Deck();
		
		List<Hand> hands = new Dealer().deal(deck, 5, 5);
		hands.stream().forEach(h -> 
		{
			System.out.println("\n### HAND ###");
			h.getCards().stream()
				.sorted()
				.forEach(c -> System.out.println(c));
			ScoreCalculator.score(h);
			System.out.println("### HAND SCORE : " + h.getScore());

		});
		Hand theWinner = ScoreCalculator.theWinnerIs(hands);
		if(!theWinner.getScore().equals(Score.NO_SCORE)) {
			System.out.println("\n######### THE WINNER ##########");
			System.out.println("\n######### "+theWinner.getScore()+" ##########");
			theWinner.getCards().stream().forEach(c -> System.out.println(c));
		} else {
			System.out.println("\n######### NO WINNER ##########");
		}
	}
}
