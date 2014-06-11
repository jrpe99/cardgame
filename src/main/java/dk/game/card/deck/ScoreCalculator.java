package dk.game.card.deck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreCalculator {
	public static Hand theWinnerIs(List<Hand> hands) {
		hands.stream().forEach(h -> ScoreCalculator.score(h));
		return hands.stream().max(new Hand()).get();
	}
	
	public static void score(Hand hand) {
		Score score = Score.NO_SCORE;

		Map<Rank, List<Card>> scoreMap = createScoreMap(hand); 
		
		int pair = pair(scoreMap);
		if(pair == 1) score = Score.PAIR;
		if(pair == 2) score = Score.TO_PAIR;
		
		int threeOfAKind = threeOfAKind(scoreMap);
		if(threeOfAKind == 1) score = Score.THREE_OF_A_KIND;
		
		int fourOfAKind = fourOfAKind(scoreMap);
		if(fourOfAKind == 1) score = Score.FOUR_OF_A_KIND;
		
		if(pair == 1 && threeOfAKind == 1)  score = Score.FULL_HOUSE;
		
		boolean flush = flush(hand);
		if(flush) score = Score.FLUSH;
		
		boolean straight = straight(hand);
		if(straight) score = Score.STRAIGHT;
		if(straight && flush) score = Score.STRAIGHT_FLUSH;
		
		hand.setScore(score);
	}
	
	private static Map<Rank, List<Card>> createScoreMap(Hand hand) {
		Map<Rank, List<Card>> scoreMap = new HashMap<>();
		for (Rank rank : Rank.values()) {
			List<Card> cardList = hand.getCards().stream()
					.filter(card -> card.getRank().name().equals(rank.name()))
					.collect(Collectors.toList());

			if (cardList.size() > 1) {
				scoreMap.put(rank, cardList);
			}
		}
		return scoreMap;
	}

	private static int pair(Map<Rank, List<Card>> scoreMap) {
		List<Rank> collect = findScore(scoreMap, 2);
		return collect.size();
	}

	private static int threeOfAKind(Map<Rank, List<Card>> scoreMap) {
		List<Rank> collect = findScore(scoreMap, 3);
		return collect.size();
	}

	private static int fourOfAKind(Map<Rank, List<Card>> scoreMap) {
		List<Rank> collect = findScore(scoreMap, 4);
		return collect.size();
	}
	
	private static boolean straight(Hand hand) {
		List<Rank> rankList = hand.getRankList();
		boolean straight = true;
		for (int i = 0; i < rankList.size()-1; i++) {
			Rank rank = rankList.get(i);
			Rank nextRank = Rank.getNextRank(rank);
			if(!nextRank.equals(rankList.get(i+1))) {
				straight = false;
				break;
			}
		}
		return straight;
	}
	
	private static boolean flush(Hand hand) {
		long count = hand.getSuitList().stream().distinct().count();
		if(count == 1) return true;
		return false;
	}

	private static List<Rank> findScore(Map<Rank, List<Card>> scoreMap, int numberOfCards) {
		List<Rank> collect = scoreMap.keySet().stream()
				.filter(rank -> scoreMap.get(rank).size() == numberOfCards)
				.collect(Collectors.toList());
		return collect;
	}
}
