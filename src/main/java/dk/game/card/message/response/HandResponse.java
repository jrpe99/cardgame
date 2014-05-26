/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.game.card.message.response;

import dk.game.card.message.GameMessage;
import dk.java8.game.card.Card;
import dk.java8.game.card.Rank;
import dk.java8.game.card.Suit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jorperss
 */
public class HandResponse extends GameMessage {

    private String score;
    private List<String> cardList = new ArrayList<>();

    public HandResponse() {
        this.setType(HAND_RESPONSE);
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void addCard(Suit suit, Rank rank) {
        String cardString = suit.toString().toLowerCase()+rank.toString().toLowerCase();        
        this.cardList.add(cardString);
    }
    public List<String> getCardList() {
        return cardList;
    }

    public void setCardList(List<String> cardList) {
        this.cardList = cardList;
    }
}
