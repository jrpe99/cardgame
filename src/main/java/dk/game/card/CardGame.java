package dk.game.card;

import dk.game.card.message.GameMessage;
import dk.game.card.message.request.LoginRequestMessage;
import dk.game.card.message.response.HandResponse;
import dk.game.card.message.response.LoginResponseMessage;
import dk.game.card.user.GameUser;
import dk.java8.game.card.Card;
import dk.java8.game.card.Dealer;
import dk.java8.game.card.Deck;
import dk.java8.game.card.Hand;
import dk.java8.game.card.score.ScoreCalculator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.websocket.Session;

/**
 * Implements the card game protocol.
 */
public class CardGame {

    private GameState state;

    /*
     * ID of the game used for communication
     */
    private final String id = "1";

    /*
     * List of remote clients (Peers)
     */
    private List<Session> sessionList = new ArrayList<>();

    private Timer gameRunningTimer;
    
    private Deck deck = null;

    public CardGame() {
        this.state = GameState.PRE_GAME;
    }

    synchronized void addSession(Session arc) {
        sessionList.add(arc);
    }

    public synchronized void removeSession(Session arc) {
        sessionList.remove(arc);
    }

    public void handleLoginRequest(LoginRequestMessage messsage, Session session) {
        synchronized (id) {
            LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
            if (state == GameState.PRE_GAME || state == GameState.GAME_RUNNING) {
                String message = null;
                GameUser gameUser = null;
                if (!getRemoteClients().contains(session)) {
                    this.addSession(session);
                    gameUser = new GameUser(UUID.randomUUID().toString());
                    session.getUserProperties().put("user", gameUser);
                    message = "Welcome! ID : " + gameUser.getGameId();
                } else {
                    gameUser = (GameUser)session.getUserProperties().get("user");
                    message = "Welcome back! ID : " + gameUser.getGameId();
                }
                try {
                    loginResponseMessage.setMessage(message);
                    loginResponseMessage.setLoginId(gameUser.getGameId());
                    loginResponseMessage.setLoginStatus(true);
                    System.out.println(loginResponseMessage.toJson());
                    session.getBasicRemote().sendText(loginResponseMessage.toJson());
                } catch (IOException e) {
                    Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
                try {
                    session.getBasicRemote().sendText(loginResponseMessage.toJson());
                } catch (IOException e) {
                    Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    public void handleDealRequest(GameMessage message, Session arc) {
        synchronized (id) {
            if (state == GameState.GAME_RUNNING) {
                int numberOfUsers = sessionList.size();
                deck = new Deck();
                List<Hand> hands = new Dealer().deal(deck, numberOfUsers, 5);
                sessionList.forEach(s -> {
                    GameUser user = (GameUser)s.getUserProperties().get("user");
                    Hand hand = hands.get(0);
                    ScoreCalculator.score(hand);
                    user.setHand(hand);
                    hands.remove(0);
                    HandResponse handResp = new HandResponse();
                    handResp.setScore(hand.getScore().toString());
                    List<Card> cards = hand.getCards().stream().sorted().collect(Collectors.toList());
                    Card card = cards.get(0);
                    handResp.setCard1(card.getSuit().toString().toLowerCase()+card.getRank().toString().toLowerCase());
                    card = cards.get(1);
                    handResp.setCard2(card.getSuit().toString().toLowerCase()+card.getRank().toString().toLowerCase());
                    card = cards.get(2);
                    handResp.setCard3(card.getSuit().toString().toLowerCase()+card.getRank().toString().toLowerCase());
                    card = cards.get(3);
                    handResp.setCard4(card.getSuit().toString().toLowerCase()+card.getRank().toString().toLowerCase());
                    card = cards.get(4);
                    handResp.setCard5(card.getSuit().toString().toLowerCase()+card.getRank().toString().toLowerCase());
                    try {
                        s.getBasicRemote().sendText(handResp.toJson());
                    } catch (IOException ex) {
                        Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    private void sendGameUpdateMessage() {
        /*
        CardGameMessage.PriceUpdateResponseMessage purm = new CardGameMessage.PriceUpdateResponseMessage(id, "" + bestBid);
        for (Session arc : getRemoteClients()) {
            try {
                arc.getBasicRemote().sendObject(purm);
            } catch (IOException | EncodeException e) {
                Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        */
    }

    public void switchStateToGameFinished() {
        synchronized (id) {
            state = GameState.GAME_FINISHED;
        }
        stopGameTimeBroadcast();
        sendGameResults();
    }

    public void switchStateToGameStarted() {
        if(state == GameState.PRE_GAME || state == GameState.GAME_FINISHED) {
            synchronized (id) {
                state = GameState.GAME_RUNNING;
            }
            startGameTimeBroadcast();
        }
    }

    private void sendGameResults() {
        Session bestBidder = null;
/*
        if(bestBidderName != null){
            for (Session session : getRemoteClients()) {
                if(session.getUserProperties().get("name").equals(bestBidderName)){
                    bestBidder = session;
                }
            }
        }

        if (bestBidder!= null) {
            CardGameMessage.ResultMessage winnerMessage = new CardGameMessage.ResultMessage(id, String.format("Congratulations, You won the auction and will pay %.0f.", bestBid));
            try {
                bestBidder.getBasicRemote().sendObject(winnerMessage);
            } catch (IOException | EncodeException e) {
                Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        CardGameMessage.ResultMessage loserMessage = new CardGameMessage.ResultMessage(id, String.format("You did not win the auction. The item was sold for %.0f.", bestBid));
        for (Session arc : getRemoteClients()) {
            if (arc != bestBidder) {
                try {
                    arc.getBasicRemote().sendObject(loserMessage);
                } catch (IOException | EncodeException e) {
                    Logger.getLogger(CardGame.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
*/        
    }

    private void startGameTimeBroadcast() {
        if(gameRunningTimer == null) {
            synchronized (id) {
                state = GameState.GAME_RUNNING;
            }
            gameRunningTimer = new Timer();
            gameRunningTimer.schedule(new GameTimeBroadcasterTask(this), 0, 1000);
        }
    }

    private void stopGameTimeBroadcast() {
        gameRunningTimer.cancel();
        gameRunningTimer = null;
    }

    public String getId() {
        return id;
    }

    public List<Session> getRemoteClients() {
        return Collections.unmodifiableList(sessionList);
    }
}
